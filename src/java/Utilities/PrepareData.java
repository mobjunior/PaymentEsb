/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Prime
 */
public class PrepareData {

    public String CheckChannelMessage(HashMap FromChannel) {
        String myfields = getparameters("fields");
        StringBuilder Response = new StringBuilder("");
        Set mykeys = FromChannel.keySet();
        String messageFromChannel = mykeys.toString();

        try {
            //System.out.println("Keys: " + Org.keySet());

            myfields = myfields.replace("{", "");
            myfields = myfields.replace("}", "");
            //myfields = myfields.replaceAll("\\s+","");
            String strArray[] = myfields.split(",");
            for (int i = 0; i < strArray.length; i++) {

                if (messageFromChannel.contains(strArray[i])) {

                } else {
                    Response.append(" " + strArray[i]);
                    //System.out.println("Not Found: " + strArray[i]);

                }

            }

        } catch (Exception e) {

        }

        return Response.toString();

    }

    public String CheckStatusMessage(HashMap FromChannel) {
        String myfields = getparameters("statusfields");
        StringBuilder Response = new StringBuilder("");
        Set mykeys = FromChannel.keySet();
        String messageFromChannel = mykeys.toString();

        try {
            //System.out.println("Keys: " + Org.keySet());

            myfields = myfields.replace("{", "");
            myfields = myfields.replace("}", "");
            //myfields = myfields.replaceAll("\\s+","");
            String strArray[] = myfields.split(",");
            for (int i = 0; i < strArray.length; i++) {

                if (messageFromChannel.contains(strArray[i])) {

                } else {
                    Response.append(" " + strArray[i]);
                    //System.out.println("Not Found: " + strArray[i]);

                }

            }

        } catch (Exception e) {

        }

        return Response.toString();

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
}
