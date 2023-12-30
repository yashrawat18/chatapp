import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;


class Server{

    ServerSocket server;
    Socket socket;

    BufferedReader br; //Reading
    PrintWriter out; // Writing

    public Server(){
        try{
        server = new ServerSocket(7777);
        System.out.println("Server is ready to accept connection");
        System.out.println("waiting...");
        socket = server.accept(); 
        
        br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out= new PrintWriter(socket.getOutputStream());

        startReading();
        startWriting();
    }catch(Exception e){
        e.printStackTrace();

    }
    }

    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("Reader started....");
            try{
            while(true){
               
                String msg = br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Client terminated the chat");
                    socket.close();
                    break;
                }

                System.out.println("client: "+msg);
            
            }
        }catch(Exception e){
            //e.printStackTrace();
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
            System.out.println("Connection closed");
        }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        new Server();
    }
}