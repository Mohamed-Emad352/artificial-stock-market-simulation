package Core.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class StockPriceDataReader {
    private Scanner scanner;
    private String relativeFilePath = "/src/Core/Data/AMIA stock prices.csv";
    private LinkedList<Double> closePrices = new LinkedList<>();
    private LinkedList<Double> openPrices = new LinkedList<>();
    private LinkedList<Double> highPrices = new LinkedList<>();
    private LinkedList<Double> lowPrices = new LinkedList<>();
    private LinkedList<Double> volumes = new LinkedList<>();

    public StockPriceDataReader() throws FileNotFoundException {
        String projectDir = System.getProperty("user.dir");
        String absoluteFilePath = projectDir + relativeFilePath;
        scanner = new Scanner(new File(absoluteFilePath));
        scanner.nextLine();
        while (scanner.hasNext()) {
            String[] data = scanner.nextLine().split(",");
            closePrices.add(Double.valueOf(data[1]));
            openPrices.add(Double.valueOf(data[2]));
            highPrices.add(Double.valueOf(data[3]));
            lowPrices.add(Double.valueOf(data[4]));
            String dirtyVolume = data[5];
            // Remove K and multiply by 1000
            Double volume = Double.parseDouble(dirtyVolume.substring(0, dirtyVolume.length() - 1)) * 1000;
            volumes.add(volume);
        }
    }

    public LinkedList<Double> getClosePrices() {
        return closePrices;
    }
    public LinkedList<Double> getOpenPrices() {
        return openPrices;
    }

    public LinkedList<Double> getVolumes() {
        return volumes;
    }

    public LinkedList<Double> getHighPrices() {
        return highPrices;
    }

    public LinkedList<Double> getLowPrices() {
        return lowPrices;
    }
}
