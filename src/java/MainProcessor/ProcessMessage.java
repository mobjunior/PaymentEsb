/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainProcessor;

import java.util.HashMap;
import Utilities.Logs;
import Queus.WriteQueue;

/**
 *
 * @author Prime
 */
public class ProcessMessage {

    Logs logs = new Logs();
    WriteQueue MyWriteQ = new WriteQueue();

    public HashMap PostData(HashMap FromChannel) {

        HashMap MessageToChannel = null;
        MessageToChannel = FromChannel;
        switch (FromChannel.get("messagetype").toString().trim()) {
            case "0200":
                if ("000100".equals(FromChannel.get("processingcode").toString().trim())) {

                    try {
                        boolean confirmwrite = MyWriteQ.writeToQ(FromChannel);
                        if (confirmwrite == true) {
                            MessageToChannel.put("Status", "000");
                            MessageToChannel.put("StatusMessage", "Message Delivered Successfully");
                        } else {
                            MessageToChannel.put("Status", "998");
                            MessageToChannel.put("StatusMessage", "Ops!! The system has encountered an error, your request has been rejected. Please contact admin stating refno: " + MessageToChannel.get("refno").toString().trim());

                        }

                    } catch (Exception ex) {
                        logs.log("Method: PostData, Java Class: ProcessMessage, Error: " + ex, MessageToChannel.get("refno").toString(), "2");

                    }

                }
                else{
                    MessageToChannel.put("status", "004");
                    MessageToChannel.put("StatusMessage", "Wrong processingcode passed");
                }
                break;

            case "0230":

                break;
            default:
                MessageToChannel.put("Status", "009");
                MessageToChannel.put("StatusMessage", "Wrong Messagetype passed: ");
                break;

        }

        return MessageToChannel;

    }

}
