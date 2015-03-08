import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static Connect con;

    Server(Connect c) {
        con = c;
    }
    
    public boolean start() {
        try {
            ServerSocket socketListener = new ServerSocket(1234);
            Socket client;
            while (true) {
                client = null;
                while (client == null) {
                    client = socketListener.accept();
                }
                Runnable r = new ClientThread(client);
                Thread t = new Thread(r);
                t.start();
            }
        } catch (IOException ex) {
            return false;
        }
    }
}
