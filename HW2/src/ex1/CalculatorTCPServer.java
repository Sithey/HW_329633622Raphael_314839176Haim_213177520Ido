package ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorTCPServer {

    public CalculatorTCPServer() throws IOException{

        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("opening server " + serverSocket.getInetAddress().getHostName() +":" + serverSocket.getLocalPort());

        while (true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected !");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            double ans = 0;
            boolean hasAns = false;
            while (true){
                String message = in.readLine();
                if (message == null || message.equalsIgnoreCase("exit")){
                    clientSocket.close();
                    break;
                }
                System.out.println("Received expression: " + message);
                try {
                    double result = getAnswer(message, ans, hasAns);
                    ans = result;
                    hasAns = true;
                    out.println(result);
                }catch (IllegalArgumentException e){
                    out.println(e.getMessage());
                }

            }
        }
    }

    private double getAnswer(String message, double ans, boolean hasAns){

        String[] words = message.split(" ");
        if (words.length != 3){
            throw new IllegalArgumentException("Error: Invalid expression");
        }
        double a, b;
        try {
            if (!words[0].equals("ANS")) {
                a = Double.parseDouble(words[0]);
            }else{
                if (!hasAns){
                    throw new IllegalArgumentException("Error: No previous result");
                }
                a = ans;
            }
            if (!words[2].equals("ANS")) {
                b = Double.parseDouble(words[2]);
            }else{
                if (!hasAns){
                    throw new IllegalArgumentException("Error: No previous result");
                }
                b = ans;
            }
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Error: Invalid expression");
        }
        String operation = words[1];
        String[] operations = new String[]{"+", "-", "/", "*"};
        boolean contain = false;
        for (String o : operations){
            if (o.equalsIgnoreCase(operation)) {
                contain = true;
                break;
            }
        }

        if (!contain){
            throw new IllegalArgumentException("Error: Invalid expression");
        }

        return switch (operation) {
            default -> -1;
            case "+" -> a + b;
            case "-" -> a - b;
            case "/" -> {
                if (b == 0) {
                    throw new IllegalArgumentException("Error: Division by zero");
                }
                yield a / b;
            }
            case "*" -> a * b;
        };
    }

    public static void main(String[] args) {

        try {
            new CalculatorTCPServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
