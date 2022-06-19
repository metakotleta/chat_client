import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            Client client = new Client("Vasya");
            new Thread(null, client::start, "client").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
