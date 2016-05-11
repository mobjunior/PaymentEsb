//package Queus;
//
//import java.util.Properties;
//import javax.jms.Connection;
//import javax.jms.ConnectionFactory;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.MessageConsumer;
//import javax.jms.MessageListener;
//import javax.jms.Queue;
//import javax.jms.QueueSession;
//import javax.jms.Session;
//import javax.jms.TextMessage;
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//
//public class ReadQueue implements MessageListener {
//	public static void main(String[] args) throws NamingException, JMSException {
//		Connection connection = null;
//		try {
//			System.out.println("Create JNDI Context");
//                         InitialContext context = new InitialContext();
//			//Context context = ContextUtil.getInitialContext();
//			
//			System.out.println("Get connection facory");
//			ConnectionFactory connectionFactory = (ConnectionFactory) context
//					.lookup("ConnectionFactory");
//			
//			System.out.println("Create connection");
//			connection = connectionFactory.createConnection();
//			
//			System.out.println("Create session");
//			Session session = connection.createSession(false,
//					QueueSession.AUTO_ACKNOWLEDGE);
//			
//			System.out.println("Lookup queue");
//			Queue queue = (Queue) context.lookup("java:/jms/queue/SERVICE_REQUEST_QUEUE");	
//			
//			System.out.println("Start connection");
//			connection.start();
//			
//			System.out.println("Create consumer");
//			MessageConsumer consumer = session.createConsumer(queue);
//			
//			System.out.println("set message listener");
//			consumer.setMessageListener(new ReadQueue());			
//		} finally {
//			if (connection != null) {
//				System.out.println("close the connection");
//				connection.close();
//			}
//		}
//	}
//
//	@Override
//	public void onMessage(Message message) {
//		try {
//			System.out.println("message received");
//			System.out.println(((TextMessage) message).getText());
//		} catch (JMSException e) {
//			e.printStackTrace();
//		}
//	}
//
//        
//    private static class ContextUtil {
//
//        public static Context getInitialContext() throws NamingException {
//            
//            Properties props = new Properties();
//		props.setProperty("java.naming.factory.initial",
//				"org.jnp.interfaces.NamingContextFactory");
//		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
//		props.setProperty("java.naming.provider.url", "localhost:1099");
//		Context context = new InitialContext(props);
//		return context;
//            
//        }
//    }
//    
//        
//}