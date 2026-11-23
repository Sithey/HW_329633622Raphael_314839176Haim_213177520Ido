package ex2;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class QuoteUDPClient {

    private static final Scanner scanner = new Scanner(System.in);

    public QuoteUDPClient() throws IOException {

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        while (true){
            byte[] buf = scanner.nextLine().getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8080);
            socket.send(packet);
            byte[] receivedBuf = new byte[256];
            packet = new DatagramPacket(receivedBuf, receivedBuf.length);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            if (received.equalsIgnoreCase("exit")){
                System.out.println("Client finished.");
                socket.close();
                break;
            }
            System.out.println("Quote received: " + received);
        }
    }


    public static void main(String[] args){
        try {
            new QuoteUDPClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
