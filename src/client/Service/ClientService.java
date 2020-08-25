package client.Service;

import javafx.application.Application;
import client.Controller;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientService {
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

 Controller con = new Controller();
    public ClientService() {
        try {
            socket = new Socket("localhost", 3344);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            setAuthorized(false);
            Thread t1 = new Thread(() -> {
                try {
                    while (true) {
                        String strMsg = in.readUTF();
                        if (strMsg.startsWith("/authOk")) {
                            setAuthorized(true);
                            break;
                        }
                     con.textArea.appendText(strMsg + "\n");

                    }
                    while (true) {
                        String strMsg = in.readUTF();
                        if (strMsg.equals("/exit")) {
                            break;
                        }
                    con.textArea.appendText(strMsg + "\n");


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t1.setDaemon(true);
            t1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setAuthorized(boolean b) {

    }


}