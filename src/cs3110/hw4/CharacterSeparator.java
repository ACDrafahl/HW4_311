package cs3110.hw4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; // Added
// Added for testing
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
            //System.out.println("Saving processed image as: " + inputPath + ".new.bmp");
            
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
            List<String> vertices = new ArrayList<>();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    vertices.add(i + ","+ j);
                }
            }

            String sourceRow = "SOURCE_ROW"; // Dummy source vertex for constant Dijkstra calls
            vertices.add(sourceRow);
            String sourceCol = "SOURCE_COL"; // Dummy source vertex for constant Dijkstra calls
            vertices.add(sourceCol);

            WeightedGraph<String> graph = new WeightedAdjacencyList<>(vertices);

            // Pair pixels with their neighbors
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    String currentVertex = i + "," + j;
                    int currentValue = getIntensity(pixelMatrix[i][j]);

                    if (j + 1 < width) {
                        String rightVertex = i + "," + (j + 1);
                        int rightValue = getIntensity(pixelMatrix[i][j + 1]);
                        if (currentValue == 255 && rightValue == 255) {
                            graph.addEdge(currentVertex, rightVertex, 0); // Add edge with weight 0
                            graph.addEdge(rightVertex, currentVertex, 0); // Add edge in the opposite direction
                        }
                    }

                    if (i + 1 < height) {
                        String downVertex = (i + 1) + "," + j;
                        int downValue = getIntensity(pixelMatrix[i + 1][j]);
                        if (currentValue == 255 && downValue == 255) {
                            graph.addEdge(currentVertex, downVertex, 0); // Add edge with weight 0
                            graph.addEdge(downVertex, currentVertex, 0); // Add edge in the opposite direction
                        }
                    }
                }
            }
            
            // Add edges from the source vertex to all vertices in the first row
            for (int row = 0; row < height; row++) {
                String vertex = row + ",0";
                graph.addEdge(sourceRow, vertex, 0);
            }

            // Run Dijkstra's algorithm from the source row
            Map<String, Long> rowDistances = graph.getShortestPaths(sourceRow);

            // Add cheap right side edges to the graph
            for (int row = 0; row < height; row++) {
                String rightPixel = row + "," + (width - 1);
                Long cost = rowDistances.get(rightPixel);
                if (cost != null) {
                    rowSeps.add(row);
                }
            }

            // Add edges from the source vertex to all vertices in the first column
            for (int col = 0; col < width; col++) {
                String vertex = "0," + col;
                graph.addEdge(sourceCol, vertex, 0);
            }

            // Run Dijkstra's algorithm from the source column
            Map<String, Long> colDistances = graph.getShortestPaths(sourceCol);

            // Add cheap bottom side edges to the graph
            for (int col = 0; col < width; col++) {
                String bottomPixel = (height - 1) + "," + col;
                Long cost = colDistances.get(bottomPixel);
                if (cost != null) {
                    colSeps.add(col);
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        return new Pair<>(rowSeps, colSeps);
    }

    // Unsure about this one
    private static int getIntensity(int argb) {
        // Color color = new Color(argb);
        // return (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
        
        
        Color color = new Color(argb);
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return (red + green + blue) / 3; 

        
        // int r = (argb >> 16) & 0xFF;
        // int g = (argb >> 8) & 0xFF;
        // int b = argb & 0xFF;
        // return (int)(0.299 * r + 0.587 * g + 0.114 * b);  // perceptual grayscale
    }

    // Unsure about this one
    private static long getThreshold(int length) {
        // The threshold is a function of the length of the image
        return length / 10;
    }
}
