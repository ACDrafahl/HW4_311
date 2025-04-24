package cs3110.hw4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class CharacterSeparator {
    // Added for testing
    public static void visualizeSeparations(String inputPath) {
        try {
            System.out.println("Loading image from: " + inputPath);

            Pair<List<Integer>, List<Integer>> separations = findSeparationWeighted(inputPath);
            List<Integer> rowSeps = separations.getFirst();
            List<Integer> colSeps = separations.getSecond();

            BitmapProcessor processor = new BitmapProcessor(inputPath);
            BufferedImage image = processor.bi;
            int width = image.getWidth();
            int height = image.getHeight();

            System.out.println("Found " + rowSeps.size() + " row separations");
            System.out.println("Found " + colSeps.size() + " column separations");

            for (Integer row : rowSeps) {
                for (int x = 0; x < width; x++) {
                    image.setRGB(x, row, Color.RED.getRGB());
                }
            }

            for (Integer col : colSeps) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(col, y, Color.GREEN.getRGB());
                }
            }

            processor.writeToFile();
        } catch (IOException e) {
            System.out.println("Error processing image: " + e.getMessage());
        }
    }

    /**
     * This method uses the WeightedAdjacencyList class to identify the space
     * between characters in an image of text.
     * For efficiency, it should only construct a single graph object and should
     * only make a constant number of calls to Dijkstra's algorithm.
     * 
     * @param path The location of the image on disk.
     * @return Two lists of Integer. The first list indicates whitespace rows. The
     *         second list indicates whitespace columns. Returns null if some error
     *         occurred loading the image.
     */
    public static Pair<List<Integer>, List<Integer>> findSeparationWeighted(String path) {
        List<Integer> rowSeps = new ArrayList<>();
        List<Integer> colSeps = new ArrayList<>();

        try {
            // Load the image and get the pixel matrix
            BitmapProcessor processor = new BitmapProcessor(path);
            int[][] pixelMatrix = processor.getRGBMatrix();

            int height = pixelMatrix.length;
            int width = pixelMatrix[0].length;
            List<String> vertices = new ArrayList<>();

            String sourceRow = "SOURCE_ROW";
            String sourceCol = "SOURCE_COL";

            vertices.add(sourceRow);
            vertices.add(sourceCol);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    vertices.add(i + "," + j);
                }
            }

            WeightedAdjacencyList<String> graph = new WeightedAdjacencyList<>(vertices);

            // Set up weighted edges between pixels
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    String current = i + "," + j;

                    // Right neighbor
                    if (j + 1 < width) {
                        String right = i + "," + (j + 1);
                        int weight = getPixelCost(pixelMatrix[i][j], pixelMatrix[i][j + 1]);
                        graph.addEdge(current, right, weight);
                    }

                    // Down neighbor
                    if (i + 1 < height) {
                        String down = (i + 1) + "," + j;
                        int weight = getPixelCost(pixelMatrix[i][j], pixelMatrix[i + 1][j]);
                        graph.addEdge(current, down, weight);
                    }
                }
            }

            // Add edges from the source vertex to all vertices in the first column of each row
            for (int i = 0; i < height; i++) {
                graph.addEdge(sourceRow, i + ",0", 0);
            }
            // Run Dijkstra's algorithm from the source row
            Map<String, Long> rowDistances = graph.getShortestPaths(sourceRow);
            for (int i = 0; i < height; i++) {
                String rightMost = i + "," + (width - 1);
                Long cost = rowDistances.get(rightMost);
                if (cost != null && cost < getThreshold(width)) {
                    rowSeps.add(i);
                }
            }

            // Add edges from the source vertex to all vertices in the top row of each column
            for (int j = 0; j < width; j++) {
                graph.addEdge(sourceCol, "0," + j, 0);
            }
            // Run Dijkstra's algorithm from the source column
            Map<String, Long> colDistances = graph.getShortestPaths(sourceCol);
            for (int j = 0; j < width; j++) {
                String bottomMost = (height - 1) + "," + j;
                Long cost = colDistances.get(bottomMost);
                if (cost != null && cost < getThreshold(height)) {
                    colSeps.add(j);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        // output list of row and column separations to a text file for testing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("separations.txt"))) {
            writer.write("Row separations:\n");
            for (Integer row : rowSeps) {
                writer.write(row + "\n");
            }
            writer.write("Column separations:\n");
            for (Integer col : colSeps) {
                writer.write(col + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        return new Pair<>(rowSeps, colSeps);
    }

    // Get the average intensity of a pixel
    private static int getIntensity(int argb) {
        Color color = new Color(argb);
        return (color.getRed() + color.getGreen() + color.getBlue()) / 3;
    }

    // Assign higher cost to darker pixel paths, white pixels ~5, darker = more
    private static int getPixelCost(int argb1, int argb2) {
        int intensity1 = getIntensity(argb1);
        int intensity2 = getIntensity(argb2);
        int avgIntensity = (intensity1 + intensity2) / 2;
        return 5 + (255 - avgIntensity) / 8;
    }

    // This threshold is used to determine if the cost of a path is low enough to
    // indicate a separation between characters. It should be adjusted based on the
    // expected noise in the image.
    private static long getThreshold(int length) {
        return length * 5; // Adjust as needed to tolerate slight noise
    }
}
