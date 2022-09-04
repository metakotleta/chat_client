import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            Client client = new Client("Vasya");
            Thread clientThread = new Thread(null, client::start, "client");
            clientThread.start();
            clientThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
