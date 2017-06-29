package CódigosProfessor;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class RecebeUDP implements Runnable {

    private List<Individuos> membros;
    private String diretorio;
    private String meuapelido;
    private File[] myfiles;
    private InetAddress meuip;
    boolean cthread;

    public InetAddress getMeuip() {
        return meuip;
    }

    public void setMeuip(InetAddress meuip) {
        this.meuip = meuip;
    }

    public String getMeuapelido() {
        return meuapelido;
    }

    public void setMeuapelido(String meuapelido) {
        this.meuapelido = meuapelido;
    }

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public RecebeUDP(List<Individuos> m, String ape, String direc, InetAddress mip, boolean controle) {
        this.myfiles = null;
        this.membros = m;
        this.meuapelido = ape;
        this.diretorio = direc;
        this.meuip = mip;
        this.cthread = controle;
    }

    public List<Individuos> getMembros() {
        return membros;
    }

    public void setMembros(List<Individuos> membros) {
        this.membros = membros;

    }

    public File[] getMyfiles() {
        return myfiles;
    }

    public void setMyfiles(File[] myfiles) {
        this.myfiles = myfiles;
    }

    public void addMembros(Individuos e) {
        this.membros.add(e);
    }

    public void listFilesForFolder() {//metodo nao funcionando
        this.myfiles = null;//zerando meus arquivos
        File[] folderfiles = new File(this.getDiretorio()).listFiles();
        this.setMyfiles(folderfiles);
    }

    @Override
    public void run() {
        try {
            DatagramSocket aSocket = new DatagramSocket(6799);
            while (cthread) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);

                aSocket.receive(request);

                String a = new String(request.getData());
                String[] b = a.split("\\[");

                System.out.println(a);
                
                if (b[0].equals("JOINACK ")) {
                    String apelido = b[1].replace("\\]", "");
                    apelido = apelido.trim();
                    String ip = request.getAddress().toString();
                    Individuos novo = new Individuos(apelido, ip);
                    this.addMembros(novo);

                } else if (b[0].equals("LISTFILES ")) {
                    //resoponder FILES [arq1, arq2, arqN] 
                    this.listFilesForFolder();//a cada LISTFILES, a lista é atualizada

                    InetAddress ipretorno = request.getAddress();

                    String resposta = new String();
                    for (File e : this.getMyfiles()) {
                        resposta = resposta.concat(e.getName() + ", ");
                    }
                    String respostaNew = resposta.substring(0, resposta.length() - 2);
                    String msgresposta = "FILES [" + respostaNew + "]";
                    byte[] m = msgresposta.getBytes();
                    //DatagramSocket skt = new DatagramSocket();
                    DatagramPacket resp = new DatagramPacket(m, m.length, ipretorno, 6799);
                    aSocket.send(resp);
                    //skt.close();

                } else if (b[0].equals("DOWNFILE ")) {//DOWNFILE [apelido] filename 
                    String[] j = b[1].split("\\]");
                    String arqname = j[1].trim();

                    InetAddress ipretorno = request.getAddress();
                    int porta = 7321;
                    long sizearq = new File(this.getDiretorio() + arqname).length();

                    String msgresposta = "DOWNINFO [" + arqname + ", " + sizearq
                            + ", " + this.getMeuip() + ", " + porta + "]";
                    byte[] m = msgresposta.getBytes();
                    DatagramSocket skt = new DatagramSocket();
                    DatagramPacket resp = new DatagramPacket(m, m.length, ipretorno, 6799);
                    skt.send(resp);
                    skt.close();

                    Thread up = new Thread(new UploaderTCP(porta, InetAddress.getLocalHost(), j[1], sizearq, this.getDiretorio(), ipretorno));
                    up.start();

                } else if (b[0].equals("DOWNINFO ")) {//DOWNINFO[nomeArquivo,sizeArquivo,ip,porta]
                    String xc = b[1].replace("\\]", "");
                    xc = xc.trim();

                    String[] div = xc.split(",");
                    String arqName = div[0].trim();
                    int arqSize = Integer.parseInt(div[1].trim());
                    InetAddress ipadres = InetAddress.getByName(div[2].trim());
                    int portN = Integer.parseInt(div[3].trim());
                    Thread down = new Thread(new DownloaderTCP(ipadres, portN, arqName, arqSize, this.getDiretorio()));
                    down.start();

                } else {
                    System.out.println("Recebido:" + new String(request.getData()));
                }

                buffer = new byte[1000];
            }
            aSocket.close();
        } catch (SocketException ex) {
            Logger.getLogger(RecebeUDP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecebeUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
