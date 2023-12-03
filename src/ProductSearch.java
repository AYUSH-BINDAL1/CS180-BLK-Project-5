import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * ProductSearch
 * <p>
 * Handles all searching for products for customers
 *
 * @author Lionel Loo, Lab #L08
 * @version 12/02/2023
 * <p>
 */

public class ProductSearch {
    //TODO: Create searchByNameClient method

    //Given searched product name, return ArrayList of products that contain that searched term
    public static ArrayList<String> searchByNameServer(String productName, Object LOCK) {
        ArrayList<String> allProducts;
        ArrayList<String> searchedproducts = new ArrayList<>();
        productName = productName.toLowerCase(); //makes product name lowercase;
        try {
            synchronized (LOCK) {
                //Put files from product.txt into an arraylist, all products
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Products.txt"));
            }
            for (int i = 0; i < allProducts.size(); i++) { //runs through all products
                String[] currentProduct = allProducts.get(i).split(","); //splits current product by comma
                currentProduct[0] = currentProduct[0].toLowerCase(); //makes product name lowercase
                if (currentProduct[0].contains(productName)) {
                    searchedproducts.add(allProducts.get(i)); //adds current product
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchedproducts; //return searched products
    }

    //TODO: Create searchByStoreClient method
    //Given searched product store, return ArrayList of products that contain that searched term
    public static ArrayList<String> searchByStoreServer(String storeName, Object LOCK) {
        ArrayList<String> allProducts;
        ArrayList<String> searchedProducts = new ArrayList<>();
        storeName = storeName.toLowerCase(); //makes product name lowercase;
        try {
            synchronized (LOCK) {
                //Put files from product.txt into the arraylist
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Products.txt"));
            }
            for (int i = 0; i < allProducts.size(); i++) { //runs through all products
                String[] currentProduct = allProducts.get(i).split(","); //splits current product by comma
                currentProduct[2] = currentProduct[2].toLowerCase(); //makes product name lowercase
                if (currentProduct[2].contains(storeName)) {
                    searchedProducts.add(allProducts.get(i)); //adds current product
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchedProducts;  //returns searched products
    }


    //TODO: Create searchByDescriptionClient method

    //Given searched product description, return ArrayList of products that contain that searched term
    public static ArrayList<String> searchByDescriptionServer(String descriptionSearched, Object LOCK) {
        ArrayList<String> allProducts;
        ArrayList<String> searchedProducts = new ArrayList<>();
        descriptionSearched = descriptionSearched.toLowerCase(); //makes product name lowercase;
        try {
            synchronized (LOCK) {
                //Put files from product.txt into the arraylist
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Products.txt"));
            }
            for (int i = 0; i < allProducts.size(); i++) {
                String[] currentProduct = allProducts.get(i).split(","); //splits current products by comma
                currentProduct[1] = currentProduct[1].toLowerCase(); //makes product name lowercase
                if (currentProduct[1].contains(descriptionSearched)) {
                    searchedProducts.add(allProducts.get(i)); //adds current product
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchedProducts; //return searched products
    }


}
