package server.inter;
import server.handler.ClientHandler;


public interface Server {
    int PORT = 3344;
    boolean isNickBusy(String nick);
    void broadcastMsg(String msg);
    void privateMsg(String s, String nickoftheReciever);
    void subscribe(ClientHandler client);
    void unsubscribe(ClientHandler client);
    void uniCast(ClientHandler from, String nickTo, String msg);
    AuthService getAuthService();


}
