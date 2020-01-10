import java.lang.reflect.Array;
import java.util.*;
 
public class Classifier {
    private ArrayList<DataPoint> trainingData;
    private int n;

    public Classifier(int n) {
        this.n = n;
        trainingData = new ArrayList<DataPoint>();
    }

    public void addTrainingData(List<DataPoint> points) {
        for (DataPoint p : points) {
            addTrainingData(p);
        }
    }

    public void addTrainingData(DataPoint point) {
        trainingData.add(point);
    }

    public void addTrainingData(String label, DImage img) {
        addTrainingData(new DataPoint(label, img));
    }

    /*
        public String classify(short[][] pixels) {
            if (trainingData.size() == 0) return "no training data";
            double min = Integer.MAX_VALUE;
            DataPoint nearest = trainingData.get(0);

            for (DataPoint p : trainingData) {
                if (distance(p.getData().getBWPixelGrid(), pixels) < min) {
                    min = distance(p.getData().getBWPixelGrid(), pixels);
                    nearest = p;
                }
            }
            return nearest.getLabel();  // replace this line
        }
    */

    public String classify(short[][] pixels, ArrayList<DataPoint> dataPoints) {
        if (dataPoints.size() == 0) return "no training data";
        double min = Integer.MAX_VALUE;
        DataPoint nearest = dataPoints.get(0);

        for (DataPoint p : dataPoints) {
            if (distance(p.getData().getBWPixelGrid(), pixels) < min) {
                min = distance(p.getData().getBWPixelGrid(), pixels);
                nearest = p;
            }
        }
        dataPoints.remove(nearest);
        return nearest.getLabel();  // replace this line
    }

    public String classify(short[][] pixels) {
        if (trainingData.size() == 0) return "no training data";
        ArrayList<String> nearest = new ArrayList<>();

        ArrayList<DataPoint> copyOfData = cloneTrainingData();
        for (int i = 0; i < n; i++) {
            if (copyOfData.size() > 0)
                nearest.add(classify(pixels, copyOfData));
        }

        System.out.println(nearest);
        return getMostFrequent(nearest);
    }

    public String getMostFrequent(ArrayList<String> list) {
        String mostCommon = list.get(0);
        int highest = 0;
        while (list.size() > 0) {
            String current = list.remove(0);
            int count = 1;
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).equals(current)) {
                    count++;
                    list.remove(i);
                }
            }
            if (count > highest) {
                highest = count;
                mostCommon = current;
            }
        }
        System.out.println(mostCommon);
        return mostCommon;
    }

    public ArrayList<DataPoint> cloneTrainingData() {
        ArrayList<DataPoint> clone = new ArrayList<DataPoint>();
        for (DataPoint dataPoint : trainingData)
            clone.add(dataPoint);
        return clone;
    }

    public String classify(DImage img) {
        return classify(img.getBWPixelGrid());
    }


    public double distance(short[][] d1, short[][] d2) {
        double totalSum = 0;
        for (int i = 0; i < d1.length; i++) {
            for (int j = 0; j < d1[i].length; j++) {
                int sum = d1[i][j] - d2[i][j];
                totalSum += sum * sum;
            }
        }
        return Math.sqrt(totalSum);
    }


    public void test(List<DataPoint> test) {
        ArrayList<DataPoint> correct = new ArrayList<>();
        ArrayList<DataPoint> wrong = new ArrayList<>();

        int i = 0;
        for (DataPoint p : test) {
            String predict = classify(p.getData());
            System.out.print("#" + i + " REAL:\t" + p.getLabel() + " predicted:\t" + predict);
            if (predict.equals(p.getLabel())) {
                correct.add(p);
                System.out.print(" Correct ");
            } else {
                wrong.add(p);
                System.out.print(" WRONG ");
            }

            i++;
            System.out.println(" % correct: " + ((double) correct.size() / i));
        }

        System.out.println(correct.size() + " correct out of " + test.size());
        System.out.println(wrong.size() + " correct out of " + test.size());
        System.out.println("% Error: " + (double) wrong.size() / test.size());
    }
}
