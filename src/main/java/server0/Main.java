package server0;

import server0.client.ClientGUI1;
import server0.server.ServerWindow1;

public class Main {
    public static void main(String[] args) {
        ServerWindow1 serverWindow1 = new ServerWindow1();
        new ClientGUI1(serverWindow1);
    }
}
