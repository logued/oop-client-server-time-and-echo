package org.example;

/** SERVER        PROTOCOL                         Mar 2024
 * Server accepts client connection, reads requests from client,
 * and sends replies to client, all in accordance with the rules of the protocol.
 * Server then loops and waits for next request from same client, and continues to loop
 * until we terminate the server program.
 * The following PROTOCOL is implemented:
 * If ( the Server receives the request "time", from a Client )
 *      then : the server will send back the current time
 * If ( the Server receives the request "echo message", from a Client )
 *      then : the server will send back the message
 * If ( the Server receives the request it does not recognize  )
 *      then : the server will send back the message "error Sorry, I don't understand"
 * This is an example of a simple protocol, where the server's response is
 * based on the client's request.
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Server
{
    final int SERVER_PORT_NUMBER = 8888;  // could be any port from 1024 to 49151 (that doesn't clash with other Apps)

    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    public void start()
    {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT_NUMBER); )
        {
            while(true)
            {
                try (   Socket clientSocket = serverSocket.accept();
                        // connection is made.
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    System.out.println("Server Message: A Client has connected.");

                    String request = in.readLine(); // wait for input from the client, then read it.

                    System.out.println("Server message: Received from client : \"" + request + "\"");

                    // Implement our PROTOCOL
                    // The protocol is the logic that determines the responses given based on requests received.
                    //
                    if (request.startsWith("time"))  // so, client wants the time !
                    {
                        LocalTime time = LocalTime.now();  // get the time
                        out.println(time);  // send the time to client (as a string of characters)
                        System.out.println("Server message: time sent to client.");
                    } else if (request.startsWith("echo")) {
                        String message = request.substring(5); // strip off the leading substring "echo "
                        out.println(message);   // send the received message back to the client
                        System.out.println("Server message: echo message sent to client.");
                    } else  // an error
                    {
                        out.println("error I'm sorry I don't understand your request");
                        System.out.println("Server message: Invalid request from client.");
                    }
                    //out.flush();  // force the response to be sent
                }
            }
        } catch (IOException e) {
            System.out.println("Server Message: IOException: " + e);
        }
    }
}
