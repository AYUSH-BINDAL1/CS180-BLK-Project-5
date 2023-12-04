import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

public class GUI extends JFrame {

    public GUI() {
        //loginPage();
        addProduct();
    }

    // General Login Page
    public void loginPage() {
        setTitle("MarketPlace Login");
        setSize(600, 325);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JComboBox<String> userType = new JComboBox<>(new String[]{"Customer", "Seller"});
        userType.setVisible(true);

        JComboBox<String> createOrLogin = new JComboBox<>(new String[]{"Login", "Create Account"});
        createOrLogin.setVisible(true);

        JLabel usernameLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel info = new JLabel("<html> <br/> Welcome to the MarketPlace!<br/> <br/>" +
                "Select if you're a Customer or Seller!<br/> <br/>" +
                "Then Select if you want to Login or Create an Account!<br/> <br/>" +
                "Then enter your Email and Password!<br/> </html>");
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        info.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField email = new JTextField(15);
        JTextField password = new JTextField(15);

        JButton loginButton = new JButton("Login/Create Account");
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userTypeSelected = (String) userType.getSelectedItem();
                String createOrLoginSelected = (String) createOrLogin.getSelectedItem();
                String emailEntered = email.getText();
                String passwordEntered = password.getText();

                if (!AccountManager.validateEmail(emailEntered) || emailEntered.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid Email"
                            , "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (passwordEntered.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid Password"
                            , "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // TODO: implement logic
                 /*
                if (userTypeSelected.equals("Customer")) {
                    CustomerPage();
                } else if (userTypeSelected.equals("Seller")) {
                    SellerPage();
                } else {
                    System.out.println("Invalid user type");
                }
                */
            }
        });

        top.add(userType);
        top.add(createOrLogin);

        middle.add(usernameLabel);
        middle.add(email);
        middle.add(passwordLabel);
        middle.add(password);
        middle.add(info);

        bottom.add(loginButton);

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    // Customer Page
    public void CustomerPage() {
        // Remove content from the previous pane
        setup("Customer Page", 800, 500);

        JPanel top = new JPanel(new GridLayout(4, 2, 5, 5));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton shoppingCart = new JButton("Shopping Cart");
        shoppingCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerShoppingCart();
            }
        });
        JButton editAccount = new JButton("Edit Account");
        editAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editAccount();
            }
        });
        JButton deleteAccount = new JButton("Delete Account");
        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });
        JComboBox<String> sortBy = new JComboBox<>(new String[]{"Sort By Store", "Sort By Seller"});
        JComboBox<String> searchBy = new JComboBox<>(new String[]{"Search By Store", "Search by Seller"});
        JButton viewPurchaseHistory = new JButton("View/Export Purchase History");
        viewPurchaseHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPurchaseHistory();
            }
        });
        JButton viewStatistics = new JButton("View store/seller Statistics");

        // TODO: implement method to print the products in Marketplace with a view more button that corresponds to each product

        top.add(exit);
        top.add(shoppingCart);
        top.add(editAccount);
        top.add(deleteAccount);
        top.add(sortBy);
        top.add(searchBy);
        top.add(viewPurchaseHistory);
        top.add(viewStatistics);

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
    }

    public void SellerPage() {
        setup("Seller MarketPlace", 800, 500);

        JPanel top = new JPanel(new GridLayout(4, 2, 5, 5));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton shoppingCart = new JButton("View Shopping Cart");
        JButton editAccount = new JButton("Edit Account");
        editAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editAccount();
            }
        });

        JButton deleteAccount = new JButton("Delete Account");

        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });
        JButton addProduct = new JButton("Add Product");
        JButton viewSales = new JButton("View Sales");
        JButton viewStatistics = new JButton("View Seller Statistics");
        JButton csv = new JButton("Export/Import CSV");

        // TODO: implement method to print the the seller's products

        top.add(exit);
        top.add(shoppingCart);
        top.add(editAccount);
        top.add(deleteAccount);
        top.add(addProduct);
        top.add(viewSales);
        top.add(viewStatistics);
        top.add(csv);

        add(top, BorderLayout.NORTH);

    }

    public void editAccount() {
        setup("Edit Account", 450, 450);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JComboBox<String> userChangeType = new JComboBox<>(new String[]{"Change Username", "Change Password"});


        JLabel currentLabel = new JLabel("Enter old username/password:");
        JLabel newLabel = new JLabel("Enter new username/password:");
        JTextField currentCredential = new JTextField(15);
        JTextField newCredential = new JTextField(15);

        JLabel info = new JLabel("<html> <br/> 1. Please Select whether to Change Password or Change Username <br/> <br/>" +
                "2. Enter your current username/password<br/> <br/>" +
                "3. Enter your new username/password<br/> <br/>" +
                "4. Click the button to change<br/> <br/>" +
                "**A confirmation message should appear if done correctly**</html>");

        JButton change = new JButton("Change Username/Password");
        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: add functionality
                /*
                String userChangeTypeSelected = (String) userChangeType.getSelectedItem();
                String currentCredentialEntered = currentCredential.getText();
                String newCredentialEntered = newCredential.getText();

                if (userChangeTypeSelected.equals("Change Password")) {

                } else if (userChangeTypeSelected.equals("Change Username")) {

                } else {
                    System.out.println("UserChangeType Error");
                }
                */
            }
        });


        top.add(userChangeType);

        middle.add(currentLabel);
        middle.add(currentCredential);
        middle.add(newLabel);
        middle.add(newCredential);
        middle.add(info);

        bottom.add(change);

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void deleteAccount() {
        setup("Delete Account", 300, 350);

        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel usernameLabel = new JLabel("Enter username:");
        JLabel passwordLabel = new JLabel("Enter password:");
        JTextField usernameCredential = new JTextField(15);
        JTextField passwordCredential = new JTextField(15);
        JLabel info = new JLabel("<html> <br/> <br/>**Disclaimer** This action is irreversible!<br/> <br/> </html>");

        JButton delete = new JButton("Delete Account");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement delete account functionality, makes sure it deletes account and returns to home page
            }
        });
        JButton returnToMain = new JButton("Return to Main Menu");
        returnToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: implement functionality to return to home page depending on if seller or customer
            }
        });

        middle.add(usernameLabel);
        middle.add(usernameCredential);
        middle.add(passwordLabel);
        middle.add(passwordCredential);
        middle.add(info);

        bottom.add(delete);
        bottom.add(returnToMain);

        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void viewPurchaseHistory() {
        setup("View Purchase History", 400, 450);

        JPanel top = new JPanel(new GridLayout(2, 2, 6, 6));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton returnToMain = new JButton("Return to Main Menu");
        returnToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement functionality to return to home page depending on if seller or customer
            }
        });

        JLabel info = new JLabel("<html> <br/> <br/> Export History file as: <br/> <br/> </html>");
        JTextField fileName = new JTextField(15);
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement functionality to export purchase history to a file
            }
        });

        //TODO: implement functionality to import customer purchase history and print to screen


        top.add(exit);
        top.add(returnToMain);

        middle.add(info);
        middle.add(fileName);
        middle.add(exportButton);


        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void customerShoppingCart() {
        setup("Customer Shopping Cart", 400, 400);

        JPanel top = new JPanel(new GridLayout(2, 2, 6, 6));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton returnToMain = new JButton("Return to Main Menu");
        returnToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement functionality to return to home page depending on if seller or customer
            }
        });

        JButton buyAllButton = new JButton("Buy All");
        buyAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement functionality to buy all items in shopping cart and return to home page
            }
        });
        JButton removeAllButton = new JButton("Remove All");
        removeAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement functionality to remove all items in shopping cart
            }
        });

        // TODO: implement functionality to print the shopping cart to the screen

        top.add(exit);
        top.add(returnToMain);
        middle.add(buyAllButton);
        middle.add(removeAllButton);

    }

    public void addProduct() {
        setup("Add Product", 350, 400);

        JPanel top = new JPanel(new GridLayout(6, 2, 7, 7));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel productNameLabel = new JLabel("Enter Product Name:");
        JTextField productName = new JTextField(15);
        JLabel productPriceLabel = new JLabel("Enter Product Price:");
        JTextField productPrice = new JTextField(15);
        JLabel productQuantityLabel = new JLabel("Enter Product Quantity:");
        JTextField productQuantity = new JTextField(15);
        JLabel productStoreLabel = new JLabel("Product Store:");
        JTextField productStore = new JTextField(15);
        JLabel productDescription = new JLabel("Enter Product Description:");
        JTextField productDescriptionField = new JTextField(15);

        JButton addProduct = new JButton("Add Product");
        addProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: add functionality to add product to database and return to home page
            }
        });

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        top.add(productNameLabel);
        top.add(productName);
        top.add(productPriceLabel);
        top.add(productPrice);
        top.add(productQuantityLabel);
        top.add(productQuantity);
        top.add(productStoreLabel);
        top.add(productStore);
        top.add(productDescription);
        top.add(productDescriptionField);

        bottom.add(addProduct);
        bottom.add(exit);

        add(top, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setup(String description, int width, int height) {
        getContentPane().removeAll();
        revalidate();
        repaint();

        setTitle(description);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}
