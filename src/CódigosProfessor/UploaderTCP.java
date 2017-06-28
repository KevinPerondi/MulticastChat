/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CódigosProfessor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kevin
 */
public class UploaderTCP implements Runnable {

    private int porta;
    private String fileName;
    private InetAddress ip;
    private InetAddress ipretorno;
    private long arqsize;
    private String diretorio;

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public long getArqsize() {
        return arqsize;
    }

    public void setArqsize(long arqsize) {
        this.arqsize = arqsize;
    }

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public InetAddress getIpretorno() {
        return ipretorno;
    }

    public void setIpretorno(InetAddress ipretorno) {
        this.ipretorno = ipretorno;
    }

    public UploaderTCP(int porta, InetAddress ipz, String fileName, long filesize, String diretorio, InetAddress ipret) throws UnknownHostException {
        this.porta = porta;
        this.fileName = fileName;
        this.ip = ipz;
        this.diretorio = diretorio;
        this.ipretorno = ipret;
        this.arqsize = filesize;
    }

    @Override
    public void run() {
        try {

            ServerSocket sSocket = new ServerSocket(this.getPorta());
            System.out.println("Aguardando conexao...");
            
            Socket clientSocket = sSocket.accept();

            System.out.println("Conectado.");

            InputStream in = clientSocket.getInputStream();
            OutputStream out = new FileOutputStream(this.getFileName());

            byte[] bytes = new byte[16 * 1024];

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            out.close();
            in.close();
            clientSocket.close();
            sSocket.close();

        } catch (IOException ex) {
            Logger.getLogger(UploaderTCP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
