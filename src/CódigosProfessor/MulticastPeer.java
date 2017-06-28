package CódigosProfessor;

/**
 * MulticastPeer: Implementa um peer multicast Descricao: Envia uma mensagem
 * para todos os membros do grupo. Apos tres msgs, finaliza.
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MulticastPeer {

    public static void main(String args[]) throws SocketException {
        /* args[0]: apelido e args[1]: ip multicast (entre 224.0.0.0 a 239.255.255.255 */
        String apelido = new String();
        apelido = args[0];
        
        String mensagem = "i aeee MULeKinHo ZikA";

        List<Individuos> membros = new ArrayList<>();
        
        MulticastSocket s = null;
        try {

            InetAddress group = InetAddress.getByName(args[1]);//cria um grupo multicast
            s = new MulticastSocket(6789);//cria um socket multicast
            s.joinGroup(group);//adiciona o host ao grupo

            byte[] buffer = new byte[1000];//aguarda o recebimento de msgs de outros peers
            byte[] msg = new byte[1000];//armazena minha mensagem

            String msgjoin = new String();
            msgjoin = apelido + "|||" + "JOIN";
            msg = msgjoin.getBytes();
            
            DatagramPacket messageOut = new DatagramPacket(msg, msg.length, group, 6789);
            s.send(messageOut);//envia o datagrama como multicast
            msg = new byte[1000];
            /*Recebe threadchat = new Recebe(membros,s,apelido);
            threadchat.run();*/
            msgjoin = apelido + "|||" + mensagem;
            msg = msgjoin.getBytes();
            
            messageOut = new DatagramPacket(msg, msg.length, group, 6789);
            s.send(messageOut);//envia o datagrama como multicast
            msg = new byte[1000];
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (s != null) {
                s.close(); //fecha o socket
            }
        } //finally
    } //main		      	
}//class
