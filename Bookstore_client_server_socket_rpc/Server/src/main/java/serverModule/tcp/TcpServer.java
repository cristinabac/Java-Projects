package serverModule.tcp;

import commonModule.Message;
import commonModule.domain.validators.BookstoreException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

public class TcpServer {

    private ExecutorService executorService;
    private int port;
    private Map<String, UnaryOperator<Message>> methodHandlers;

    public TcpServer(ExecutorService executorService, int port) {
        this.executorService = executorService;
        this.port = port;
        methodHandlers = new HashMap<>();
    }

    public void addHandler(String methodName, UnaryOperator<Message> handler) {
        methodHandlers.put(methodName, handler);
    }

    public void startServer() {
        System.out.println("starting server");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected");

                executorService.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BookstoreException("could not start server", e);
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (InputStream is = clientSocket.getInputStream();
                 OutputStream os = clientSocket.getOutputStream()) {

                Message request = Message.builder().build();
                request.readFrom(is);
                System.out.println("server - received request: " + request);

                Message response = methodHandlers.get(request.getHeader()).apply(request);

                System.out.println("server - computed response: " + response);
                response.writeTo(os);


            } catch (IOException e) {
                e.printStackTrace();
                throw new BookstoreException("server - data exchange error", e);
            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
