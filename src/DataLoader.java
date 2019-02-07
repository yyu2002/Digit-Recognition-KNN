import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataLoader {

    private static String normalizeLineBreaks(String s) {
        return s.replace("\r\n", "\n").replace('\r', '\n');
    }

    public static String readFileAsString(String filepath) {
        ClassLoader classLoader = DataLoader.class.getClassLoader();
        File file = new File(classLoader.getResource(filepath).getFile());

        // Read File Content
        String content = "";
        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            System.err.println("FILE NOT FOUND: " + filepath);
            e.printStackTrace();
        }

        return content;
    }

    public static List<DataPoint> createDataSet(String filepath) {
        String data = normalizeLineBreaks(readFileAsString(filepath));
        String[] lines = data.split("\n");

        // create storage for data
        ArrayList<DataPoint> dataset = new ArrayList<>();

        for (int a = 0; a < lines.length; a++) {
            String[] line = lines[a].split(",");
            String label = line[0];
            short[] nums = new short[line.length - 1];

            for (int i = 1; i < line.length; i++) {
                nums[i - 1] = Short.parseShort(line[i]);

                if (nums[i - 1] > 122.5)
                    nums[i - 1] = 0;
                else
                    nums[i - 1] = 255;
            }

            short[][] pixels = new short[28][28];
            int loc = 0;
            for (int r = 0; r < pixels.length; r++) {
                for (int c = 0; c < pixels[0].length; c++) {
                    pixels[r][c] = nums[loc];
                    loc++;
                }
            }

            DImage img = new DImage(28, 28);
            img.setPixels(pixels);

            DataPoint point = new DataPoint(label, img);
            dataset.add(point);
        }
        return dataset;
    }
}