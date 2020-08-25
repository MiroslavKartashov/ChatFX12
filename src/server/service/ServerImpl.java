package server.service;

import server.handler.ClientHandler;
import server.inter.AuthService;
import server.inter.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ServerImpl implements Server {

    private List<ClientHandler> clients;
    private AuthService authService;

    public ServerImpl() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            authService = new AuthServiceImpl();
            authService.start();
            clients = new LinkedList<>();
            while (true) {
                System.out.println("Wait join clients");
                Socket socket = serverSocket.accept();
                System.out.println("Client join");
            }
        } catch (IOException e) {
            System.out.println("Problem in server");
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    @Override
    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler c : clients) {
            if (c.getNick() != null && c.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler c : clients) {
            c.sendMsg(msg);
        }
    }

    @Override
    public void privateMsg(String s, String nickoftheReciever) {
        for (ClientHandler с : clients) {
            if (с.getNick().equalsIgnoreCase(nickoftheReciever)) {
                с.sendMsg(s);
            }
        }

    }

    @Override
    public void subscribe(ClientHandler client) {
        clients.add(client);
    }

    @Override
    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }

    @Override
    public void uniCast(ClientHandler from, String nickTo, String msg) {
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nickTo)) {
                o.sendMsg("from " + from.getNick() + ": " + msg);
                from.sendMsg("to " + nickTo + " you sent: " + msg);
                return;
            }
        }
        from.sendMsg("Nick: " + nickTo + " was not found in the chat.");
    }

    @Override
    public AuthService getAuthService() {
        return null;
    }
}
