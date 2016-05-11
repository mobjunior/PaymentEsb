/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Security;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
/**
 *
 * @author Prime
 */
public class AuthenticateMessage {
    
    
        private static String EncodeMessage(String Message){
            
        String MyEncodedMessage = DatatypeConverter.printBase64Binary(Message.getBytes());
      
        return MyEncodedMessage;
    }
    
    public String GetHash(String MyValue)
    {
        String CheckSum ="";
        try {

            MessageDigest message = MessageDigest.getInstance("SHA-1");  //SHA-512
            message.update(MyValue.getBytes());
            byte byteData[] = message.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            CheckSum=sb.toString();
            System.out.println("Hex format : " + sb.toString());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return CheckSum;
    }
    
}
