import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * ProductSort
 * <p>
 * Sorts the products based on price or quantity for the customer
 *
 * @author Lionel, Lab #L08
 * @version 12/02/2023
 * <p>
 */

public class ProductSort {

    //TODO: Create sortByIncreasingPriceClient method
    //Sorts all products by min to max price
    public static ArrayList<String> sortByIncreasingPriceServer(Object LOCK) {
        ArrayList<String> allProducts = new ArrayList<>();
        try {
        synchronized (LOCK) {
            //creates arraylist of all products
            allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Products.txt"));
        }
            // sorts all products by min to max with selection sort
            for (int i = 0; i < allProducts.size() - 1; i++) {
                int min = i;
                for(int j = i + 1; j < allProducts.size(); j++) {
                    String[] productMin = allProducts.get(min).split(",");
                    double minPrice  = Double.parseDouble(productMin[4]);

                    String[] productCurrent = allProducts.get(j).split(",");
                    double currentPrice = Double.parseDouble(productCurrent[4]);

                    if (currentPrice < minPrice) {
                        min = j;
                    }
                }
                if (min != i) {
                    String temp = allProducts.get(i);
                    allProducts.set(i, allProducts.get(min));
                    allProducts.set(min, temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allProducts; //returns product arraylist
    }

    //TODO: Create sortByDecreasingPriceClient method
    //sorts all products by max to min price
    public static ArrayList<String> sortByDecreasingPriceServer(Object LOCK) {
        ArrayList<String> allProducts = new ArrayList<>();
        try {
            synchronized (LOCK) {
                //creates arraylist of all products
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Products.txt"));
            }
            //sorts all products by max to min price by selection sort
            for (int i = 0; i < allProducts.size() - 1; i++) {
                int max = i;
                for(int j = i + 1; j < allProducts.size(); j++) {
                    String[] productMax = allProducts.get(max).split(",");
                    double maxPrice  = Double.parseDouble(productMax[4]);

                    String[] productCurrent = allProducts.get(j).split(",");
                    double currentPrice = Double.parseDouble(productCurrent[4]);

                    if (currentPrice > maxPrice) {
                        max = j;
                    }
                }
                if (max != i) {
                    String temp = allProducts.get(i);
                    allProducts.set(i, allProducts.get(max));
                    allProducts.set(max, temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allProducts; //returns product arraylist
    }

    //TODO: Create sortByIncreasingQuantityClient method
    //sorts all products by min to max quantity
    public static ArrayList<String> sortByIncreasingQuantityServer(Object LOCK) {
        ArrayList<String> allProducts = new ArrayList<>();
        try {
            synchronized (LOCK) {
                //creates arraylist of all products
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Products.txt"));
            }
            //sorts all products by min to max quantity by selection sort
            for (int i = 0; i < allProducts.size() - 1; i++) {
                int min = i;
                for(int j = i + 1; j < allProducts.size(); j++) {
                    String[] productMin = allProducts.get(min).split(",");
                    double minQuantity  = Double.parseDouble(productMin[5]);

                    String[] productCurrent = allProducts.get(j).split(",");
                    double currentQuantity = Double.parseDouble(productCurrent[5]);

                    if (currentQuantity < minQuantity) {
                        min = j;
                    }
                }
                if (min != i) {
                    String temp = allProducts.get(i);
                    allProducts.set(i, allProducts.get(min));
                    allProducts.set(min, temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allProducts; //returns product arraylist
    }

    //TODO: Create sortByDecreasingQuantityClient method
    //sorts all products by max to min quantity
    public static ArrayList<String> sortByDecreasingQuantityServer(Object LOCK) {
        ArrayList<String> allProducts = new ArrayList<>();
        try {
            synchronized (LOCK) {
                //creates arraylist of all products
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Products.txt"));
            }
            //sorts all products by max to min quantity by selection sort
            for (int i = 0; i < allProducts.size() - 1; i++) {
                int max = i;
                for(int j = i + 1; j < allProducts.size(); j++) {
                    String[] productMax = allProducts.get(max).split(",");
                    double maxQuantity = Double.parseDouble(productMax[5]);

                    String[] productCurrent = allProducts.get(j).split(",");
                    double currentQuantity = Double.parseDouble(productCurrent[5]);

                    if (currentQuantity > maxQuantity) {
                        max = j;
                    }
                }
                if (max != i) {
                    String temp = allProducts.get(i);
                    allProducts.set(i, allProducts.get(max));
                    allProducts.set(max, temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allProducts; //returns product arraylist
    }

}


