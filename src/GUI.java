import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.net.Socket;
import java.io.*;

public class GUI extends JFrame implements Runnable {
    private Socket socket;
    private String email;
    private String password;
    private String userType;
    private ObjectInputStream reader;
    private BufferedWriter writer;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public GUI(Socket socket) {
        this.socket = socket;
        this.email = "";
        this.password = "";
        this.userType = "";
        try {
            reader = new ObjectInputStream(socket.getInputStream());
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        loginPage(); //General Login Page
    }


    public Object communicateWithServer(String message) {
        System.out.println(message);
        Object serverResponse = null;
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
            serverResponse = reader.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(serverResponse);
        return serverResponse;
    }

    public void loginPage() {
        setTitle("MarketPlace Login");
        setSize(600, 325);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JComboBox<String> userType = new JComboBox<>(new String[]{"CUSTOMER", "SELLER"});
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
        JPasswordField password = new JPasswordField(15);

        JButton loginButton = new JButton("Login/Create Account");
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageToServer = "";
                String userTypeSelected = (String) userType.getSelectedItem();
                String createOrLoginSelected = (String) createOrLogin.getSelectedItem();
                String emailEntered = email.getText();
                String passwordEntered = String.valueOf(password.getPassword());

                // checks if it is a valid email or empty
                if (!AccountManager.validateEmail(emailEntered) || emailEntered.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Must Be A Valid Email!"
                            , "Invalid Email", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // checks if email is empty
                if (passwordEntered.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Password Cannot Be Empty"
                            , "Invalid Password", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // TODO: implement logic

                if (createOrLoginSelected.equals("Create Account")) {
                    messageToServer = String.format("REGISTER,%s,%s,%s", emailEntered, passwordEntered,
                            userTypeSelected);
                    String result = (String) communicateWithServer(messageToServer);
                    System.out.println(result);
                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "Your account has been created", "Account Creation " +
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        setEmail(emailEntered);
                        setPassword(passwordEntered);
                        setUserType(userTypeSelected);
                        returnHome();
                    } else if (result.equals("EMAIL ALREADY EXISTS")) {
                        JOptionPane.showMessageDialog(null, "Entered email is already taken.", "Email Taken",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else if (createOrLoginSelected.equals("Login")) {
                    messageToServer = String.format("LOGIN,%s,%s,%s", emailEntered, passwordEntered,
                            userTypeSelected);
                    String result = (String) communicateWithServer(messageToServer);
                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "You have been successfully logged in.",
                                "Login Success", JOptionPane.INFORMATION_MESSAGE);
                        setEmail(emailEntered);
                        setPassword(passwordEntered);
                        setUserType(userTypeSelected);
                        returnHome();
                    } else if (result.equals("INVALID EMAIL")) {
                        JOptionPane.showMessageDialog(null, "This email doesn't exist.", "Unknown Email",
                                JOptionPane.ERROR_MESSAGE);
                    } else if (result.equals("INCORRECT PASSWORD")) {
                        JOptionPane.showMessageDialog(null, "Wrong Password For This Account","Invalid Password",
                                JOptionPane.ERROR_MESSAGE);
                    } else if (result.equals("INCORRECT USER TYPE")) {
                        JOptionPane.showMessageDialog(null, "Wrong User Type For This Account.", "Invalid User Type",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.out.println("Error, the Login/Create an account dropdown doesn't work");
                }
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
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

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
        JButton searchBy = new JButton("Search By");
        searchBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBy();
            }
        });
        JButton viewPurchaseHistory = new JButton("View/Export Purchase History");
        viewPurchaseHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPurchaseHistory();
            }
        });
        JButton viewStatistics = new JButton("View store/Customer Statistics");
        viewStatistics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStatistics();
            }
        });

        // TODO: implement method to print the products in Marketplace with a view more button that corresponds to each product

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement refresh button
            }
        });

        top.add(exit);
        top.add(shoppingCart);
        top.add(editAccount);
        top.add(deleteAccount);
        top.add(sortBy);
        top.add(searchBy);
        top.add(viewPurchaseHistory);
        top.add(viewStatistics);

        bottom.add(refresh);

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void SellerPage() {
        setup("Seller MarketPlace", 800, 500);

        JPanel top = new JPanel(new GridLayout(4, 2, 5, 5));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton shoppingCart = new JButton("View Shopping Cart");
        shoppingCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewShoppingCart();
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
        JButton addProduct = new JButton("Add Product");
        addProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        JButton viewSales = new JButton("View Sales");
        JButton viewStatistics = new JButton("View Seller Statistics");
        viewStatistics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStatistics();
            }
        });
        JButton csv = new JButton("Export/Import CSV");
        csv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                csv();
            }
        });


        // TODO: implement method to print the the seller's products

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement refresh button
            }
        });

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

    public void viewStatistics() {
        setup("View Statistics", 450, 450);

        JPanel top = new JPanel(new GridLayout(1, 2, 7, 7));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JComboBox<String> sortBy = new JComboBox<>(new String[]{"Sort By Store Descending ", "Sort By Store Ascending"
                , "Sort by Seller Descending", "Sort by Seller Ascending"});
        sortBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: add functionality to sort
            }
        });

        JButton mainMenu = new JButton("Main Menu");
        mainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnHome();
            }
        });

        top.add(sortBy);
        top.add(mainMenu);

        // TODO: implement method to print the statistics *note*
        //  statistics is general and used by both seller and customer

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
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
                String userChangeTypeSelected = (String) userChangeType.getSelectedItem();
                String currentCredentialEntered = currentCredential.getText();
                String newCredentialEntered = newCredential.getText();


                String messageToServer;

                if (userChangeTypeSelected.equals("Change Password")) {
                    if(!currentCredentialEntered.equals(getPassword())) {
                        JOptionPane.showMessageDialog(null, "Old Password Does Not Match",
                                "Password Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    messageToServer = String.format("EDIT PASSWORD,%s,%s,%s", getEmail(), getPassword(), newCredentialEntered);
                    String result = (String) communicateWithServer(messageToServer);
                    if (result.equals("PASSWORD UPDATED")) {
                        JOptionPane.showMessageDialog(null, "Your Password Has Been Changed",
                                "Password Change Success", JOptionPane.INFORMATION_MESSAGE);
                        setPassword(newCredentialEntered);
                        returnHome();
                    } else if (result.equals("INVALID PASSWORD")) {
                        JOptionPane.showMessageDialog(null, "Old Password Does Not Match",
                                "Password Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (userChangeTypeSelected.equals("Change Username")) {
                    if (!currentCredentialEntered.equals(getEmail())) {
                        JOptionPane.showMessageDialog(null, "Old Username Does Not Match",
                                "Username Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    messageToServer = String.format("EDIT USERNAME,%s,%s", getEmail(), newCredentialEntered);
                    String result = (String) communicateWithServer(messageToServer);
                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "Username changed successfully",
                                "Username Change Success", JOptionPane.INFORMATION_MESSAGE);
                        setEmail(newCredentialEntered);
                        returnHome();
                    } else if (result.equals("EMAIL ALREADY TAKEN")) {
                        JOptionPane.showMessageDialog(null, "New Email entered is already taken",
                                "Username Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.out.println("UserChangeType Error");
                }

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
                String usernameCredentialEntered = usernameCredential.getText();
                String passwordCredentialEntered = passwordCredential.getText();
                System.out.println("EMAIL ONE"+getEmail());
                System.out.println("EMAIL TWO"+usernameCredentialEntered);
                if (!usernameCredentialEntered.equals(getEmail())) {
                    JOptionPane.showMessageDialog(null, "Username entered is incorrect",
                            "Incorrect username/email entered", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println(getPassword());
                if (!passwordCredentialEntered.equals(getPassword())) {
                    JOptionPane.showMessageDialog(null, "Password entered is incorrect",
                            "Incorrect password entered", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String messageToServer = String.format("DELETE ACCOUNT,%s,%s", usernameCredentialEntered, passwordCredentialEntered);
                String result = (String) communicateWithServer(messageToServer);
                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, "Account deleted successfully",
                            "Account Deletion Success", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Password Entered",
                            "Password Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton returnToMain = new JButton("Return to Main Menu");
        returnToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnHome();
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
                returnHome();
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
                CustomerPage();
            }
        });

        JButton buyAllButton = new JButton("Buy All");
        buyAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageToServer = String.format("CHECKOUT CART,%s", getEmail());
                String result = (String) communicateWithServer(messageToServer);
                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, "Purchase successful",
                            "Purchase Success", JOptionPane.INFORMATION_MESSAGE);
                    returnHome();
                } else if (result.equals("EMPTY CART")) {
                    JOptionPane.showMessageDialog(null, "Cart is empty",
                            "Empty Cart", JOptionPane.ERROR_MESSAGE);
                }
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

    public void searchBy() {
        setup("Search by - Products", 350, 400);

        JPanel top = new JPanel(new GridLayout(2, 1, 6, 6));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JTextField search = new JTextField(15);
        JButton searchButton = new JButton("Search");

        top.add(search);
        top.add(searchButton);

        add(top, BorderLayout.NORTH);
    }

    public void addProduct() {
        setup("Create Product", 350, 400);

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
                try {
                    double price = Double.parseDouble(productPrice.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Invalid price entered",
                            "Price ERROR", JOptionPane.ERROR_MESSAGE);
                }

                try {
                    int quantity = Integer.parseInt(productQuantity.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Invalid quantity entered",
                            "Quantity ERROR", JOptionPane.ERROR_MESSAGE);
                }

                String messageToServer = String.format("CREATE NEW PRODUCT,%s,%s,%s,%s,%s", productName,
                        productDescription, productStore, productPrice, productQuantity);
                String result = (String) communicateWithServer(messageToServer);
                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, "Product added successfully",
                            "Product Creation Success", JOptionPane.INFORMATION_MESSAGE);
                    returnHome();
                } else if (result.equals("PRODUCT ALREADY EXISTS")) {
                    JOptionPane.showMessageDialog(null, "Product already exists",
                            "Product Already Exists", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton exit = new JButton("Back Button");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerPage();
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


    public void csv() {
        setup("CSV", 400, 400);

        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new GridLayout(1, 2, 6, 6));

        JComboBox<String> csvType = new JComboBox<>(new String[]{"Import", "Export"});
        JLabel csvText = new JLabel("<html> <br/> <br/> Please enter the file you want to import or export <br/> <br/> </html>");
        JTextField fileName = new JTextField(15);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerPage();
            }
        });
        JButton csvButton = new JButton("Import/ Export CSV");
        csvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement functionality to import or export csv file depending on user selection
            }
        });

        middle.add(csvType);
        middle.add(csvText);
        middle.add(fileName);

        bottom.add(backButton);
        bottom.add(csvButton);

        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void modifyProduct(String product) {
        setup("Modify Product", 400, 600);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel productModified = new JLabel("Product Modified: " + product);

        JLabel productNameLabel = new JLabel("Enter New Product Name:");
        JTextField productName = new JTextField(15);
        JLabel productPriceLabel = new JLabel("Enter New Product Price:");
        JTextField productPrice = new JTextField(15);
        JLabel productQuantityLabel = new JLabel("Enter New Product Quantity:");
        JTextField productQuantity = new JTextField(15);
        JLabel productStoreLabel = new JLabel("Enter New Product Store:");
        JTextField productStore = new JTextField(15);
        JLabel productDescription = new JLabel("Enter New Product Description:");
        JTextField productDescriptionField = new JTextField(15);

        JButton modifyProduct = new JButton("Modify Product");
        modifyProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: add functionality to modify product and return to home page
                try {
                    double price = Double.parseDouble(productPrice.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Invalid price entered",
                            "Price ERROR", JOptionPane.ERROR_MESSAGE);
                }

                try {
                    int quantity = Integer.parseInt(productQuantity.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Invalid quantity entered",
                            "Quantity ERROR", JOptionPane.ERROR_MESSAGE);
                }

                String messageToServer = String.format("MODIFY PRODUCT,%s,%s,%s,%s,%s,%s", productName,
                        productDescription, productStore, productPrice, productQuantity,product);
                String result = (String) communicateWithServer(messageToServer);
                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, "Product modified successfully",
                            "SUCCESSFUL MODIFICATION", JOptionPane.INFORMATION_MESSAGE);
                    returnHome();
                }
            }
        });

        JButton exit = new JButton("Back to Main Menu");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerPage();
            }
        });

        top.add(productModified);

        middle.add(productNameLabel);
        middle.add(productName);
        middle.add(productPriceLabel);
        middle.add(productPrice);
        middle.add(productQuantityLabel);
        middle.add(productQuantity);
        middle.add(productStoreLabel);
        middle.add(productStore);
        middle.add(productDescription);
        middle.add(productDescriptionField);

        bottom.add(exit);
        bottom.add(modifyProduct);

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

    }

    public void viewShoppingCart() {
        setup("View Customers Shopping Cart", 400, 400);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel info = new JLabel("<html> <br/> <br/> Shopping Cart <br/> <br/> </html>");

        top.add(info);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerPage();
            }
        });

        bottom.add(backButton);


        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void viewSales() {
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel info = new JLabel("<html> <br/> <br/> Sales <br/> <br/> </html>");

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerPage();
            }
        });

        // TODO: add fucntionality to print sales
        middle.add(info);

        add(middle, BorderLayout.CENTER);
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

    public void returnHome() {
        if (userType.equals("CUSTOMER")) {
            CustomerPage();
        } else if (userType.equals("SELLER")) {
            SellerPage();
        }
    }

}
