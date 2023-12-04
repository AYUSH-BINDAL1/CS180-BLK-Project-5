import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
public class LoginPage extends JFrame {
    private JTextField email;
    private JTextField password;

    public LoginPage() {
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

        email = new JTextField(15);
        password = new JTextField(15);

        JButton loginButton = new JButton("Login/Create Account");
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CustomerPage();
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

    public void CustomerPage() {
        // Remove content from the previous pane
        getContentPane().removeAll();
        revalidate();
        repaint();

        setTitle("MarketPlace");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new GridLayout(4 , 2, 5, 5));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton shoppingCart = new JButton("Shopping Cart");
        JButton editAccount = new JButton("Edit Account");
        JButton deleteAccount = new JButton("Delete Account");
        JComboBox<String> sortBy = new JComboBox<>(new String[]{"Sort By Store", "Sort By Seller"});
        JComboBox<String> searchBy = new JComboBox<>(new String[]{"Search By Store", "Search by Seller"});
        JButton viewPurchaseHistory = new JButton("View/Export Purchase History");
        JButton viewStatistics = new JButton("View store/seller Statistics");

        top.add(exit);
        top.add(shoppingCart);
        top.add(editAccount);
        top.add(deleteAccount);
        top.add(sortBy);
        top.add(searchBy);
        top.add(viewPurchaseHistory);
        top.add(viewStatistics);

        add(top, BorderLayout.NORTH);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPage().setVisible(true);
            }
        });
    }
}
