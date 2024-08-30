package server0.client;

import server0.server.ServerWindow1;

import javax.swing.*;
import java.awt.*;

public class ClientGUI1 extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

    private ServerWindow1 serverWindow1;
    private boolean connected;
    private String name;

    JButton login, send;
    JTextField ipField, portField, nameField, smsField;
    JPasswordField passwordField;
    JPanel headerPanel;
    JTextArea log;

    public ClientGUI1(ServerWindow1 serverWindow1) {
        this.serverWindow1 = serverWindow1;

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Client Chat");
        setResizable(false);
        setLocationRelativeTo(null);

        createPanel();

        setVisible(true);
    }

    private Component createHeaderPanel() {
        headerPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        ipField = new JTextField("127.0.0.1");
        portField = new JTextField("8189");
        nameField = new JTextField("Ivan Ivanovich");
        passwordField = new JPasswordField("123456");
        login = new JButton("Login");
        login.addActionListener(e -> connectServer());

        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        headerPanel.add(ipField);
        headerPanel.add(portField);
        headerPanel.add(new JPanel());
        headerPanel.add(nameField);
        headerPanel.add(passwordField);
        headerPanel.add(login);

        return headerPanel;
    }

    private void appendLog(String message) {
        log.append(message + "\n");
    }

    private void connectServer() {
        if (serverWindow1.connectUser(this)) {
            appendLog("You connected!\n");
            headerPanel.setVisible(false);
            connected = true;
            name = nameField.getText();
            String log = serverWindow1.getLog();
            if (log != null) {
                appendLog(log);
            }
        } else {
            appendLog("Not connected!");
        }
    }

    public void disconnectFromServer() {
        if (connected) {
            headerPanel.setVisible(true);
            connected = false;
            serverWindow1.disconnectUser(this);
            appendLog("You have been disconnected from the server!");
        }
    }

    public void message() {
        if (connected) {
            String text = smsField.getText();
            if (!text.isEmpty()) {
                serverWindow1.message(name + ": " + text);
                smsField.setText("");
            }
        } else {
            appendLog("Not connected to server!");
        }
    }

    public void answer(String text) {
        appendLog(text);
    }

    public Component createSendSms() {
        JPanel jPanel = new JPanel(new BorderLayout());
        smsField = new JTextField();
        send = new JButton("Send");
        send.addActionListener(e -> message());

        jPanel.add(smsField);
        jPanel.add(send, BorderLayout.EAST);
        return jPanel;
    }

    private Component createLog() {
        log = new JTextArea();
        log.setEditable(false);
        log.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return new JScrollPane(log);
    }

    private void createPanel() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog(), BorderLayout.CENTER);
        add(createSendSms(), BorderLayout.SOUTH);
    }
}
