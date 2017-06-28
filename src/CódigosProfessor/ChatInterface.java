package CódigosProfessor;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author kevin
 */
public class ChatInterface {
    private JTextArea text;

    public void appendText(String msg) {
        text.append(msg);
        text.append("\r\n");
    }

    public void prepareFrame() {
        JFrame f = new JFrame();
        f.setTitle("Mensagens recebidas");
        text = new JTextArea(20, 40);
        text.setBackground(Color.GRAY);
        text.setEnabled(false);
        text.setFont(new Font("Arial", Font.BOLD, 12));
        text.setBounds(0, 0, 300, 300);

        f.add(text);
        f.setVisible(true);
        f.setSize(300, 300);

        appendText("[Use o terminal para digitar]");
    }    

    public ChatInterface(JTextArea text) {
        this.text = text;
        prepareFrame();
    }
    
    /*
    package ChatProcess;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ChatProcess {

    private JTextArea text;

    public void appendText(String msg) {
        text.append(msg);
        text.append("\r\n");
    }

    public void prepareFrame() {
        JFrame f = new JFrame();
        f.setTitle("Mensagens recebidas");
        text = new JTextArea(20, 40);
        text.setBackground(Color.GRAY);
        text.setEnabled(false);
        text.setFont(new Font("Arial", Font.BOLD, 12));
        text.setBounds(0, 0, 300, 300);

        f.add(text);
        f.setVisible(true);
        f.setSize(300, 300);

        appendText("[Use o terminal para digitar]");
    }

    public ChatProcess(Socket socket) {
        prepareFrame();

        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream()));

            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        String msgRead;
                        try {
                            msgRead = reader.readLine().trim();
                        } catch (IOException e) {
                            msgRead = e.getMessage();
                        }

                        appendText(msgRead);
                        //System.out.print("<<");
                        //System.out.println(msgRead);
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
*/
    
}
