import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

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
        loginPage();
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
                        JOptionPane.showMessageDialog(null, "Wrong Password For This Account", "Invalid Password",
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
        setup("Customer Page", 800, 700);

        JPanel top = new JPanel(new GridLayout(4, 2, 5, 5));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                communicateWithServer("EXIT");
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
        JComboBox<String> sortBy = new JComboBox<>(new String[]{"Sort By Increasing Price", "Sort By Decreasing Price",
                "Sort By Increasing Quantity", "Sort By Decreasing Quantity"});
        sortBy.setVisible(true);
        sortBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) sortBy.getSelectedItem();
                String messageToServer ;
                switch(selectedItem) {
                    case "Sort By Increasing Price" ->
                        messageToServer = "SORT INCREASING PRICE";
                    case "Sort By Decreasing Price" ->
                        messageToServer = "SORT DECREASING PRICE";
                    case "Sort By Increasing Quantity" ->
                        messageToServer = "SORT INCREASING QUANTITY";
                    case "Sort By Decreasing Quantity" ->
                        messageToServer = "SORT DECREASING QUANTITY";
                    default ->
                        messageToServer = "";
                }

                if (messageToServer.isEmpty()) {
                    messageToServer = "GET PRODUCTS";
                }
                middle.removeAll();
                middle.add(new JLabel("<html> <br/> Product  |   Store   |   Price  |  View Product <br/> </html>"));
                middle.add(customerProducts(messageToServer));
                middle.revalidate();
                middle.repaint();
            }
        });
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
                viewCustomerStatistics();
            }
        });


        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                middle.removeAll();
                middle.add(new JLabel("<html> <br/> Product  |   Store   |   Price  |  View Product <br/> </html>"));
                middle.add(customerProducts("GET ALL PRODUCTS"));
                middle.revalidate();
                middle.repaint();
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

        middle.add(new JLabel("<html> <br/> Product  |   Store   |   Price  |  View Product <br/> </html>"));
        middle.add(customerProducts("GET ALL PRODUCTS"));

        bottom.add(refresh);

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void SellerPage() {
        setup("Seller MarketPlace", 800, 700);

        JPanel top = new JPanel(new GridLayout(4, 2, 5, 5));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                communicateWithServer("EXIT");
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
        JButton viewSales = new JButton("View Sales By Store");
        viewSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSales();
            }
        });
        JButton viewStatistics = new JButton("View Seller Statistics");
        viewStatistics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSellerStatistics();
            }
        });
        JButton csv = new JButton("Export/Import CSV");
        csv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                csv();
            }
        });


        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                middle.removeAll();
                middle.add(new JLabel("<html> <br/> Product  |   Store   |   Price  |  Modify Product  " +
                        "|  Delete Product <br/> </html>"));
                middle.add(sellerProducts());
                middle.repaint();
                middle.revalidate();
                middle.repaint();
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


        middle.add(new JLabel("<html> <br/> Store  |   Product   |   Price  |  Modify Product  " +
                "|  Delete Product <br/> </html>"));
        middle.add(sellerProducts());


        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
    }

    public void viewCustomerStatistics() {
        setup("View Statistics", 450, 450);

        JPanel top = new JPanel(new GridLayout(1, 2, 7, 7));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton mainMenu = new JButton("Main Menu");
        mainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnHome();
            }
        });

        JComboBox<String> sortBy = new JComboBox<>(new String[]{"View General Statistics High To Low",
                "View General Statistics Low To High", "Sort By High", "Sort By Low",
                });
        sortBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sort = (String) sortBy.getSelectedItem();
                String messageToServer;
                switch (sort.toUpperCase()) {
                    case "SORT BY HIGH" ->
                        messageToServer = "VIEW CUSTOMER STATISTICS SORT," + getEmail() + ",HIGH TO LOW";
                    case "SORT BY LOW" ->
                        messageToServer = "VIEW CUSTOMER STATISTICS SORT," + getEmail() + ",LOW TO HIGH";
                    case "VIEW GENERAL STATISTICS LOW TO HIGH" ->
                        messageToServer = "VIEW CUSTOMER STATISTICS,LOW TO HIGH";
                    default ->
                        messageToServer = "VIEW CUSTOMER STATISTICS,HIGH TO LOW";
                }
                ArrayList<String> statistics = new ArrayList<>();
                try {
                    statistics = (ArrayList) communicateWithServer(messageToServer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "SERVER ERROR", "Error reading file",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println(statistics);

                JPanel stats = new JPanel(new FlowLayout(FlowLayout.CENTER));
                if (statistics.isEmpty()) {
                    stats = error("no Statistics");
                } else {
                    stats = printStatistics(statistics, 200, 300);
                }

                middle.removeAll();
                middle.add(new JLabel("<html> <br/> Store  |   Product   |" +
                        "   Price <br/> </html>"));
                middle.add(stats);
                middle.revalidate();
                middle.repaint();
            }
        });



        top.add(sortBy);
        top.add(mainMenu);


        middle.add(new JLabel("<html> <br/> Store  |   Product   |" +
                "   Price <br/> </html>"));

        middle.add(printStatistics((ArrayList<String>)communicateWithServer("VIEW CUSTOMER STATISTICS," + getEmail()), 200, 300));

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
    }

    // TODO: finish this method
    public void viewSellerStatistics() {
        setup("View Statistics", 450, 450);

        JPanel top = new JPanel(new GridLayout(1, 2, 7, 7));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JComboBox<String> sortBy = new JComboBox<>(new String[]{"Sort By High", "Sort By low"});
        sortBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sort = (String) sortBy.getSelectedItem();
                String messageToServer;
                switch (sort) {
                    // TODO: PRINT SELLER STATS
                }
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
                String userChangeTypeSelected = (String) userChangeType.getSelectedItem();
                String currentCredentialEntered = currentCredential.getText();
                String newCredentialEntered = newCredential.getText();


                String messageToServer;

                if (userChangeTypeSelected.equals("Change Password")) {
                    if (!currentCredentialEntered.equals(getPassword())) {
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
                    } else if (result.equals("INVALID EMAIL FORMAT")) {
                        JOptionPane.showMessageDialog(null, "New email is in an invalid format",
                                "Username Error", JOptionPane.ERROR_MESSAGE);
                    } else if (result.equals("EMAILS ARE THE SAME")) {
                        JOptionPane.showMessageDialog(null, "New email is same as old email",
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
                System.out.println("EMAIL ONE" + getEmail());
                System.out.println("EMAIL TWO" + usernameCredentialEntered);
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
                    communicateWithServer("EXIT");
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
        setup("View Purchase History", 600, 650);


        JPanel top = new JPanel(new GridLayout(2, 2, 6, 6));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                communicateWithServer("EXIT");
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
                String fileNameInput = fileName.getText();

                if (!fileNameInput.endsWith(".txt") || !fileNameInput.matches(".*\\.txt$")) {
                    JOptionPane.showMessageDialog(null, "Please make sure you have a .txt file", "Export " +
                            "Failure", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String messageToServer = String.format("EXPORT PURCHASE HISTORY,%s,%s", getEmail(), fileNameInput);
                String result = (String) communicateWithServer(messageToServer);
                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, "Purchase history exported successfully",
                            "Purchase History Export Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        top.add(exit);
        top.add(returnToMain);

        middle.add(info);
        middle.add(fileName);
        middle.add(exportButton);

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);

        ArrayList<String> purchaseHistory = (ArrayList<String>) communicateWithServer(String.format("VIEW PURCHASE HISTORY,%s", getEmail()));
        System.out.println(purchaseHistory);
        if (purchaseHistory.isEmpty()) {
            add(error("No Products"), BorderLayout.SOUTH);
            return;
        }

        bottom.add(printStatistics(purchaseHistory, 200, 200));

        add(bottom, BorderLayout.SOUTH);
    }

    public void customerShoppingCart() {
        setup("Customer Shopping Cart", 800, 400);

        JPanel top = new JPanel(new GridLayout(2, 2, 6, 6));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                communicateWithServer("EXIT");
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
                boolean success = false;
                String messageToServer = String.format("CHECKOUT CART,%s", getEmail());
                ArrayList<String> result = (ArrayList<String>) communicateWithServer(messageToServer);
                if (result.size() == 0) {
                    JOptionPane.showMessageDialog(null, "Cart is empty",
                            "Empty Cart", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for (int i = 0; i < result.size(); i++) {
                    String[] currentProductSplit = result.get(i).split(","); //spot 0 = product name 1 = result
                    if (currentProductSplit[1].equals("NOT ENOUGH QUANTITY") ||  currentProductSplit[1].equals(
                            "PRODUCT NOT FOUND")){
                        JOptionPane.showMessageDialog(null, "Product \"" + currentProductSplit[0]
                                + "\" does not have enough quantity.\nPlease put the item into your cart again with " +
                                        "an avaiable quantity",
                                "Cart Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                    JOptionPane.showMessageDialog(null, "Your cart has been empytied!" +
                                    "\nAvaiable Items have been purchased.",
                            "Cart purchased", JOptionPane.INFORMATION_MESSAGE);
                    returnHome();

                }

        });


        top.add(exit);
        top.add(returnToMain);
        middle.add(buyAllButton);
        bottom.add(viewCustomerShoppingCart());

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void searchBy() {
        setup("Search by - Products", 450, 450);

        JPanel top = new JPanel(new GridLayout(2, 2, 6, 6));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JTextField search = new JTextField(15);
        JButton searchButton = new JButton("Search");

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerPage();
            }
        });

        JLabel searchByLabel = new JLabel("Search by: ");
        JComboBox<String> searchBy = new JComboBox<>(new String[]{"Name",
                "Description", "Store"});

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchInput = search.getText();
                if (searchInput.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a search term",
                            "No Search Term", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String searchWith = ((String) Objects.requireNonNull(searchBy.getSelectedItem())).toUpperCase();
                ArrayList<String> products;
                String messageToServer = String.format("SEARCH BY %s,%s", searchWith, searchInput);
                try {
                    products = (ArrayList<String>) communicateWithServer(messageToServer);
                } catch (Exception exception) {
                    System.out.println("Server Error");
                    JOptionPane.showMessageDialog(null, "Server Error",
                            "Server Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Server Error");
                    return;
                }

                if (products.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No products found",
                            "No Products Found", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                for (String product : products) {
                    System.out.println(product);
                }
                middle.removeAll();
                middle.add(new JLabel("<html> <br/> Name | Store  | Price <br/>  </html>"));
                middle.add(searchProductPanel(products));

                middle.revalidate();
                middle.repaint();
            }
        });

        top.add(searchByLabel);
        top.add(searchBy);
        top.add(search);
        top.add(searchButton);

        bottom.add(back);

        middle.add(new JTextArea("PLEASE SEARCH TO VIEW PRODUCTS"));

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
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
                    int quantity = Integer.parseInt(productQuantity.getText());
                    String messageToServer = String.format("CREATE NEW PRODUCT,%s,%s,%s,%s,%s,%s", productName.getText(),
                            productDescriptionField.getText(), productStore.getText(), getEmail(),
                            productPrice.getText(),
                            productQuantity.getText());
                    String result = (String) communicateWithServer(messageToServer);
                    if (result.equals("PRODUCT CREATED")) {
                        JOptionPane.showMessageDialog(null, "Product added successfully",
                                "Product Creation Success", JOptionPane.INFORMATION_MESSAGE);
                        returnHome();
                    } else if (result.equals("PRODUCT ALREADY EXISTS")) {
                        JOptionPane.showMessageDialog(null, "Product already exists",
                                "Product Already Exists", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException exception) {
                    if (!productPrice.getText().matches("^\\d*\\.?\\d+$")) {
                        JOptionPane.showMessageDialog(null, "Invalid price entered",
                                "Price ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid quantity entered",
                                "Quantity ERROR", JOptionPane.ERROR_MESSAGE);
                    }
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


    // fix formating
    public void csv() {
        setup("CSV", 400, 400);

        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new GridLayout(1, 2, 6, 6));

        JComboBox<String> csvType = new JComboBox<>(new String[]{"Import", "Export"});
        JLabel csvText = new JLabel("<html> <br/> <br/> Please enter the file you want" +
                " to import or export <br/> <br/> </html>");
        JLabel csvformat = new JLabel("<html> <br/> <br/> Imported Filed should have the format: Product Name," +
                "Product Description,Store Name,Seller Email,Price,Quantity <br/> " +
                "<br/> </html>");


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
                String fileNameInput = fileName.getText();
                String csvTypeInput = csvType.getSelectedItem().toString();
                String messageToServer;
                String result;
                if (!fileNameInput.endsWith(".csv") || !fileNameInput.matches(".*\\.csv$")) {
                    JOptionPane.showMessageDialog(null, "Please make sure your file is a .csv file", "Import/Export " +
                            "Failure", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((csvTypeInput.equals("Import"))) {
                    messageToServer = String.format("IMPORT SELLER CSV,%s", fileNameInput);
                    result = (String) communicateWithServer(messageToServer);
                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "Your file has been imported. \n Incorrectly Formatted " +
                                "linea have been omitted", "Export CSV" +
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        returnHome();
                    } else if (result.equals("PATH DOES NOT EXIST")) {
                        JOptionPane.showMessageDialog(null, "Your file path does not exist", "File Failure", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (csvTypeInput.equals("Export")) {
                    messageToServer = String.format("EXPORT SELLER CSV,%s,%s", fileNameInput, getEmail());
                    result = (String) communicateWithServer(messageToServer);
                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "Your file has been exported", "Export CSV" +
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        returnHome();
                    } else if (result.equals("PATH TAKEN")) {
                        JOptionPane.showMessageDialog(null, "Your file path is already taken. Try a different one",
                                "File Failure", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        middle.add(csvType);
        middle.add(csvText);
        middle.add(csvformat);
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
                        productDescription, productStore, productPrice, productQuantity, product);
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

    // TODO: fix formatting
    public void viewShoppingCart() {
        setup("View Customers Shopping Cart", 500, 500);

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

        String messageToServer = String.format("VIEW SELLER SHOPPING CART,%s", getEmail());

        ArrayList<String> shoppingCart = (ArrayList<String>) communicateWithServer(messageToServer);

        if (shoppingCart.isEmpty()) {
            middle.add(new JLabel("Your shopping cart is empty"));
        } else {
            middle.add(printStatistics(shoppingCart, 300, shoppingCart.size() * 100));
        }

        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void viewSales() {
        setup("View Sales By Store", 800, 700);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel info = new JLabel("<html> <br/> <br/> Sales By Store <br/> <br/> </html>");
        String messageToServer = "VIEW SALES BY STORE," + getEmail();
        String response = (String) communicateWithServer(messageToServer);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerPage();
            }
        });

        top.add(info);
        JTextArea display = new JTextArea ( 16, 58 );
        display.setText ( response );
        display.setEditable ( false ); // set textArea non-editable
        JScrollPane scroll = new JScrollPane ( display );
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        //Add Textarea in to middle panel
        middle.add (scroll);

        bottom.add(backButton);
        add(top, BorderLayout.NORTH);

        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public JPanel customerProducts(String messageToServer) {
        ArrayList<String> products;
        try {
            products = (ArrayList<String>) communicateWithServer(messageToServer);
        } catch (Exception e) {
            System.out.println("GET ALL PRODUCTS, arraylistError");
            return error("ERROR, SERVER ERROR sending products");
        }

        if (products.isEmpty()) {
            return error("No Products");
        }
        JPanel panel = new JPanel(new GridLayout(products.size(), 1, 4, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(600, products.size() * 100));
        for (String product : products) {
            JPanel component = new JPanel(new FlowLayout(FlowLayout.CENTER));
            String[] productInfo = product.split(",");
            try {
                component.add(new JLabel("   " + productInfo[0] + "   "));
                component.add(new JLabel("   " + productInfo[2] + "   "));
                component.add(new JLabel("   " + productInfo[4] + "   "));
            } catch (Exception e) {
                System.out.println("index out of bound exception");
                return error("ERROR, SERVER ERROR sending products");
            }

            JButton viewProduct = new JButton("View Product");
            viewProduct.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewProduct(product);
                }
            });


            component.add(viewProduct);
            panel.add(component);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(600, products.size() * 100));
        JPanel view = new JPanel();
        view.add(scrollPane);
        return view;
    }

    public JPanel sellerProducts() {
        String messageToServer = "VIEW SELLER PRODUCTS," + getEmail();
        ArrayList<String> products;
        try {
            products = (ArrayList<String>) communicateWithServer(messageToServer);
        } catch (Exception e) {
            System.out.println("GET SELLER PRODUCTS, arraylistError");
            return error("ERROR, Server Error");
        }

        if (products.isEmpty()) {
            return error("No Products");
        }

        JPanel panel = new JPanel(new GridLayout(products.size(), 1, 4, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(600, products.size() * 100));
        for (String product : products) {
            JPanel component = new JPanel(new FlowLayout(FlowLayout.CENTER));
            String[] productInfo = product.split(",");
            try {
                component.add(new JLabel("   " + productInfo[0] + "   "));
                component.add(new JLabel("   " + productInfo[2] + "   "));
                component.add(new JLabel("   " + productInfo[4] + "   "));
            } catch (Exception e) {
                System.out.println("index out of bound exception");
                return error("ERROR, SERVER ERROR sending products");
            }

            JButton modifyProduct = new JButton("ModifyProduct");
            modifyProduct.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    modifyProduct(product);
                }
            });

            JButton deleteProduct = new JButton("Delete Product");
            deleteProduct.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String messageToServer = String.format("DELETE PRODUCT,%s", product);
                    String result = (String) communicateWithServer(messageToServer);
                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "Product Deleted: " + productInfo[0],
                                "Product Deleted", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Product Error, Could not delete",
                                "Product Error", JOptionPane.ERROR_MESSAGE);
                    }
                    SellerPage();
                }
            });

            component.add(modifyProduct);
            component.add(deleteProduct);
            panel.add(component);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(600, products.size() * 100));
        JPanel view = new JPanel();
        view.add(scrollPane);
        return view;
    }

    public JPanel viewCustomerShoppingCart() {
        String messageToServer = "GET CUSTOMER CART," + getEmail(); // Assuming getEmail() retrieves the customer's email
        ArrayList<String> cart;
        try {
            cart = (ArrayList<String>) communicateWithServer(messageToServer);
            for (int i = 0; i < cart.size(); i++) {
                System.out.println(cart.get(i));
            }
        } catch (Exception e) {
            System.out.println("GET SHOPPING CART, arraylistError");
            return error("ERROR, SERVER ERROR retrieving shopping cart");
        }

        if (cart.isEmpty()) {
            return error("Shopping Cart is Empty");
        }

        JPanel panel = new JPanel(new GridLayout(cart.size(), 1, 4, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(600, cart.size() * 100));

        for (String cartItem : cart) {
            JPanel component = new JPanel(new FlowLayout(FlowLayout.CENTER));
            String[] cartItemInfo = cartItem.split(",");
            try {
                component.add(new JLabel("   " + cartItemInfo[0] + "   "));
                component.add(new JLabel("   " + cartItemInfo[2] + "   "));
                component.add(new JLabel("   " + cartItemInfo[4] + "   "));
            } catch (Exception e) {
                System.out.println("index out of bound exception");
                return error("ERROR, SERVER ERROR sending shopping cart");
            }

            JButton removeFromCartButton = new JButton("Remove from Cart");
            removeFromCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String messageToServer = String.format("REMOVE PRODUCT FROM CART,%s", cartItem);
                    String result = (String) communicateWithServer(messageToServer);
                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, "Product Removed: " + cartItemInfo[0],
                                "Product Removed", JOptionPane.INFORMATION_MESSAGE);
                        CustomerPage();
                    }
                }
            });

            component.add(removeFromCartButton);
            panel.add(component);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(600, cart.size() * 100));

        JPanel view = new JPanel();
        view.add(scrollPane);
        return view;
    }

    public JPanel searchProductPanel(ArrayList<String> products) {
        if (products.isEmpty()) {
            return error("No Products");
        }

        JPanel panel = new JPanel(new GridLayout(products.size(), 1, 4, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(600, 800));


        for (String product : products) {
            JPanel component = new JPanel(new FlowLayout(FlowLayout.CENTER));
            String[] productInfo = product.split(",");
            JButton viewProduct = new JButton("View Product");
            try {
                component.add(new JLabel("   " + productInfo[0] + "   "));
                component.add(new JLabel("   " + productInfo[2] + "   "));
                component.add(new JLabel("   " + productInfo[4] + "   "));
            } catch (Exception e) {
                System.out.println("index out of bound exception");
                return error("ERROR, SERVER ERROR sending products");
            }
            viewProduct.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewProduct(product);
                }
            });
            component.add(viewProduct);
            panel.add(component);
        }






        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(600, 500));

        JPanel view = new JPanel();
        view.add(scrollPane);
        return view;
    }


    public void viewProduct(String product) {
        boolean validFormat = false;
        String[] productWords;
        try {
            productWords = product.split(",");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException");
            JOptionPane.showMessageDialog(null, "Product Error",
                    "Product Error", JOptionPane.ERROR_MESSAGE);
            returnHome();
            return;
        }

        setup("View Product", 500, 500);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel middle = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel productInfo = new JLabel("<html> <br/> Product: " + productWords[0] +
                "<br/> <br/> Product Description: " + productWords[1] +
                "<br/> <br/> Product Seller: " + productWords[3] +
                "<br/> <br/> Product Store: " + productWords[2] +
                "<br/> <br/> Product Price: " + productWords[4] +
                "<br/> <br/> Product Quantity: " + productWords[5]
                + "<br/> <br/> </html>");


        top.add(productInfo);

        JPanel cart = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel addToCartLabel = new JLabel("Add to Cart: ");
        JTextField cartQuantity = new JTextField(15);
        JButton addToCart = new JButton("Add to Cart");
        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(cartQuantity.getText());
                } catch (NumberFormatException numFormat){
                    JOptionPane.showMessageDialog(null, "Please enter a valid number",
                            "Invalid Format", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String[] productInfoSplit = product.split(",");
                productInfoSplit[5] = String.valueOf(quantity);
                String productWithQuantitiy = String.join(",", productInfoSplit);

                String messageToServer = String.format("ADD PRODUCT TO CART,%s,%s", getEmail(), productWithQuantitiy);
                String result = (String) communicateWithServer(messageToServer);
                if (result.equals("ADDED TO CART")) {
                    JOptionPane.showMessageDialog(null, "Added to cart",
                            "Shoppingcart", JOptionPane.INFORMATION_MESSAGE);

                } else if (result.equals("NOT ENOUGH QUANTITY") || result.equals("INVALID PRODUCT")) {
                    JOptionPane.showMessageDialog(null, "Not Enough Quantitiy",
                            "Shoppingcart", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                returnHome();
            }
        });
        cart.add(addToCartLabel);
        cart.add(cartQuantity);
        cart.add(addToCart);


        JPanel buy = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel buyLabel = new JLabel("Buy:             ");
        JTextField buyQuantity = new JTextField(15);
        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = 0;
                    try {
                        quantity = Integer.parseInt(buyQuantity.getText());
                    } catch (NumberFormatException numFormat){
                        JOptionPane.showMessageDialog(null, "Please enter a valid number",
                                "Invalid Format", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                String[] productInfoSplit = product.split(",");
                productInfoSplit[5] = String.valueOf(quantity);
                String productWithQuantitiy = String.join(",", productInfoSplit);

                String messageToServer = String.format("BUY PRODUCT,%s,%s", getEmail(), productWithQuantitiy);
                String result = (String) communicateWithServer(messageToServer);
                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, "Successful purchase",
                            "Product Purchase", JOptionPane.INFORMATION_MESSAGE);

                } else if (result.equals("NOT ENOUGH QUANTITY") || result.equals("PRODUCT NOT FOUND")) {
                    JOptionPane.showMessageDialog(null, "Not Enough Quantitiy",
                            "Product Purchase", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                returnHome();
            }
        });

        buy.add(buyLabel);
        buy.add(buyQuantity);
        buy.add(buyButton);

        middle.add(cart);
        middle.add(buy);


        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnHome();
            }
        });

        bottom.add(backButton);

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

    }


    public JPanel error(String message) {
        JPanel error = new JPanel();
        error.add(new JLabel(message));
        return error;
    }


    public JPanel printStatistics(ArrayList<String> statistics, int width, int height) {
        if(statistics == null) {
            return error("No Statistics");
        }
        if (statistics.isEmpty()) {
            return error("No Statistics");
        }
        JPanel panel = new JPanel(new GridLayout(statistics.size(), 1, 4, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(width, height));
        for (String statistic : statistics) {
            JPanel component = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JTextArea line = new JTextArea(statistic);
            component.add(line);
            panel.add(component);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(width + 100, height + 100));
        JPanel view = new JPanel();
        view.add(scrollPane);
        return view;
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
        } else {
            loginPage();
        }
    }

}
