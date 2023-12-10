import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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


    public static ArrayList<String> customerDashboardServer(String sort, Object PURCHASEHISTORYLOCK,
                                                            Object PRODUCTLOCK) {
        //seller store product quantity
        ArrayList<String> sortedBought = new ArrayList<>();
        ArrayList<String> allProducts;
        ArrayList<String> purchaseHistory;
        try {
            synchronized (PRODUCTLOCK) {
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Products.txt"));
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
                purchaseHistory = (ArrayList<String>) Files.readAllLines(Paths.get("PurchaseHistory.txt"));
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
        if(sort.equals("HIGH TO LOW")) {
            return sortCustomerHightoLowServer(sortedBought);
        } else {
            return sortCustomerLowtoHighServer(sortedBought);
        }
    }


    public static ArrayList<String> customerDashboardSpecificServer(String customerEmail,
                                                                    String sort, Object PURCHASEHISTORYLOCK) {
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
        if(sort.equals("HIGH TO LOW")) {
            return sortCustomerHightoLowServer(sortedBought);
        } else {
            return sortCustomerLowtoHighServer(sortedBought);
        }
    }


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


    //sort from high to low
    public static ArrayList<String> sortCustomerHightoLowServer(ArrayList<String> toSort) {
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


    public static String viewSalesByStoreServer(String sellerEmail, Object PURCHASEHISTORYLOCK) {
        ArrayList<String> purchaseHistoryLines; //ArrayList of purchaseHistory from PurchaseHistory.txt
        ArrayList<String> stores = new ArrayList<String>(); //ArrayList of Strings contain stores
        StringBuilder newString = null;
        try {
            synchronized (PURCHASEHISTORYLOCK) {
                //Reads lines from PurchaseHistory
                purchaseHistoryLines = (ArrayList<String>) Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            }

            for (String purchase : purchaseHistoryLines) { //Reads through each line in purchaseHistory and if the
                // stores ArrayList doesn't already have the store and the sellerEmail matches adds the name of the
                // store to the ArrayList
                String[] purchaseSplit = purchase.split(",");
                if (!stores.contains(purchaseSplit[2]) && purchaseSplit[3].equals(sellerEmail)) {
                    stores.add(purchaseSplit[2]);
                }
            }
            newString = new StringBuilder();
            for (int i = 0; i < stores.size(); i++) { //Loops through all the stores ArrayList

                String currentStore = stores.get(i);
                newString.append("Store: ").append(currentStore).append("\n"); //Adds the current store to the
                // StringBuilder

                for (int j = 0; j < purchaseHistoryLines.size(); j++) { //Loops through purchaseHistory and checks
                    // if the purchase was from the currentStore. If it is, it calculates the revenue from the sale
                    // and adds it to the StringBuilder
                    String[] currentPurchaseSplit = purchaseHistoryLines.get(j).split(",");

                    if (currentPurchaseSplit[2].equals(currentStore)) {

                        //Calculates revenue from sale
                        int quantitySold = Integer.parseInt(currentPurchaseSplit[5]);
                        double pricePerUnit = Double.parseDouble(currentPurchaseSplit[4]);
                        double revenueFromSale = quantitySold * pricePerUnit;

                        //Adds the revenue from the sale and customer information to the StringBuilder
                        newString.append("    Product: ").append(currentPurchaseSplit[0]).append("\n");
                        newString.append("        Customer Info: ").append(currentPurchaseSplit[6]).append("\n");
                        newString.append("        Revenue: ").append(revenueFromSale).append("\n");

                    }
                }
                newString.append("\n"); //Adds a new line to the StringBuilder
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(newString.isEmpty()) {
            return "NO SALES";
        } else {
            return newString.toString(); //Returns formatted String of the stores sales
        }
    }


    public static String generateSellerDashboardServer(String sellerEmail, int rank, Object PURCHASEHISTORYLOCK) {

        ArrayList<String> purchaseHistoryLines; //ArrayList of purchaseHistory from PurchaseHistory.txt
        ArrayList<String> stores = new ArrayList<String>(); //ArrayList of Strings contain stores
        StringBuilder sellerDashboard = new StringBuilder(sellerEmail + "'s Dashboard\n"); //StringBuilder of
        // sellerDashboard

        try {

            synchronized (PURCHASEHISTORYLOCK) {
                //Reads lines from PurchaseHistory
                purchaseHistoryLines = (ArrayList<String>) Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            }

            //Loops through each purchase in the purchaseHistory
            for (String purchase : purchaseHistoryLines) {
                String[] purchaseSplit = purchase.split(",");

                // Check if the store related to the purchase is not already in the ArrayList and is associated
                // with the seller
                if (!stores.contains(purchaseSplit[2]) && purchaseSplit[3].equals(sellerEmail)) {
                    ArrayList<String> customerPurchaseCount = new ArrayList<>(); //List to track customer purchase count for a store
                    ArrayList<String> productSalesCount = new ArrayList<>(); //List to track product sales count for a store

                    // Process purchase history for the specified store related to the seller
                    for (String currentPurchase : purchaseHistoryLines) {
                        String[] currentPurchaseSplit = currentPurchase.split(",");

                        // Check if the purchase is related to the specified seller and store
                        if (currentPurchaseSplit[3].equals(sellerEmail) && currentPurchaseSplit[2].equals(purchaseSplit[2])) {
                            // Update customer purchase count
                            updatePurchaseCount(customerPurchaseCount, currentPurchaseSplit[6],
                                    currentPurchaseSplit[5]);

                            // Update product sales count
                            String productKey = currentPurchaseSplit[0].toLowerCase(); // Case-insensitive product key
                            updatePurchaseCount(productSalesCount, productKey, currentPurchaseSplit[5]);
                        }
                    }

                    // Sort the lists of customer purchase count and product sales count based on rank
                    sortList(customerPurchaseCount, rank);
                    sortList(productSalesCount, rank);

                    // Generate the dashboard for the specified store
                    sellerDashboard.append("\nStore: ").append(purchaseSplit[2]);


                    // List of customers with the number of items they have purchased
                    sellerDashboard.append("\nCustomer Purchase Count:\n");
                    if (customerPurchaseCount.isEmpty()) {
                        sellerDashboard.append("    No data! No current products sold for this store!");
                    }
                    for (String customer : customerPurchaseCount) {
                        sellerDashboard.append("    ").append(customer).append("\n");
                    }

                    // List of products with the number of sales
                    sellerDashboard.append("Product Sales Count:\n");
                    if (productSalesCount.isEmpty()) {
                        sellerDashboard.append("    No data! No current products sold for this store!");
                    }
                    for (String entry : productSalesCount) {
                        sellerDashboard.append("    ").append(entry).append("\n");
                    }
                    stores.add(purchaseSplit[2]); // Add the processed store to the list of stores
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sellerDashboard.toString(); //Returns the formatted String of the seller's dashboard
    }


    private static void updatePurchaseCount(ArrayList<String> list, String key, String quantity) {

        // Find existing entry
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            String entry = list.get(i);
            if (entry.startsWith(key + ": ")) {
                // Update existing entry
                int count = Integer.parseInt(entry.split(": ")[1]);
                count += Integer.parseInt(quantity);
                list.set(i, key + ": " + count);
                found = true;
                break;
            }
        }
        // If the entry doesn't exist, add a new one
        if (!found) {
            list.add(key + ": " + quantity);
        }
    }


    private static void sortList(ArrayList<String> list, int rank) { //Sorts seller dashboard based on what the
        //user wants to sort
        Comparator<String> comparator;
        comparator = Comparator.comparingInt(s -> Integer.parseInt(s.split(": ")[1]));

        if (rank == 1) { //Sort from high to low if rank is 1
            Collections.sort(list, comparator.reversed());
        } else { //Sorts from low to high if rank is 2
            Collections.sort(list, comparator);
        }
    }


}
