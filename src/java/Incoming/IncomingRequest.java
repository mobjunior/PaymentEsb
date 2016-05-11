/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Incoming;

import javax.jws.WebService;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.xml.ws.Holder;
//import Payment.CardPayment;
import Utilities.Logs;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.bind.DatatypeConverter;
import sun.misc.BASE64Decoder;
import Security.Utilities;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import MainProcessor.ProcessMessage;
import Utilities.PrepareData;

/**
 *
 * @author Prime
 */
@WebService(serviceName = "Request")
@Stateless()
public class IncomingRequest {

    Logs logs = new Logs();
    String ClientKey = "";
    String Name = "";
    ProcessMessage PostDetails = new ProcessMessage();
    PrepareData CheckMyData = new PrepareData();
//  Decrypt DecryptData = new Decrypt();
//  Encrypt EncryptData = new Encrypt();
    Utilities CalculateSha512 = new Utilities();
    int count;

    public void SendRequest(@WebParam(name = "TranDetails") String TranDetails, @WebParam(name = "AuthKey") String AuthKey,
            @WebParam(name = "TranResponse", mode = WebParam.Mode.OUT) Holder<String> TranResponse
    ) throws ClassNotFoundException, SQLException {
        logs.log("Request ~ " + TranDetails, "", "4");
        //Database MyInsertDetails = new Database();

        HashMap MessageFromChannel = null;
        MessageFromChannel = ConvertToHashMap(TranDetails);

        String checkdata = CheckMyData.CheckChannelMessage(MessageFromChannel);

        if (checkdata.equals("")) {

            boolean access = AuthenticateMessage(MessageFromChannel, AuthKey);
            if (access == false) {
                MessageFromChannel.put("Status", "999");
                MessageFromChannel.put("StatusMessage", "Channel Authentication Failed");

            } else {
                MessageFromChannel.put("organization", Name);
                // MessageFromChannel.put("StatusMessage", "Request Received Successfully");
                HashMap MessageToChannel = PostDetails.PostData(MessageFromChannel);
                MessageFromChannel = MessageToChannel;

            }

        } else {
            MessageFromChannel.put("Status", "009");
            MessageFromChannel.put("StatusMessage", "Missing Parameters: " + checkdata);
        }

        TranResponse.value = MessageFromChannel.toString().toLowerCase();
    }

    private HashMap ConvertToHashMap(String Message) {
        final HashMap hm = new HashMap();
        try {
            Message = Message.toLowerCase();
            Message = Message.replaceAll("\t", "");
            Message = Message.replaceAll("\n", "");
            Message = Message.replaceAll("\r", "");

            Message = Message.replace("{", "");
            Message = Message.replace("}", "");
            // Message = Message.replaceAll("\\s+","");        
            String[] s2 = Message.split(",");
            for (int i = 0; i <= s2.length - 1; i++) {
                String[] s3 = s2[i].split("=");

                try {
                    if (s3[1] == null) {
                        s3[1] = " ";
                    }

                } catch (Exception ex) {
                    s3[1] = null;

                }

                hm.put(s3[0].trim(), (s3[1]));
                // System.out.println("Count: " + i);
            }
            String RefNo = getMessageID(hm.get("country").toString());
            hm.put("refno", RefNo);
        } catch (Exception ex) {
            hm.put("Status", "010");
            hm.put("StatusMessage", "Wrong Message Sent: " + Message);
            logs.log("ConvertToHashMap " + ex, "", "2");
        }

        return hm;

    }

    private String getMessageID(String Mycountry) {  //Remember to generate a random number
        try {
            // String Mycountry = hm.get("field2").toString();
            //String Mycountry = "kenya" ;
            Mycountry = Mycountry.substring(0, 2);

            DateFormat df = new SimpleDateFormat("yyMMdd");
            Date dtDateToday = new Date();
            Random rand = new Random();
            int value = rand.nextInt(1000000);
            String strPaddedNumber = String.format("%06d", value);
            String getM = Mycountry + df.format(dtDateToday) + strPaddedNumber;

            return getM;

        } catch (Exception ex) {
            logs.log("Method: getMessageID, Java Class: IncomingTransaction, Error: " + ex, "", "2");

            return "Error123";
        }

    }

