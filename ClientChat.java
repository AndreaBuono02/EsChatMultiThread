package clientchat;

import java.awt.Color; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.KeyEvent; 
import java.awt.event.KeyListener; 
import java.io.BufferedReader; 
import java.io.BufferedWriter; 
import java.io.IOException; 
import java.io.InputStream; 
import java.io.InputStreamReader; 
import java.io.OutputStream; 
import java.io.OutputStreamWriter; 
import java.io.Writer; 
import java.net.Socket; 
import javax.swing.*;

public class ClientChat extends JFrame implements ActionListener, KeyListener{

    private static final long serialVersionUID = 1L; 
    private JTextArea text; 
    private JTextField txtMsg; 
    private JButton btnSend; 
    private JButton btnLogout; 
    private JLabel lblHistorico; 
    private JLabel lblMsg; 
    private JPanel pnlContent; 
    private Socket socket; 
    private OutputStream ou ; 
    private Writer ouw;
    private BufferedWriter bfw;
    private JTextField txtIP; 
    private JTextField txtPort; 
    private JTextField txtName;

    public ClientChat() throws IOException
    { 
        JLabel lblMessage = new JLabel("Convalidazione"); 
        txtIP = new JTextField("127.0.0.1");
        txtPort = new JTextField("12345");
        txtName = new JTextField("Client"); 
        Object[] texts = {lblMessage, txtIP, txtPort, txtName }; 
        JOptionPane.showMessageDialog(null, texts);
        pnlContent = new JPanel(); 
        text = new JTextArea(10,20);
        text.setEditable(false); 
        text.setBackground(new Color(240,240,240)); 
        txtMsg = new JTextField(20); 
        lblHistorico = new JLabel("Cronologia"); 
        lblMsg = new JLabel("Messaggio"); 
        btnSend = new JButton("Invia"); 
        btnSend.setToolTipText("Invia messaggio"); 
        btnLogout = new JButton("Logout");
        btnLogout.setToolTipText("Logout of Chat"); 
        btnSend.addActionListener(this); 
        btnLogout.addActionListener(this);
        btnSend.addKeyListener(this); 
        txtMsg.addKeyListener(this); 
        JScrollPane scroll = new JScrollPane(text);
        text.setLineWrap(true);
        pnlContent.add(lblHistorico);
        pnlContent.add(scroll); 
        pnlContent.add(lblMsg); 
        pnlContent.add(txtMsg); 
        pnlContent.add(btnLogout);
        pnlContent.add(btnSend); 
        pnlContent.setBackground(Color.LIGHT_GRAY); 
        text.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE)); 
        txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        setTitle(txtName.getText()); setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false); setSize(250,300); setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
    }

    public void connect() throws IOException
    { 
        socket = new Socket(txtIP.getText(),Integer.parseInt(txtPort.getText())); 
        ou = socket.getOutputStream(); 
        ouw = new OutputStreamWriter(ou); 
        bfw = new BufferedWriter(ouw); 
        bfw.write(txtName.getText()+"\r\n"); 
        bfw.flush(); 
    }
    
    public void sendMessage(String msg) throws IOException
    { 
        if(msg.equals("Logout"))
        { 
            bfw.write("Disconnesso \r\n"); 
            text.append("Diconnesso \r\n"); 
        }
        else
        { 
            bfw.write(msg+"\r\n"); 
            text.append( txtName.getText() + " <tu> -> " + txtMsg.getText()+"\r\n");
        } 
        bfw.flush();
        txtMsg.setText(""); 
    }


    public void listen() throws IOException
    { 
        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in); 
        BufferedReader bfr = new BufferedReader(inr);
        String msg = ""; 
        while(!"Logout".equalsIgnoreCase(msg)) {
            if(bfr.ready()){
                msg = bfr.readLine(); 
                if(msg.equals("Logout")) 
                {
                    text.append("Server out! \r\n");
                } 
                else
                {
                    text.append(msg+"\r\n");
                } 
            } 
        }
    }

    public void exit() throws IOException
    {
        sendMessage("Logout");
        bfw.close(); 
        ouw.close(); 
        ou.close(); 
        socket.close(); 
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    { 
        try 
        {
            if(e.getActionCommand().equals(btnSend.getActionCommand()))
            { 
                sendMessage(txtMsg.getText());
            }
            else if(e.getActionCommand().equals(btnLogout.getActionCommand())) 
            {
                exit();
            } 
        } catch (IOException e1) 
        { 
            e1.printStackTrace(); 
        } 
    }
    
    @Override 
    public void keyPressed(KeyEvent e) 
    { 
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        { 
            try { sendMessage(txtMsg.getText());
            }
            catch (IOException e1) 
            { 
               e1.printStackTrace(); 
            } 
        } 
    } 
    
    @Override 
    public void keyReleased(KeyEvent arg0) 
    { 
    }
    
    @Override 
    public void keyTyped(KeyEvent arg0) 
    {    
    }

    public static void main(String[] args) throws IOException {
        ClientChat app = new ClientChat();
        app.connect();
        app.listen();
    }

}
