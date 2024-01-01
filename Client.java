import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * Client
 */

 
public class Client extends JFrame{

    Socket socket;
    
    BufferedReader br; //Reading
    PrintWriter out; // Writing
    
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();

    private Font font = new Font("Roboto",Font.PLAIN,20);

    public Client(){
    try{

        System.out.println("Sending request to server");
        socket = new Socket("127.0.0.1",7777);
        System.out.println("connection done");

        br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out= new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();
        startReading();
        // startWriting();

    }catch(Exception e){
        e.printStackTrace();
    }
    }

    public void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
                if(e.getKeyCode()==10){

                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }

            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }
            
        });
    }

    public void createGUI(){
        this.setTitle("Client Chat Window");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        heading.setFont(font);
        heading.setIcon(new ImageIcon("chat.png"));

        //heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        //heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        
        messageArea.setFont(font);
        messageInput.setFont(font);
        //messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
    }

    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("Reader started....");
            try{
            while(true){
                
                String msg = br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"Server terminated the chat");
                    messageInput.setEnabled(false); 
                    socket.close();                    
                    break;
                }

                //System.out.println("Server: "+msg);
                
                messageArea.append("Server: "+msg+"\n");
            
            }
        }catch(Exception e){
           // e.printStackTrace();
           System.out.println("Connection is closed");
        }
        };

        new Thread(r1).start();
    }

    public void startWriting(){
        Runnable r2=()->{
            try{
            while(!socket.isClosed()){
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                    }

                
            }

        }catch(Exception e){
                    //e.printStackTrace();
                    System.out.println("Connection is closed");
        }
        };
        new Thread(r2).start();
    }


    public static void main(String[] args) {
        new Client();
    }
}