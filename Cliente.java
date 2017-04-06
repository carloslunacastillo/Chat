

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

	public static void main(String[] args) {
		
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}



 class MarcoCliente extends JFrame {
	
	public MarcoCliente(){
		super("Chat");
                
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
                
                addWindowListener(new EnviaOline());
		}	
	
}

class EnviaOline extends WindowAdapter{
    public void windowOpened(WindowEvent e){
        try{
            
            Socket misockect = new Socket("192.168.0.8",9999);
            PaqueteEnvio datos=new PaqueteEnvio();
            datos.setMensaje("Online");
            ObjectOutputStream paquete_datos = new ObjectOutputStream(misockect.getOutputStream());
            paquete_datos.writeObject(datos);
            misockect.close();
        }catch(Exception ex){
            
        }
    }
    

}

class LaminaMarcoCliente extends JPanel implements Runnable{
	
	public LaminaMarcoCliente(){
                String nick_usuario=JOptionPane.showInputDialog(null,"Nick: ");
		
		ipServer=JOptionPane.showInputDialog(null,"Ingrese Ip del Servidor: ");
                
                JLabel n_nick = new JLabel("Nick: ");
                
                add(n_nick);
                
	        nick=new JLabel(nick_usuario);
                
                add(nick);
                
		JLabel texto=new JLabel(" Online: ");
                
                add(texto);
                
                ip=new JComboBox();
                
                add(ip);
                
                areaChat=new JTextArea(12,20);
		
		add(areaChat);
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");
		
                EnviaTexto miEvento = new EnviaTexto();
                
                miboton.addActionListener(miEvento);
                
		add(miboton);
                
                Thread hilo = new Thread(this);
                
                hilo.start();
		
	}

    @Override
    public void run() {
        
        try{
          ServerSocket miServer = new ServerSocket(9090);
          Socket cliente ;
          PaqueteEnvio paqueteRecibido;
          
          while(true){
              cliente=miServer.accept();
              ObjectInputStream flujoentrada= new ObjectInputStream(cliente.getInputStream());
              paqueteRecibido=(PaqueteEnvio) flujoentrada.readObject();
              if(!paqueteRecibido.getMensaje().equalsIgnoreCase("Online")){
              areaChat.append("\n"+paqueteRecibido.getNick()+": "+paqueteRecibido.getMensaje());
              }else{
              //areaChat.append("\n"+paqueteRecibido.getIps());
              ArrayList <String> ipMenu = new ArrayList <String>();
              ipMenu=paqueteRecibido.getIps();
              ip.removeAllItems();
              for(String z:ipMenu){
                  ip.addItem(z);
              }
              
              }
              }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
	
	
	
	private class EnviaTexto implements ActionListener{
            
            @Override
            public void actionPerformed(ActionEvent e){
               //System.out.println(campo1.getText());
                try {  
                    
                    Socket miSocket = new Socket(ipServer,9999);
                    
                    PaqueteEnvio datos = new PaqueteEnvio();
                    
                    datos.setNick(nick.getText());
                    datos.setIp(ip.getSelectedItem().toString());
                    datos.setMensaje(campo1.getText());
                    
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(miSocket.getOutputStream());
                    
                    flujoSalida.writeObject(datos);
                    miSocket.close();
                   /* DataOutputStream flujoDatos= new DataOutputStream(miSocket.getOutputStream());
                    
                    flujoDatos.writeUTF(campo1.getText());
                    flujoDatos.close();*/
                    
                } catch (IOException ex) {
                    Logger.getLogger(LaminaMarcoCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        }	
		
		
	private JTextField campo1;
        private JComboBox ip;
        private JLabel nick;
	private JTextArea areaChat;
	private JButton miboton;
	private String ipServer;
	
}
