package Servidor;

import java.io.IOException;
import java.net.*;

public class Servidor {
    
    private ServerSocket socket;

    public Servidor(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }
        
    
    public static void main(String[] args) {
        
    }
}
