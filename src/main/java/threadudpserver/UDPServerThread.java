package threadudpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class UDPServerThread {

    static DatagramPacket dgPacketRequest;
    static DatagramPacket dgPacketResponse;
    static DatagramSocket dgSocketServer;
    static List<String> jokes;

    private static final int port = 50000;

    public static void main(String[] args) {
        readJokes();
        setServer();
    }

    private static void setServer() {
        boolean hasJokes = true;
        while (hasJokes) {
            try {
                dgSocketServer = new DatagramSocket(port);
                byte[] buffer = new byte[512];

                dgPacketRequest = new DatagramPacket(buffer, buffer.length);
                dgSocketServer.receive(dgPacketRequest);
                InetAddress clientAddress = dgPacketRequest.getAddress();
                int clientPort = dgPacketRequest.getPort();

                if(jokes.size() > 0) {
                    final int jokeNumber = (int) ((Math.random() * (jokes.size())));
                    buffer = jokes.get(jokeNumber).getBytes();
                    jokes.remove(jokeNumber);
                } else {
                    buffer = "No more jokes...".getBytes();
                    hasJokes = false;
                }

                dgPacketResponse = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                dgSocketServer.send(dgPacketResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void readJokes() {
        jokes = new ArrayList<>();
        InputStream inputStream = UDPServerThread.class.getClassLoader().getResourceAsStream("jokes.txt");
//        InputStream in = UDPServer.class.getResourceAsStream("jokes.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                jokes.add(line);
            }
        } catch (IOException e) {}
    }

}
