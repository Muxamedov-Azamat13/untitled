package server0.server;

import server0.client.ClientGUI1;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow1 extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    public static final String LOG_PATH = "src/main/java/server0/log.txt";

    private List<ClientGUI1> clientGUI1List;

    JButton btnStart, btnStop;
    JTextArea log;
    private boolean work;

    public ServerWindow1() {
        clientGUI1List = new ArrayList<>();

        setTitle("Chat Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        createPanel();

        setVisible(true);
    }

    private Component createButtons() {
        JPanel jPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");

        btnStart.addActionListener(e -> startServer());
        btnStop.addActionListener(e -> stopServer());

        jPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel.add(btnStart);
        jPanel.add(btnStop);
        return jPanel;
    }



    public void createPanel() {
        log = new JTextArea();
        log.setEditable(false);
        log.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(new JScrollPane(log), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    public boolean connectUser(ClientGUI1 clientGUI1) {
        if (!work) {
            return false;
        }
        clientGUI1List.add(clientGUI1);
        return true;
    }

    private void saveLog(String text) {
        try (FileWriter fw = new FileWriter(LOG_PATH, true)) {
            fw.write(text);
            fw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLog() {
        try {
            String content = Files.readString(Paths.get(LOG_PATH));

            // Удаляем последний символ, если строка не пустая
            if (!content.isEmpty()) {
                content = content.substring(0, content.length() - 1);
            }
            return content;

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLog() {
        return readLog();
    }

    public void disconnectUser(ClientGUI1 clientGUI1) {
        clientGUI1List.remove(clientGUI1);
        if (clientGUI1 != null) {
            clientGUI1.disconnectFromServer();
        }
    }

    private void answerAll(String text) {
        for (ClientGUI1 clientGUI1 : clientGUI1List) {
            clientGUI1.answer(text);
        }
    }

    public void message(String text) {
        if (!work) {
            return;
        }
        appendLog(text + "\n");
        answerAll(text);
    }

    private void startServer() {
        work = true;
        appendLog("Server started!\n");
    }

    private void stopServer() {
        work = false;
        appendLog("Server stopped!\n");
    }

    private void appendLog(String message) {
        log.append(message);
        saveLog(message); // Лог сохраняется в файл каждый раз, когда он обновляется
    }
}
