/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CódigosProfessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloaderTCP implements Runnable {

    private int porta;
    private String fileName;
    private InetAddress ip;
    private int arqsize;
    private String diretorio;

    public DownloaderTCP(InetAddress ipsocket, int port, String fn, int sArq, String dir) {
        this.porta = port;
        this.fileName = fn;
        this.ip = ipsocket;
        this.arqsize = sArq;
        this.diretorio = dir;
    }

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

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

    public int getArqsize() {
        return arqsize;
    }

    public void setArqsize(int arqsize) {
        this.arqsize = arqsize;
    }

    @Override
    public void run() {
        try {
            Socket aSocket = new Socket(this.getIp(), this.getPorta());
            File arquivo = new File(this.getDiretorio() + this.getFileName());
            InputStream in = new FileInputStream(arquivo);
            OutputStream out = aSocket.getOutputStream();
            byte[] bytes = new byte[16 * 1024];
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            out.close();
            in.close();
            aSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(DownloaderTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
