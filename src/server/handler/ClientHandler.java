package server.handler;


import server.inter.Server;
import server.service.ServerImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
      private ServerImpl server;
      private Socket socket;
      private DataInputStream in;
      private DataOutputStream out;
      private String nick;
      public String getNick() {
        return nick;
    }

    public ClientHandler(ServerImpl server, Socket socket) throws IOException {
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.nick = "";

            new Thread(() -> {
                try {
                    authentication();
                    readMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    private void authentication() throws IOException {
        while (true) {
            String str = in.readUTF();
            if (str.startsWith("/auth")) {
                String[] dataArray = str.split("\\s");
                String nick = server.getAuthService().getNick(dataArray[1], dataArray[2]);
                if (nick != null) {
                    if (!server.isNickBusy(nick)) {
                        sendMsg("/authOk " + nick);
                        this.nick = nick;
                        server.broadcastMsg(this.nick + " Join to chat");
                        server.subscribe(this);
                        return;
                    } else {
                        sendMsg("You are logged in");
                    }
                } else {
                    sendMsg("Incorrect password or login");
                }
            }
            if (str.equalsIgnoreCase("/end")) {
                sendMsg("/clientClose");
                break;
            }
            if (str.startsWith("/w")){
                String[] words = str.split(" ");
                String nickoftheReciever = words[1];
                String message = str.substring(3);
                server.privateMsg(nick+": " + message, nickoftheReciever);
            } else {
                server.broadcastMsg(nick + ": " + str);}
        }
           try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.unsubscribe(ClientHandler.this);
        }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessage() throws IOException {
        while (true) {
            String clientStr = in.readUTF();
            System.out.println("from " + this.nick + ": " + clientStr);
            if (clientStr.equals("/exit")) {
                return;
            }
            server.broadcastMsg(this.nick + ": " + clientStr);
        }
    }

    private void closeConnection() {
        server.unsubscribe(this);
        server.broadcastMsg(this.nick + ": out from chat");

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
