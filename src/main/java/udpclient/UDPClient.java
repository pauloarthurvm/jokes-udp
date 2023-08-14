package udpclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPClient {

    private static DatagramPacket dgPacketRequest;
    private static DatagramPacket dgPacketResponse;
    private static DatagramSocket dgSocket;
    private static InetAddress address;

    private static final int port = 50000;

//   Quote Of The Day Server: djxmmx.net
//   QOTD port: 17

    public static void main(String[] args) {

        sendPacket();
        String quote = receivePacket();
        System.out.println(quote);

    }

    private static String receivePacket() {
        final byte[] bufferResponse = new byte[512];
        try {
            dgPacketResponse = new DatagramPacket(bufferResponse, bufferResponse.length);
            dgSocket.receive(dgPacketResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(bufferResponse, 0, dgPacketResponse.getLength());
    }

    private static void sendPacket() {
        final byte[] bufferRequest = "Make me laugh...".getBytes();
        try {
            address = InetAddress.getLocalHost();
            dgPacketRequest = new DatagramPacket(bufferRequest, bufferRequest.length, address, port);
            address = InetAddress.getByName("localhost");
            dgSocket = new DatagramSocket();
            dgSocket.send(dgPacketRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