    private String getparameters(String WhatToSearch) {
        String ItemFound = "";
        Properties properties = new Properties();
        try {

            String path = System.getProperty("jboss.server.data.dir");
            path = path + "\\paymentesb.properties";
            properties.load(new FileInputStream(path));
            ItemFound = properties.getProperty(WhatToSearch);         //key  
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return ItemFound;

    }

    private void BreakOrganisation(String Message, String Clientid) {

        try {
            Message = Message.replace("{", "");
            Message = Message.replace("}", "");
            //Message = Message.replaceAll("\\s+","");
            String strArray[] = Message.split(",");
            for (int i = 0; i < strArray.length; i++) {

                String MyOrganizationParts[] = strArray[i].split(":");
                String OrganizationKey = MyOrganizationParts[1];
                String OrganizationName = MyOrganizationParts[2];

                String OrganizationID = MyOrganizationParts[0];
                String ClientDetails[] = OrganizationID.split("=");

                if (ClientDetails[1].contains(Clientid)) {   //needs to find a better way of doing this

                    String MyControlKey[] = OrganizationKey.split("=");
                    ClientKey = MyControlKey[1];

                    String MyControlName[] = OrganizationName.split("=");
                    Name = MyControlName[1];
                }

            }

        } catch (Exception e) {
            logs.log("BreakOrganisation" + e, "", "2");
        }

    }

    private boolean AuthenticateMessage(HashMap Data, String Authkey) {
        boolean access = false;
        try {
            String MyAccessKey = getparameters("organizationKeys");
            BreakOrganisation(MyAccessKey, Data.get("organizationid").toString());
            String key = ClientKey + Data.get("organizationid").toString() + Data.get("tranid").toString();
            String Sha512Key = CalculateSha512.GetSha512((EncodeMessage(key)));

            access = Authkey.equals(Sha512Key);
        } catch (Exception ex) {
            logs.log("AuthenticateMessage ~ " + ex, Data.get("refno").toString(), "2");
            access = false;
        }
        return access;
    }

    private String DecodeMessage(String Message) {
        String MyDecodedMessage = "";
        BASE64Decoder decoder = new BASE64Decoder();
        //BASE64Encoder encoder = new BASE64Encoder();
        try {
            String encodedBytes = Message;
            byte[] decodedBytes = decoder.decodeBuffer(encodedBytes);

            MyDecodedMessage = new String(decodedBytes);
        } catch (Exception e) {
            logs.log("DecodeMessage - Incoming Transaction " + e, "", "2");
            //e.printStackTrace();
        }
        return MyDecodedMessage;
    }

    private static String EncodeMessage(String Message) {
        String MyEncodedMessage = "";
        try {
            MyEncodedMessage = DatatypeConverter.printBase64Binary(Message.getBytes());
            return MyEncodedMessage;
        } catch (Exception ex) {

        }
        return MyEncodedMessage;
    }

    public void CheckStatus(@WebParam(name = "TranDetails") String TranDetails, @WebParam(name = "AuthKey") String AuthKey,
            @WebParam(name = "TranResponse", mode = WebParam.Mode.OUT) Holder<String> TranResponse
    ) throws ClassNotFoundException, SQLException {
        logs.log("Request ~ " + TranDetails, "", "4");
        //Database MyInsertDetails = new Database();

        HashMap MessageFromChannel = null;
        MessageFromChannel = ConvertToHashMap(TranDetails);

        String checkdata = CheckMyData.CheckStatusMessage(MessageFromChannel);

        if (checkdata.equals("")) {

            boolean access = AuthenticateMessage(MessageFromChannel, AuthKey);
            if (access == false) {
                MessageFromChannel.put("Status", "999");
                MessageFromChannel.put("StatusMessage", "Channel Authentication Failed");

            } else {
                MessageFromChannel.put("Status", "000");
                MessageFromChannel.put("StatusMessage", "Received Successfully");
                MessageFromChannel.put("TransactionStatus", "Request awaiting processing by Biller");
                // MessageFromChannel.put("organization", Name);
                // MessageFromChannel.put("StatusMessage", "Request Received Successfully");
                //  HashMap MessageToChannel = PostDetails.PostData(MessageFromChannel);
                // MessageFromChannel=MessageToChannel;

            }

        } else {
            MessageFromChannel.put("Status", "009");
            MessageFromChannel.put("StatusMessage", "Missing Parameters: " + checkdata);
        }

        TranResponse.value = MessageFromChannel.toString().toLowerCase();
    }

}
