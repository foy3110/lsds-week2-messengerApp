import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Gui {
    ServerSocket serverSocket;
    Socket clientSocket;
    BufferedReader into;
    PrintWriter out;
    Scanner sc = new Scanner(System.in);

    JFrame f= new JFrame("Server Messenger");
    JTextField inp1;
    JTextArea out1;

    JButton bt1;
    JButton bt2;

    Gui() throws IOException {

        inp1=new JTextField("Enter Message.");
        inp1.setBounds(50,10, 200,30);

        out1 =new JTextArea("Messages");
        out1.setBounds(50,50, 200,240);
        out1.setEditable(false);

        bt1 = new JButton(new AbstractAction("Send") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("btn1 working");
                Thread sender = new Thread(new Runnable() {
                    String msg;
                    @Override
                    public void run() {
                        msg = inp1.getText();
                        out1.setText(out1.getText()+"\n"+"Server: " + msg);

                        out.println(msg);
                        out.flush();

                    }

                });
                sender.start();
            }
        });

        bt1.setBounds(55,300,75,30);

        bt2 = new JButton(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("exit");
                System.exit(0);

            }
        });
        bt2.setBounds(175,300,75,30);

        f.add(inp1); f.add(out1); f.add(bt1); f.add(bt2);
        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);

        serverStart();

    }


    void serverStart(){
        try {
            serverSocket = new ServerSocket(4999);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream());
            into = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread recieve = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try{
                        msg = into.readLine();
                        while (msg!=null){
                            System.out.println("client: "+msg);
                            out1.setText(out1.getText()+"\n"+"client: "+ msg);
                            msg = into.readLine();
                        }
                        System.out.println("client exited");

                        out.close();
                        clientSocket.close();
                        serverSocket.close();

                        System.exit(0);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            });
            recieve.start();

        }catch (IOException e ){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {

        new Gui();
    }
}