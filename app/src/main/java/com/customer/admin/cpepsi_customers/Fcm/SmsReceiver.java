package com.customer.admin.cpepsi_customers.Fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by raghav on 12-06-2019.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

         Object[] pdus = new Object[0];
        if (data != null) {
            pdus = (Object[]) data.get("pdus"); // the pdus key will contain the newly received SMS
        }

//        for(int i=0;i<pdus.length;i++){
//            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
//
//            String sender = smsMessage.getDisplayOriginatingAddress();
//            //You must check here if the sender is your provider and not another one with same text.
//
//            String messageBody = smsMessage.getMessageBody();
//
//            //Pass on the text to our listener.
//            mListener.messageReceived(messageBody);
//        }

        if (pdus != null) {
            for (Object pdu : pdus) { // loop through and pick up the SMS of interest
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                // your custom logic to filter and extract the OTP from relevant SMS - with regex or any other way.

                String messageBody = smsMessage.getMessageBody();
                if (mListener != null)
                    mListener.messageReceived(messageBody);
                break;
            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

    public static void unbindListener() {
        mListener = null;
    }
}
