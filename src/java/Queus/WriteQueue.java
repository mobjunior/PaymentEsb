/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Queus;
import Utilities.Logs;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/**
 *
 * @author Prime
 */



public class WriteQueue {
    Logs logs = new Logs();
//    	public static Context getInitialContext() throws NamingException {
//		Properties props = new Properties();
//		props.setProperty("java.naming.factory.initial",
//				"org.jnp.interfaces.NamingContextFactory");
//		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
//		props.setProperty("java.naming.provider.url", "localhost:1099");
//		Context context = new InitialContext(props);
//		return context;
//	}
//    
    
    public boolean writeToQ(HashMap FromChannel) throws JMSException{
        String MyQueue = getparameters("WriteQueue");
        boolean Verify=false;
        Connection connection = null;
		try {
			//System.out.println("Create JNDI Context");
                        //InitialContext ic = new InitialContext();
                        
                        InitialContext context = new InitialContext();
                        
			//Context context = ContextUtil.getInitialContext();
			
			//System.out.println("Get connection facory");
			ConnectionFactory connectionFactory = (ConnectionFactory) context
					.lookup("ConnectionFactory");
			
			//System.out.println("Create connection");
			connection = connectionFactory.createConnection();
			
			//System.out.println("Create session");
			Session session = connection.createSession(false,
					QueueSession.AUTO_ACKNOWLEDGE);
			
			//System.out.println("Lookup queue");
			Queue queue = (Queue) context.lookup(MyQueue);  //java:/queue/HelloWorldQueue
			
			//System.out.println("Start connection");
			connection.start();
			
			//System.out.println("Create producer");
			MessageProducer producer = session.createProducer(queue);
			
			//System.out.println("Create hello world message");
			Message hellowWorldText = session.createTextMessage(FromChannel.toString());
			
			//System.out.println("Send hello world message");
			producer.send(hellowWorldText);	
		} catch (NamingException ex) {
                    Verify=false;
                     logs.log("writeToQ " + ex, FromChannel.get("refno").toString().trim(), "8");
                   // Logger.getLogger(WriteQueue.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JMSException ex) {
                    Verify=false;
                     logs.log("writeToQ " + ex, FromChannel.get("refno").toString().trim(), "8");
                    //Logger.getLogger(WriteQueue.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
			if (connection != null) {
				//System.out.println("close the connection");
				connection.close();
			}
Verify = true;
		}
        return Verify;

        
    }
    
    
    
               private String getparameters(String WhatToSearch) {
String ItemFound="";
        Properties properties = new Properties();
        try {
       
            String path = System.getProperty("jboss.server.data.dir");
            path = path +"\\paymentesb.properties";
             properties.load(new FileInputStream(path));
             ItemFound = properties.getProperty(WhatToSearch);         
        } catch (IOException ex) {
            System.out.println(ex);
        }
     return ItemFound;

    }

    private static class ContextUtil {

        public static Context getInitialContext() throws NamingException {
            
            Properties props = new Properties();
		props.setProperty("java.naming.factory.initial",
				"org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", "localhost:1099");
		Context context = new InitialContext(props);
		return context;
            
        }
    }
    
}
