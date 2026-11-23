package ex2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuoteUDPServer {


    private List<String> citations = new ArrayList<>();
    private Random random = new Random();

    public QuoteUDPServer() throws IOException {
        citations.add("A rose by any other name would smell as sweet.");
        citations.add("All that glitters is not gold.");
        citations.add("All the world’s a stage, and all the men and women merely players.");
        citations.add("Ask not what your country can do for you; ask what you can do for your country.");
        citations.add("Ask, and it shall be given you; seek, and you shall find.");

        DatagramSocket socket = new DatagramSocket(8080);
        byte[] buf = new byte[256];
        System.out.println("UDP Quote Server listening on port " + socket.getLocalPort());
        while (true){
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Request received: " + received);
            if (received.equalsIgnoreCase("exit")){
                socket.send(packet);
                System.out.println("Server shutting down.");
                socket.close();
                break;
            }
            String quote = getCitation();
            byte[] response = quote.getBytes();
            packet = new DatagramPacket(response, response.length, address, port);

            socket.send(packet);


        }

    }
    private String getCitation(){
        return citations.get(random.nextInt(citations.size()));
    }

    public static void main(String[] args){
        try {
            new QuoteUDPServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
