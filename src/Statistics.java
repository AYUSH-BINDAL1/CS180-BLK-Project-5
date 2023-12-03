import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Statistics
 * <p>
 * Handles all information regarding statistics feature for both customer and seller
 *
 * @author Ayush Bindal, Lab #L08
 * @version 11/30/2023
 * <p>
 */

public class Statistics {
    //TODO: Create customerDashboardClient method
    public static ArrayList<String> customerDashboardServer(Object PURCHASEHISTORYLOCK, Object PRODUCTLOCK) {
        //seller store product quantity
        ArrayList<String> sortedBought = new ArrayList<>();
        ArrayList<String> allProducts;
        ArrayList<String> purchaseHistory;
        try {
            synchronized (PRODUCTLOCK) {
                allProducts =  (ArrayList<String>) Files.readAllLines(Paths.get("Products.txt"));
            }
            for (int currentLine = 0; currentLine < allProducts.size(); currentLine++) {
                String[] words = allProducts.get(currentLine).split(",");
                sortedBought.add(words[3] + "," + words[2] + "," + words[0] + ",0"); // Get sellers from products.txt
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            synchronized (PURCHASEHISTORYLOCK) {
                purchaseHistory =  (ArrayList<String>) Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            }
            for (int currentHistory = 0; currentHistory < purchaseHistory.size(); currentHistory++) {

                String[] words = purchaseHistory.get(currentHistory).split(",");
                int location = -1;
                for (int i = 0; i < sortedBought.size(); i++) {
                    if (sortedBought.get(i).contains(words[3] + "," + words[2] + "," + words[0])) {
                        // Iterate through to see which stores, store in arraylist, check if duplicate
                        location = i;
                        break;
                    }
                }
                if (location != -1) {
                    String[] arrayWords = sortedBought.get(location).split(",");
                    try {
                        int addQuantity = Integer.parseInt(words[5]);
                        int currentQuantity = Integer.parseInt(arrayWords[arrayWords.length - 1]);
                        int total = addQuantity + currentQuantity;
                        sortedBought.set(location, words[3] + "," + words[2] + "," + words[0] + "," + total);
                    } catch (NumberFormatException e) {
                        System.out.println("PurchaseHistoryFile.txt Error!");
                    }
                } else {
                    sortedBought.add(words[3] + "," + words[2] + "," + words[0] + "," + words[5]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // This is dependent on case from the productHistory file
        }

        if (sortedBought.isEmpty()) {
            System.out.println("No Products in File/File could not be read!");
            // This is dependent on case from the productHistory file
            return null;
        }

        Collections.sort(sortedBought);
        // Returns arraylist of strings from purchase history in the format of
        // CustomerUsername, productName, quantity, price, sellerUsername, store
        return sortedBought;
    }
    //TODO: Create customerDashboardSpecificClient method
    public static ArrayList<String> customerDashboardSpecificServer(String customerEmail, Object PURCHASEHISTORYLOCK) {
        // Seller store product quantity
        ArrayList<String> sortedBought = new ArrayList<>();
        ArrayList<String> totalPurchaseHistory;
        try {
            synchronized (PURCHASEHISTORYLOCK) {
                totalPurchaseHistory = (ArrayList<String>) Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            }
            for (int currentLine = 0; currentLine < totalPurchaseHistory.size(); currentLine++) {
                String[] words = totalPurchaseHistory.get(currentLine).split(",");
                if (words[6].equalsIgnoreCase(customerEmail)) {
                    if (!sortedBought.contains(words[3] + "," + words[2] + "," + words[0])) {
                        sortedBought.add(words[3] + "," + words[2] + "," + words[0] + "," + words[5]);
                    } else {
                        int location = -1;
                        for (int i = 0; i < sortedBought.size(); i++) {
                            if (sortedBought.get(i).contains(words[3] + "," + words[2] + "," + words[0])) {
                                location = i;
                                break;
                            }
                        }
                        if (location != -1) {
                            String[] arrayWords = sortedBought.get(location).split(",");
                            try {
                                int addQuantity = Integer.parseInt(words[5]);
                                int currentQuantity = Integer.parseInt(arrayWords[arrayWords.length - 1]);
                                int total = addQuantity + currentQuantity;
                                sortedBought.set(location,
                                        words[3] + "," + words[2] + "," + words[0] + "," + total);
                            } catch (NumberFormatException e) {
                                System.out.println("PurchaseHistoryFile.txt Error!");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sortedBought.isEmpty()) {
            System.out.println("No purchases!");
        }
        Collections.sort(sortedBought);
        // Returns arraylist of strings from purchase history in the format of
        // CustomerUsername, productName, quantity, price, sellerUsername, store
        return sortedBought;
    }
    //TODO: Create sortCustomerLowtoHighClient method
    //sort from low to high
    public static ArrayList<String> sortCustomerLowtoHighServer(ArrayList<String> toSort) {
        // selection sort
        for (int i = 0; i < toSort.size() - 1; i++) {
            int min = i;
            for (int j = i + 1; j < toSort.size(); j++) {
                String[] wordsMin = toSort.get(min).split(",");
                int minQuantity = Integer.parseInt(wordsMin[wordsMin.length - 1]);

                String[] wordsCurrent = toSort.get(j).split(",");
                int currentQuantity = Integer.parseInt(wordsCurrent[wordsCurrent.length - 1]);

                if (currentQuantity < minQuantity) {
                    min = j;
                }
            }
            if (min != i) {
                String temp = toSort.get(i);
                toSort.set(i, toSort.get(min));
                toSort.set(min, temp);
            }
        }
        return toSort;
    }

    //TODO: Create sortCustomerHightoLowClient method
    //TODO: Create sortCustomerHightoLowServer method
    //sort from high to low
    public static ArrayList<String> sortQuantityMax(ArrayList<String> toSort) {
        // selection sort
        for (int i = 0; i < toSort.size() - 1; i++) {
            int max = i;
            for (int j = i + 1; j < toSort.size(); j++) {
                String[] wordsMax = toSort.get(max).split(",");
                int maxQuantity = Integer.parseInt(wordsMax[wordsMax.length - 1]);

                String[] wordsCurrent = toSort.get(j).split(",");
                int currentQuantity = Integer.parseInt(wordsCurrent[wordsCurrent.length - 1]);

                if (currentQuantity > maxQuantity) {
                    max = j;
                }
            }
            if (max != i) {
                String temp = toSort.get(i);
                toSort.set(i, toSort.get(max));
                toSort.set(max, temp);
            }
        }
        return toSort;
    }


    //TODO: Create viewSalesByStoreClient method
    //TODO: Create viewSalesByStoreServer method

    //TODO: Create generateSellerDashboardServer method
    //TODO: Create generateSellerDashboardClient method

    //TODO: Create sortListClient method (Sub method for generateSeller Dashboard)
    //TODO: Create sortListServer method (Sub method for generateSeller Dashboard)

    //TODO: Create updatePurchaseCountClient method (Sub method for generateSeller Dashboard)
    //TODO: Create updatePurchaseCountServer method (Sub method for generateSeller Dashboard)
}
