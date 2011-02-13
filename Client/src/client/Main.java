package client;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.hibernate.validator.util.GetClassLoader;

import consumer.JMSManager;
import ejb_cl_pkg.LocalServer;

public class Main {

	static InputStream clientStream;
	
	public Main() throws Exception{
		
		clientStream =  getClass().getResource("/client/resources/CLIENTS.xml").openStream();
	}
    public static void main(String[] args) throws Exception {

    	new Main();
    	
        GuiFrame gg = new GuiFrame();
        
        XMLDecoder decoder = new XMLDecoder(clientStream);

        SystemConfig config = (SystemConfig) decoder.readObject();

        ArrayList<ClientBL> c = new ArrayList<ClientBL>(config.getNumClient());
        ArrayList<monitor> m = gg.getMonitorArrayList();

        ClientConfig tmp;

        for (int i = 0; i < config.getNumClient(); i++) {
            tmp = (ClientConfig) decoder.readObject();
            c.add(new ClientBL(""+i,config.getIpRouter(),tmp.getLatitude(),tmp.getLongitude()));
            m.get(i).setClientId("" + i);
            c.get(i).addObserver(m.get(i));
            new Thread(c.get(i)).start();    
        }
        
        gg.setVisible(true);

        JMSManager ms = new JMSManager(new LocalServer("5.67.58.136"));
        ms.AdvCons.addObserver(m.get(0));
    }
}
