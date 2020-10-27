package serverchat2.pkg0;

import java.io.BufferedReader; 
import java.io.BufferedWriter; 
import java.io.IOException; 
import java.io.InputStream; 
import java.io.InputStreamReader; 
import java.io.OutputStream; 
import java.io.OutputStreamWriter; 
import java.io.Writer; 
import java.net.ConnectException;
import java.net.ServerSocket; 
import java.net.Socket; 
import java.util.ArrayList; 
import javax.swing.JLabel; 
import javax.swing.JOptionPane; 
import javax.swing.JTextField;


public class ServerChat20 extends Thread{

    private static ArrayList<BufferedWriter> clients; 
    private static ServerSocket server; 
    private String nome; 
    private Socket con; 
    private InputStream in; 
    private InputStreamReader inr; 
    private BufferedReader bfr;

    
    public ServerChat20(Socket con)
    {   
        this.con = con; 
        try { 
            in = con.getInputStream(); 
            inr = new InputStreamReader(in); 
            bfr = new BufferedReader(inr); 
        } 
        catch (IOException e) 
        { 
            e.printStackTrace(); 
        }
    }
    
    public void run()
    { 
        try{ 
            String msg; 
            OutputStream ou = this.con.getOutputStream(); 
            Writer ouw = new OutputStreamWriter(ou); 
            BufferedWriter bfw = new BufferedWriter(ouw); 
            clients.add(bfw); 
            nome = msg = bfr.readLine(); 
            while(!"Logout".equalsIgnoreCase(msg) && msg != null) 
            { 
                msg = bfr.readLine(); sendToAll(bfw, msg); 
                System.out.println(msg); 
            } 
        }catch (Exception e) 
        { 
            e.printStackTrace(); 
        }
    }

    public void sendToAll(BufferedWriter bwOutput, String msg) throws IOException 
    { 
        BufferedWriter bwS; 
        for(BufferedWriter bw : clients)
        { 
            bwS = (BufferedWriter)bw; 
            if(!(bwOutput == bwS))
            { 
                bw.write(nome + " -> " + msg+"\r\n"); 
                bw.flush(); 
            } 
        } 
    }

    public static void main(String[] args) {
        try{ 
            JLabel lblMessage = new JLabel("Server Port:"); 
            JTextField txtPort = new JTextField("12345"); 
            Object[] texts = {lblMessage, txtPort }; 
            JOptionPane.showMessageDialog(null, texts); 
            server = new ServerSocket(Integer.parseInt(txtPort.getText())); 
            clients = new ArrayList<BufferedWriter>(); JOptionPane.showMessageDialog(null,"Server attivo nella porta: "+ txtPort.getText()); 
            while(true)
            { 
                System.out.println("Attendendo una connessione..."); 
                Socket con = server.accept(); 
                System.out.println("Client connesso ..."); 
                
                Thread t = new ServerChat20(con); 
                t.start(); 
            } 
        }catch (Exception e) 
        { 
            e.printStackTrace(); 
        } 
    }
}
