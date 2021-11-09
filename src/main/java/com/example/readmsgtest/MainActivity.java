package com.example.readmsgtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    String msgData = "";
    ListView lv;
    ArrayList<String> lst=new ArrayList<>();
    ArrayAdapter<String>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=findViewById(R.id.msz);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
        }
        else {
            msgData = getAllSms(this);

        }
    }
    public String getAllSms(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        String s="";
        if (c != null) {
            totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    Date dateFormat= new Date(Long.valueOf(smsDate));
                    String type;
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            if(number.length() == 10|| number.length() == 13) {
                                s = s + "NULL message";
                            }
                            else{

                                if (body.contains("RS") || body.contains("Rs") || body.contains("rs") || body.contains("INR") || body.contains("Inr")) {

                                    if (body.contains("A/C ") || body.contains("A/c ") || body.contains("Ac ") || body.contains("a/c ") || body.contains("ac ") || body.contains("A/C") || body.contains("A/c") || body.contains("Ac") || body.contains("a/c") || body.contains("ac") || body.contains("curr o/s")) {

                                        if (body.contains("credited ") || body.contains("debited ") || body.contains("Paid ") || body.contains("paid ") || body.contains("credited") || body.contains("debited") || body.contains("Avail") || body.contains("Avail ") || body.contains("spent") || body.contains("spent ")) {
                                            lst.add(dateFormat + number + body);
                                            s = s + dateFormat + number + body;
                                        }
                                        //cred wale ka else
                                        else {
                                            s = s + "NULL message";
                                        }
                                    }
                                    //ac wale ka else
                                    else {
                                        s = s + "NULL message";
                                    }
                                }
                                //rs wale ka else
                                else {
                                    s = s + "NULL message";
                                }
                                // number ki length wale ka else
                            }

                            break;
//                        case Telephony.Sms.MESSAGE_TYPE_SENT:
//                            type = "sent";
//                            break;
//                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
//                            type = "outbox";
//                            break;
                        default:
                            break;
                    }


                    c.moveToNext();
                }
            }
            Toast.makeText(context, ""+s, Toast.LENGTH_SHORT).show();
            c.close();

        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
        }
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, lst);

        lv.setAdapter(adapter);
        return s;
    }



}