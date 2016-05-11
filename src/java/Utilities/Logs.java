
package Utilities;

import java.util.Properties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author Prime
 */
public class Logs {
    String LogLocation ="";
    
    private void getparameters() {

        Properties properties = new Properties();
        try {
       
            String path = System.getProperty("jboss.server.data.dir");
            path = path +"\\paymentesb.properties";
             properties.load(new FileInputStream(path));
             LogLocation = properties.getProperty("logFile");           
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

   
        
    public void log(String details,String uniqueid,String Loglevel){
       // String LogLocation ="D:/Logs";
        getparameters();
        //Log level mappings  1. Transactions 2. Application 3. Database 
       String TypeOfLog = "";     
       if (Loglevel.equals("1")) {
            TypeOfLog = "TransactionErrors";
        } else if (Loglevel.equals("2")) {
            TypeOfLog = "ApplicationErrors";
        } else if (Loglevel.equals("3")) {
            TypeOfLog = "DatabaseErrors";
        } else if (Loglevel.equals("4")) {
            TypeOfLog = "MessageFromChannel";
        } else if (Loglevel.equals("5")) {
            TypeOfLog = "MessageToChannel";
        } else if (Loglevel.equals("6")) {
            TypeOfLog = "MessageToPaymentGateway";
         } else if (Loglevel.equals("7")) {
            TypeOfLog = "MessageFromPaymentGateway";
            } else if (Loglevel.equals("8")) {
            TypeOfLog = "Queue";
           } else if (Loglevel.equals("9")) {
            TypeOfLog = "SMS";
        } else {
            TypeOfLog = "Others";
        }

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String LogDate = formatter.format(today);       
        SimpleDateFormat LogTimeformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String LogTime = LogTimeformatter.format(today);
          File dir = new File(LogLocation + "/" + TypeOfLog + "/" + LogDate);
        BufferedWriter writer = null;
        if (dir.exists()) {         
        } else {
            dir.mkdirs();
        }
        
                try {                    
                    if (uniqueid.equals("")) {
                      String minimum = "5";
           int maximum = 100000;
         String  randomNum = minimum + (int)(Math.random()*maximum); 
         uniqueid = randomNum;  
                    }                   
            String Filename = "/" + uniqueid + ".txt";
            writer = new BufferedWriter(new FileWriter(dir + Filename));
            writer.write(LogTime + " ~ " + details);
        } catch (Exception e) {
            log("Logs: " + e.getMessage(),"","");
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
                 log("Logs: " + e.getMessage(),"","");
            }
        }
    }
     
    
   
    
}
