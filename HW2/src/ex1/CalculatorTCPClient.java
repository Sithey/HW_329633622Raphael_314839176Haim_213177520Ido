package ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CalculatorTCPClient {
    private static final Scanner scanner = new Scanner(System.in);

    public CalculatorTCPClient() throws IOException {
            Socket socket = new Socket("localhost", 9090);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String calculation = scanner.nextLine();
                out.println(calculation);
                String response = in.readLine();
                if (response == null){
                    System.out.println("It's was a pleasure.");
                    socket.close();
                    break;
                }
                System.out.println(calculation + " = " + response);
            }
    }

    public static void main(String[] args){

        try {
            new CalculatorTCPClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
