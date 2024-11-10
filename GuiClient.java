import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GuiClient {
    //server stuffs

    String IpHostName;
    ServerSocket serverSocket;
    Socket clientSocket;
    BufferedReader into;
    PrintWriter out;
    Scanner sc = new Scanner(System.in);

    //gui stuff
    JFrame f= new JFrame("Client Messenger");
    JTextField inp1 ,inpIP;
    JTextArea out1;
    JButton bt1, bt2, btnIP;


    GuiClient() throws IOException {

        inpIP=new JTextField("Enter message.");//creates the first input
        inpIP.setBounds(50,10, 100,30);//sets where the input is

        inp1=new JTextField("Enter message.");//creates the first input
        inp1.setBounds(50,50, 200,30);//sets where the input is

        out1 =new JTextArea("Messages");//creates the text area -- may change to a scrollable
        out1.setBounds(50,90, 200,240);
        out1.setEditable(false);// sets it so it cant be written too if send isnt clicked

        bt1 = new JButton(new AbstractAction("Send") {//creates the button
            @Override
            //function for when button is clicked
            public void actionPerformed(ActionEvent e) {
                Thread sender = new Thread(new Runnable() {// thread for sending a msg to server
                    String msg;
                    @Override
                    public void run() {
                        msg = inp1.getText();// gets the user input
                        out1.setText(out1.getText()+"\n"+"Client: " + msg);

                        out.println(msg);
                        out.flush();

                    }

                });
                sender.start();

            }
        });

        bt1.setBounds(55,350,75,30);

        bt2 = new JButton(new AbstractAction("Exit") {
            //kills the system when pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                //terminates the program
                System.out.println("exit");
                System.exit(0);

            }
        });
        bt2.setBounds(175,350,75,30);

        btnIP = new JButton(new AbstractAction("Enter") {
            @Override
            public void actionPerformed(ActionEvent e) {
                //gets ip and starts server
                IpHostName =inpIP.getText();
                System.out.println(IpHostName);
                inpIP.setEditable(false);//makes input un editable
                f.remove(btnIP);//removes button
                serverStart();

            }
        });
        btnIP.setBounds(170,10,75,30);

        f.add(inp1); f.add(out1); f.add(bt1); f.add(bt2);f.add(inpIP);f.add(btnIP);// adds all the inps/outs/btns to the frame
        f.setSize(325,450);
        f.setLayout(null);
        f.setVisible(true);

    }
    void serverStart() {
        // server function
        // handles connecting the server
        // handles server sending data to the client
        try {
            System.out.println(IpHostName);
            clientSocket = new Socket(IpHostName, 4999);// connects client to server
            out = new PrintWriter(clientSocket.getOutputStream());
            into = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread recieve = new Thread(new Runnable() {
                //recieves data from the server
                String msg;
                @Override
                public void run() {
                    try{
                        msg = into.readLine();// gets the server data
                        while (msg!=null){
                            System.out.println("Server: "+msg);//prints to terminal
                            out1.setText(out1.getText()+"\n"+"Server: "+ msg);//sets the text on the output screen

                            msg = into.readLine();

                        }
                        System.out.println("Client exitted");

                        out.close();
                        clientSocket.close();
                    }
                    catch(IOException e){//catches input output errors incase they fail
                        e.printStackTrace();
                    }
                }
            });
            recieve.start();//starts the recieve function
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        new GuiClient();
    }
}