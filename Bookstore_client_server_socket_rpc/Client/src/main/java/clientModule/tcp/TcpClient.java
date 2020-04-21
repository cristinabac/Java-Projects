package clientModule.tcp;

import commonModule.Message;
import commonModule.domain.validators.BookstoreException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {
    String  host;
    Integer portNumber;

    public TcpClient(String host, Integer portNumber) {
        this.host = host;
        this.portNumber = portNumber;
    }

    /**
     * Makes client - server communication
     * @param request - Message to be sent to server
     * @return Message - server respone
     * @throws BookstoreException - if client - server communication fails
     */
    public Message sendAndReceive(Message request)throws BookstoreException{
        Message response = Message.builder().build();
        try(Socket socket = new Socket(host, portNumber)){
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            //write message to server
            request.writeTo(outputStream);
            //receive response from server
            response.readFrom(inputStream);
            return  response;
        } catch (IOException e) {
            throw new BookstoreException("TCP Client Error:"+e.getMessage());
        }
    }
}
