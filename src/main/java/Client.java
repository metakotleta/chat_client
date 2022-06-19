import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static final String ADRESS = "192.168.31.68";
    public static final int PORT = 46555;
    final SocketChannel socketChannel;
    private String name;

    public Client(String name) throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(ADRESS, PORT));
        this.name = name;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter your name:");
            name = scanner.nextLine();
            new Thread(this::listen);
            System.out.printf("%s, welcome to chat!\n", name);
            while (true) {
                System.out.printf("%s:", name);
                String msg = scanner.nextLine();
                socketChannel.write(ByteBuffer.wrap((name + ": " + msg + "\n").getBytes(StandardCharsets.UTF_8)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        try {
            while (true) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int count = socketChannel.read(buffer);
                if (count == -1) break;
                String msg = new String(buffer.array(), 0, count, StandardCharsets.UTF_8);
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
