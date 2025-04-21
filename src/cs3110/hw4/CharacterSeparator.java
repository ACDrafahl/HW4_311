package cs3110.hw4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
            System.out.println("Saving processed image as: " + inputPath + ".new.bmp");
            
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
        return null;
    }
}
