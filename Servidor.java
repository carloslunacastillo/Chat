

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}



 class MarcoServidor extends JFrame implements Runnable {
	
	public MarcoServidor(){
		
		setBounds(1200,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
                
                Thread hilo = new Thread(this);
                
                hilo.start();
		
		}
	
	private	JTextArea areatexto;

    @Override
    public void run() {
            try {
                //System.out.println("estoy a la escucha");
                ServerSocket server = new ServerSocket(9999);
                 String nick,ip,mensaje;
                 ArrayList <String> listaIp=new ArrayList<String>();
                 PaqueteEnvio paquete_recibido;
                while(true){
                Socket miSocket = server.accept();
                
               
               
               
                ObjectInputStream flujoEntrada=new ObjectInputStream(miSocket.getInputStream());
                    
                paquete_recibido=(PaqueteEnvio) flujoEntrada.readObject();
                nick=paquete_recibido.getNick();
                ip=paquete_recibido.getIp();
                mensaje=paquete_recibido.getMensaje();
                
                if(!mensaje.equalsIgnoreCase("Online")){
                areatexto.append("\n"+nick+": "+mensaje+" para "+ip);
                 
                }else{
                 //-----------ONLINE-----
                InetAddress localizacion=miSocket.getInetAddress();
                
                String ipRemota = localizacion.getHostAddress();
                
                System.out.println("Online: "+ipRemota);
                
                listaIp.add(ipRemota);
                
                paquete_recibido.setIps(listaIp);
                
                for(String z:listaIp){
                Socket enviaDestinatario = new Socket (z,9090);
                ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
                paqueteReenvio.writeObject(paquete_recibido);
                
                paqueteReenvio.close();
                        
                enviaDestinatario.close();
                
                miSocket.close();
                }
                //--------------------------------------------    
                }
                }
            } catch (IOException  | ClassNotFoundException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
