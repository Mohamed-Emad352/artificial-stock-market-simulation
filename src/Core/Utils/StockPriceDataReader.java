package Core.Utils;

import Core.Configurations.SimulationParameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class StockPriceDataReader {
    private Scanner scanner;
    private ArrayList<Float> closePrices = new ArrayList<>();
    private ArrayList<Float> openPrices = new ArrayList<>();
    private ArrayList<Float> highPrices = new ArrayList<>();
    private ArrayList<Float> lowPrices = new ArrayList<>();
    private ArrayList<Float> volumes = new ArrayList<>();

    public StockPriceDataReader() throws FileNotFoundException {
        String projectDir = System.getProperty("user.dir");
        String absoluteFilePath = projectDir + SimulationParameters.relativeStockDataPath;
        scanner = new Scanner(new File(absoluteFilePath));
        scanner.nextLine();
        ArrayList<String[]> data = new ArrayList<>();
        while (scanner.hasNext()) {
            String[] dataLine = scanner.nextLine().split(",");
            data.add(dataLine);
        }
        for (int i = data.size() - 1;  i >= 0; i--) {
            String[] dataLine = data.get(i);
            closePrices.add(Float.valueOf(dataLine[1]));
            openPrices.add(Float.valueOf(dataLine[2]));
            highPrices.add(Float.valueOf(dataLine[3]));
            lowPrices.add(Float.valueOf(dataLine[4]));
            String dirtyVolume = dataLine[5];
            // Remove K and multiply by 1000
            Float volume = Float.parseFloat(dirtyVolume.substring(0, dirtyVolume.length() - 1)) * 1000;
            volumes.add(volume);
        }
    }

    public ArrayList<Float> getClosePrices() {
        return closePrices;
    }
    public ArrayList<Float> getOpenPrices() {
        return openPrices;
    }

    public ArrayList<Float> getVolumes() {
        return volumes;
    }

    public ArrayList<Float> getHighPrices() {
        return highPrices;
    }

    public ArrayList<Float> getLowPrices() {
        return lowPrices;
    }
}
