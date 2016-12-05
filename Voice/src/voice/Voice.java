package voice;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.JTextArea;

class Login {

    boolean loggedIn;
    ArrayList<Player> playerDatabase = new ArrayList<Player>();
    Scanner input = new Scanner(System.in);

    public Login() {
        loggedIn = false;
        run();
    }

    public void run() {

        boolean play = true;
        int choice;

        String name = "default";
        String password = "123abc";
        String email = "CORE@gmail.com";

        Player defaultPlayer = new Player(name, password, email);
        playerDatabase.add(defaultPlayer);

        System.out.println("Welcome to Slot Frenzy!!!");
        while (play) {

            switch (0) {
                case 1: {
                    createAccount();
                    break;
                }
                case 2: {
                    login();
                }
                case 3: {

                }
                case 4: {

                }
                case 5: {
                    play = false;
                    System.out.println("Exiting...");
                    break;
                }

            }

        }
    }

    public void createAccount() {

        String name = "", password = "", email = "";
        boolean failed = true;
        name = input.nextLine();
        while (failed) {
            failed = false;
            System.out.print("Enter new name: ");
            name = input.nextLine();
            System.out.print("Enter new password: ");
            password = input.nextLine();
            System.out.print("Enter new email: ");
            email = input.nextLine();
            for (int i = 0; i < playerDatabase.size(); i++) {
                if (name.equals(playerDatabase.get(i).getName())) {
                    System.out.println("ERROR: Name already in use.");
                    failed = true;
                }
            }
        }
        Player newPlayer = new Player(name, password, email);
        playerDatabase.add(newPlayer);
    }

    public void login() {
        String name = "";
        String password = "";
        name = input.nextLine();
        while (!loggedIn) {
            System.out.print("Enter name: ");
            name = input.nextLine();
            System.out.print("Enter password: ");
            password = input.nextLine();
            for (int i = 0; i < playerDatabase.size(); i++) {
                if (name.equals(playerDatabase.get(i).getName())) {
                    if (password.equals(playerDatabase.get(i).getPassword())) {
                        loggedIn = true;
                    }
                }
            }
            if (!loggedIn) {
                System.out.println("Incorrect login credentials");
            }
        }
    }

}

class Player {

    String name;
    String password;
    String email;

    public Player(String newName, String newPassword, String newEmail) {

        name = newName;
        password = newPassword;
        email = newEmail;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    public void setEmail(String newEmail) {
        email = newEmail;
    }

}

public class Voice extends JPanel {

    static JTextArea output;

    public static void main(String[] args) {

        final JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setPreferredSize(new Dimension(80, 45));
        northPanel.setBackground(Color.black);
        frame.getContentPane().add("North", northPanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(80, 35));
        centerPanel.setBackground(Color.white);
        centerPanel.setSize(450, 200);
        frame.getContentPane().add("Center", centerPanel);

        JLabel loginLabel = new JLabel("Login: ", JLabel.RIGHT);
        JLabel passwordLabel = new JLabel("Password: ", JLabel.CENTER);
        final JTextField loginField = new JTextField(10);
        final JPasswordField passwordField = new JPasswordField(10);
        centerPanel.add(loginLabel);
        centerPanel.add(loginField);
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);

        final JLabel verLabel = new JLabel();
        verLabel.setVisible(false);
        centerPanel.add(verLabel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.gray);
        frame.getContentPane().add("South", bottomPanel);
        //bottomPanel.setBorder(new EtchedBorder());

        output = new JTextArea("\n", 33, 38);
        output.setBackground(Color.white);
        bottomPanel.add(output);
        output.setVisible(true);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                true, centerPanel, bottomPanel);
        frame.getContentPane().add(splitPane);
        splitPane.setDividerLocation(0.9);

        JButton genButton = new JButton("Generate Text");
        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");

        final JLabel label = new JLabel("Hello World");
        label.setVisible(false);

        northPanel.add(loginButton);
        bottomPanel.add(genButton, BorderLayout.WEST);
        northPanel.add(exitButton);
        bottomPanel.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 700);
        frame.setVisible(true);

        genButton.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                synchronized (genButton) {
                    genButton.notify();
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                System.exit(0);

            }
        });

        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                loginField.setText("");
                passwordField.setText("");
                verLabel.setText("Logged In!");
                verLabel.setVisible(true);
                loginLabel.setVisible(false);
                loginField.setVisible(false);
                passwordLabel.setVisible(false);
                passwordField.setVisible(false);
            }
        });

        ConfigurationManager cm;

        cm = new ConfigurationManager(Voice.class.getResource("new.xml"));

        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();

        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }
        String word;
        while (true) {

            synchronized (genButton) {
                try {
                    genButton.wait();
                } catch (InterruptedException ex) {
                    System.out.println("Interrupted");
                }
            }
            System.out.println("After button clicked");

            word = getInstructions();
            word = word.toLowerCase();
            output.append("Say " + word + "\n");
            output.setVisible(true);

            Result result = recognizer.recognize();

            if (result != null) {

                String resultText = result.getBestResultNoFiller();
                resultText = resultText.toLowerCase();
                output.append("You said: " + resultText + "\n");
                output.setVisible(true);

                if (word.equals(resultText)) {
                    output.append("Correct! \n");
                    output.setVisible(true);
                } else {
                    output.append("Incorrect! \n");
                    output.setVisible(true);
                }
            } else {
                output.append("I can't hear what you said. \n");
            }
        }
    }

    public static String getInstructions() {
        String[] wordList = {"I'm hungry", "Nice to meet you", "The dog barked", "My name is Sam", "Computer Science", "Programming is fun"};
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(6);

        return wordList[randomInt];
    }

}
