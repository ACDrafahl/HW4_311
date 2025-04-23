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
        } 
        catch (IOException e) {
            System.out.println("Error processing image: " + e.getMessage());
        }
    }

    /**
     * This method uses the WeightedAdjacencyList class to identify the space
     * between characters in an image of text.
     * For efficiency, it should only construct a single graph object and should
     * only make a constant
     * number of calls to Dijkstra's algorithm.
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

            // Create a graph from the pixel matrix
            int height = pixelMatrix.length;
            int width = pixelMatrix[0].length;
            List<String> rowVertices = new ArrayList<>();
            List<String> colVertices = new ArrayList<>();

            String sourceRow = "SOURCE_ROW"; // Dummy source vertex for constant Dijkstra calls
            String sourceCol = "SOURCE_COL"; // Dummy source vertex for constant Dijkstra calls

            rowVertices.add(sourceRow);
            colVertices.add(sourceCol);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    rowVertices.add(i + "," + j);
                    colVertices.add(i + "," + j);
                }
            }

            WeightedGraph<String> rowGraph = new WeightedAdjacencyList<>(rowVertices);
            WeightedGraph<String> colGraph = new WeightedAdjacencyList<>(colVertices);

            // Set up weighted horizontal edges between pixels for row detection
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width - 1; j++) {
                    String current = i + "," + j;
                    String right = i + "," + (j + 1);
                    int intensity = (getIntensity(pixelMatrix[i][j]) + getIntensity(pixelMatrix[i][j + 1])) / 2;
                    rowGraph.addEdge(current, right, 255 - intensity);
                }
            }

            // Set up weighted vertical edges between pixels for column detection
            for (int i = 0; i < height - 1; i++) {
                for (int j = 0; j < width; j++) {
                    String current = i + "," + j;
                    String down = (i + 1) + "," + j;
                    int intensity = (getIntensity(pixelMatrix[i][j]) + getIntensity(pixelMatrix[i + 1][j])) / 2;
                    colGraph.addEdge(current, down, 255 - intensity);
                }
            }

            // Add edges from the source vertex to all vertices in the first column of each row
            for (int i = 0; i < height; i++) {
                rowGraph.addEdge(sourceRow, i + ",0", 0);
            }

            // Run Dijkstra's algorithm from the source row
            Map<String, Long> rowDistances = rowGraph.getShortestPaths(sourceRow);

            // Identify cheap horizontal paths as row separators
            for (int i = 0; i < height; i++) {
                String rightMost = i + "," + (width - 1);
                Long cost = rowDistances.get(rightMost);
                if (cost != null && cost < getThreshold(width)) {
                    rowSeps.add(i);
                }
            }

            // Add edges from the source vertex to all vertices in the top row of each column
            for (int j = 0; j < width; j++) {
                colGraph.addEdge(sourceCol, "0," + j, 0);
            }

            // Run Dijkstra's algorithm from the source column
            Map<String, Long> colDistances = colGraph.getShortestPaths(sourceCol);

            // Identify cheap vertical paths as column separators
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

    // Unsure about this one
    private static int getIntensity(int argb) {
        Color color = new Color(argb);
        return (color.getRed() + color.getGreen() + color.getBlue()) / 3;
    }

    // Give a threshold as a function of the image size
    private static long getThreshold(int length) {
        return (long) (length * 0.1); // Example threshold: 10% of the image size
    }
}
