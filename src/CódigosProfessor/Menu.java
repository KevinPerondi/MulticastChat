package CódigosProfessor;

import java.io.IOException;
import static java.lang.System.exit;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    public static void main(String[] args) throws IOException {

        /* args[0]: apelido e args[1]: ip multicast (entre 224.0.0.0 a 239.255.255.255 */
        String apelido = args[0];
        InetAddress group = InetAddress.getByName(args[1]);//cria um grupo multicast
        int opcao = 0;
        String op = null;
        List<Individuos> membros = new ArrayList<>();
        boolean controleall = true;

        MulticastSocket s = null;

        DatagramSocket udpSocket = null;
        try {
            s = new MulticastSocket(6789);//cria um socket multicast
            s.joinGroup(group);//adiciona o host ao grupo
            byte[] msg = new byte[1000];//armazena minha mensagem

            String msgjoin = new String();
            msgjoin = "JOIN [" + apelido + "]";
            msg = msgjoin.getBytes();

            DatagramPacket messageOut = new DatagramPacket(msg, msg.length, group, 6789);
            s.send(messageOut);//envia o datagrama como multicast

            msg = new byte[1000];
            DatagramPacket pegaMeuIP = new DatagramPacket(msg, msg.length);
            s.receive(pegaMeuIP);
            String a = new String(pegaMeuIP.getData()).trim();
            InetAddress meuip = null;
            if (a.equals(msgjoin)) {
                meuip = pegaMeuIP.getAddress();
            }
            msg = new byte[1000];

            Thread threadchat = new Thread(new Recebe(membros, s, apelido, controleall, udpSocket));
            threadchat.start();

            Thread armazenaMembros = new Thread(new RecebeUDP(membros, apelido, "/home/todos/alunos/cm/a1552287/Downloads/Arq", meuip, controleall));
            armazenaMembros.start();

            while (controleall) {
                System.out.println("Opções:");
                System.out.println("1-Mensagem Multicast\n" + "2-Mensagem privada\n" + "3-Solicitar Listagem de Arqivos\n"
                        + "4-Baixar arquivo de usuario\n" + "5-Listar membros do multicast\n" + "6-Sair\n");
                Scanner scanner = new Scanner(System.in);
                op = scanner.nextLine();
                opcao = Integer.parseInt(op);
                switch (opcao) {
                    case 1:
                        String x = scanner.nextLine();
                        x = "MSG [" + apelido + "] " + x;
                        msg = x.getBytes();
                        messageOut = new DatagramPacket(msg, msg.length, group, 6789);
                        s.send(messageOut);
                        msg = new byte[1000];
                        break;
                    case 2:
                        System.out.println("Digite o apelido e a mensagem(exemplo: apelido mensagem): ");
                        String y = scanner.nextLine();
                        String[] b = y.split(" ");
                        for (Individuos e : membros) {
                            if (e.getApelido().equals(b[0])) {
                                String msgPrivada = "MSGIDV FROM [" + apelido + "] TO [" + e.getApelido() + "] " + b[1];
                                //System.out.println(msgPrivada);
                                msg = msgPrivada.getBytes();
                                InetAddress ipudp = InetAddress.getByName(e.getIp());
                                messageOut = new DatagramPacket(msg, msg.length, ipudp, 6799);
                                udpSocket = new DatagramSocket();
                                udpSocket.send(messageOut);
                                msg = new byte[1000];
                                udpSocket = null;
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Digite o apelido para quem ira solicitar os arquivos: ");
                        String bn = scanner.nextLine();
                        for (Individuos e : membros) {
                            if (e.getApelido().equals(bn)) {
                                String msgPrivada = "LISTFILES [" + apelido + "]";
                                msg = msgPrivada.getBytes();
                                InetAddress ipudp = InetAddress.getByName(e.getIp());
                                messageOut = new DatagramPacket(msg, msg.length, ipudp, 6799);
                                udpSocket = new DatagramSocket();
                                udpSocket.send(messageOut);
                                msg = new byte[1000];
                                udpSocket = null;
                            }
                        }
                        break;

                    case 4:
                        System.out.println("Digite o apelido e o nome do arquivo(exemplo: apelido arquivo): ");
                        String df = scanner.nextLine();
                        String[] aux = df.split(" ");
                        for (Individuos e : membros) {
                            if (e.getApelido().equals(aux[0])) {
                                String msgPrivada = "DOWNFILE [" + apelido + "]" + aux[1];
                                msg = msgPrivada.getBytes();
                                InetAddress ipudp = InetAddress.getByName(e.getIp());
                                messageOut = new DatagramPacket(msg, msg.length, ipudp, 6799);
                                udpSocket = new DatagramSocket();
                                udpSocket.send(messageOut);
                                msg = new byte[1000];
                                udpSocket = null;
                            }
                        }
                        break;

                    case 5:
                        for (Individuos e : membros) {
                            System.out.println("nome: " + e.getApelido() + " ip: " + e.getIp());
                        }
                        break;

                    case 6:
                        String msgleave = new String();
                        msgleave = "LEAVE [" + apelido + "]";
                        msg = msgleave.getBytes();
                        messageOut = new DatagramPacket(msg, msg.length, group, 6789);
                        s.send(messageOut);//envia o datagrama como multicast
                        msg = new byte[1000];
                        controleall = false;
                        threadchat.interrupt();
                        armazenaMembros.interrupt();
                    /*s.close();
                        udpSocket.close();
                        exit(1);*/

                    default:
                        System.out.println("opcao invalida");
                        break;
                }
            }
            s.close();
            udpSocket.close();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            s.close();
            udpSocket.close();
        }

    }

}
