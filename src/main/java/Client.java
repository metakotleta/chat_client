import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    public static final String ADRESS = "192.168.31.103";
    public static final int PORT = 46555;
    private SocketChannel socketChannel;
    private String name;
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private ByteBuffer buffer = ByteBuffer.allocate(2048);

    public Client(String name) throws IOException {
        this.name = name;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter your name:");
            name = scanner.nextLine();
            socketChannel = SocketChannel.open(new InetSocketAddress(ADRESS, PORT));

            Thread listenThread = new Thread(this::listen, "listenThread");
            listenThread.start();
            while (true) {
                String msg = scanner.nextLine();
                if (msg.equals("/exit") || listenThread.isInterrupted()) {
                    socketChannel.close();
                    break;
                }
                byte[] byteMessage = objectMapper.writeValueAsBytes(new Message(name, msg));
                socketChannel.write(ByteBuffer.wrap(byteMessage));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        try {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int count = socketChannel.read(buffer);
                    if (count == -1) break;
                    Message msg = objectMapper.readValue(buffer.array(), 0, count, Message.class);
                    System.out.println(msg);
                    buffer.clear();
                }
            } catch (SocketException se) {
                socketChannel.close();
                System.err.println(se.getMessage());
                Thread.currentThread().interrupt();
            } catch (AsynchronousCloseException as) {
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
