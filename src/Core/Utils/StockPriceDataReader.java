package Core.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class StockPriceDataReader {
    private Scanner scanner;
    private String relativeFilePath = "/src/Core/Data/AMIA stock prices.csv";
    private LinkedList<Double> prices = new LinkedList<>();
    private LinkedList<Double> volumes = new LinkedList<>();

    public StockPriceDataReader() throws FileNotFoundException {
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + relativeFilePath;
        scanner = new Scanner(new File(filePath));
        scanner.nextLine();
        while (scanner.hasNext()) {
            String[] data = scanner.nextLine().split(",");
            prices.add(Double.valueOf(data[1]));
            String dirtyVolume = data[5];
            // Remove K and multiply by 1000
            Double volume = Double.parseDouble(dirtyVolume.substring(0, dirtyVolume.length() - 1)) * 1000;
            volumes.add(volume);
        }
    }

    public LinkedList<Double> getPrices() {
        return prices;
    }

    public LinkedList<Double> getVolumes() {
        return volumes;
    }
}
