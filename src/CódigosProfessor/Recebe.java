package CódigosProfessor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Recebe implements Runnable {

    List<Individuos> membros;
    MulticastSocket s;
    String apelido;
    boolean cthread;
    DatagramSocket SocketUDP;

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public Recebe(List<Individuos> m, MulticastSocket cc, String ape, boolean bol, DatagramSocket udpSocket) {
        this.membros = m;
        this.s = cc;
        this.apelido = ape;
        this.cthread = bol;
        this.SocketUDP = udpSocket;
        
    }

    public List<Individuos> getMembros() {
        return membros;
    }

    public void removeMembro(String ape) {
        for (Individuos e : this.getMembros()) {
            if (e.getApelido().equals(ape)) {
                this.membros.remove(e);
            }
        }
    }

    public void addMembro(Individuos e) {
        this.membros.add(e);
    }

    public void setMembros(List<Individuos> membros) {
        this.membros = membros;
    }

    public MulticastSocket getS() {
        return s;
    }

    public void setS(MulticastSocket s) {
        this.s = s;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1000];//aguarda o recebimento de msgs de outros peers
        while (this.cthread) {
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            try {
                s.receive(messageIn);
            } catch (IOException ex) {
                Logger.getLogger(Recebe.class.getName()).log(Level.SEVERE, null, ex);
            }

            String a = new String(messageIn.getData());

            String[] b = a.split("\\[");

            String[] ape = b[1].split("\\]");

            String pg = b[0].trim();

            if (pg.equals("JOIN")) {
                InetAddress ipzin = messageIn.getAddress();
                String x = "JOINACK [" + this.getApelido() + "]";
                byte[] m = x.getBytes();
                DatagramPacket joinack = new DatagramPacket(m, m.length, ipzin, 6799);
                try {
                    this.SocketUDP = new DatagramSocket();
                    this.SocketUDP.send(joinack);
                    this.SocketUDP = null;
                } catch (SocketException ex) {
                    Logger.getLogger(Recebe.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Recebe.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Individuos novo = new Individuos();
                novo.setApelido(ape[0]);
                novo.setIp(messageIn.getAddress().toString());
                this.addMembro(novo);
                m = new byte[1000];
                System.out.println("nome: "+novo.getApelido()+" ip: "+novo.getIp());
                
            } else if (pg.equals("LEAVE")) {
                this.removeMembro(b[1]);
                System.out.println("Membro ["+ape[0]+"] Removido!");
            }

            System.out.println("Recebido:" + new String(messageIn.getData()));
            buffer = new byte[1000];
        }
    }
}
