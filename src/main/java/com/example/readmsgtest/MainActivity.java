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
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    String msgData = "";
    ListView lv;
    static boolean isdebited = false;
    static String m="";

    static String amts="";
    static String availAmt="";
    static String amtsfordisplay="";
    static String availAmtfordisplay="";
    //ye bhi add krna
    Spinner month;
    Spinner year;

    ArrayAdapter<String>adapter;
    ArrayList<String> lst1=new ArrayList<>();
    ArrayAdapter<String>adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        lv=findViewById(R.id.msz);
//        new code
        month=findViewById(R.id.month);
        year=findViewById(R.id.year);
        ArrayList<String>years=new ArrayList<>();
        years.add("2021");
        years.add("2020");
        years.add("2019");

        ArrayList<String> months = new ArrayList<>();
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(arrayAdapter);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String monthname = parent.getItemAtPosition(position).toString();
                //              Toast.makeText(parent.getContext(), "Selected: " + monthname,          Toast.LENGTH_LONG).show();

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
                }
                else {
                    msgData = getAllSms(MainActivity.this,month.getSelectedItem().toString(),year.getSelectedItem().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> yeararrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        yeararrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yeararrayAdapter);
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String yearname = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), "Selected: " + yearname,Toast.LENGTH_LONG).show();

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
                }
                else {
                    msgData = getAllSms(MainActivity.this,month.getSelectedItem().toString(),year.getSelectedItem().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//yha tk h
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
        }
        else {
            msgData = getAllSms(this,month.getSelectedItem().toString(),year.getSelectedItem().toString());
        }


    }

    public String getAllSms(Context context,String month,String year) {
        // Toast.makeText(context, ""+year+month, Toast.LENGTH_SHORT).show();
        ArrayList<String> lst=new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        int count =0;
        String s="";
        String ban="";

        if (c != null) {
            totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));

                    //body ko lowercase me ynha kra
                    body = body.toLowerCase();
                    System.out.println(body);
                    //ho gyi
                    m = m + body;

                    Date dateFormat= new Date(Long.valueOf(smsDate));
                    long millisecond = Long.parseLong(smsDate);

                    //new code
                    String dateString = DateFormat.format("dd/MMM/yyyy", new Date(millisecond)).toString();
                    String type;

                    String splitedate[]=dateString.split("/");

                    //yha tk
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

                    //if(splitedate[1].trim().equals(month)&&splitedate[2].trim().equals(year)){
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            if(splitedate[1].trim().equals(month)&&splitedate[2].trim().equals(year)) {
                                //Pattern regEx = Pattern.compile("(?:inr|rs)+[\\s][0-9+[\\,]+[0-9]]+[\\.][0-9]+");
                                Pattern regEx = Pattern.compile("(?=.[Aa]ccount.|.[Aa]/[Cc].|.[Aa][Cc][Cc][Tt].|.[Cc][Aa][Rr][Dd].)(?=.[Cc]redit.|.[Dd]ebit.)(?=.[Ii][Nn][Rr].|.[Rr][Ss].)");
                                Matcher m = regEx.matcher(body);
                                if (m.find()) {
                                    if (body.contains("debited")) {
                                        String amt[] = {};
                                        String avail[] = {};
                                        if (body.contains("inr") && body.contains("bal")) {
                                            amt = body.split("inr", 2);
                                            //ye agar balance inr me h to
                                            if (amt[1].contains("inr")) {
                                                avail = amt[1].split("inr", 2);
                                            }
                                            //ye agar balance rs me h to
                                            else if (amt[1].contains("rs")) {
                                                avail = amt[1].split("rs", 2);
                                            }
                                        } else if (body.contains("rs") && body.contains("bal")) {
                                            amt = body.split("rs", 2);
                                            if (amt[1].contains("inr")) {
                                                avail = amt[1].split("inr", 2);
                                            } else if (amt[1].contains("rs")) {
                                                avail = amt[1].split("rs", 2);
                                            }
                                        }
                                        //ye for debited wale ka
                                        for (int i = 0; i < amt[1].length(); i++) {
                                            if (amt[1].charAt(i) >= 48 && amt[1].charAt(i) <= 57 || amt[1].charAt(i) == ' ' || amt[1].charAt(i) == ',' || amt[1].charAt(i) == '.') {
                                                if (amt[1].charAt(i) == ',') {
                                                    amtsfordisplay = amtsfordisplay + amt[1].charAt(i);
                                                } else {
                                                    amts = amts + amt[1].charAt(i);
                                                    amtsfordisplay = amtsfordisplay + amt[1].charAt(i);
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        amts = "\ndebited amount" + amts;
                                        //ye for available wale ka
                                        for (int i = 0; i < avail[1].length(); i++) {
                                            if (avail[1].charAt(i) >= 48 && avail[1].charAt(i) <= 57 || avail[1].charAt(i) == ' ' || avail[1].charAt(i) == ',' || avail[1].charAt(i) == '.') {
                                                if (avail[1].charAt(i) == ',') {
                                                    availAmtfordisplay = availAmtfordisplay + avail[1].charAt(i);
                                                } else {
                                                    availAmt = availAmt + avail[1].charAt(i);
                                                    availAmtfordisplay = availAmtfordisplay + avail[1].charAt(i);
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        //ye akhri wala dot htane k liye
                                        availAmt = availAmt.substring(0, availAmt.length() - 2);
                                        //ye available amount print krne k liye
                                        availAmt = "\navailable amount" + availAmt;
                                    }
                                    //abhi credited ka
                                    else if (body.contains("credited")) {
                                        String amt[] = {};
                                        String avail[] = {};
                                        if (body.contains("inr") && body.contains("bal")) {
                                            amt = body.split("inr", 2);
                                            //ye agar balance inr me h to
                                            if (amt[1].contains("inr")) {
                                                avail = amt[1].split("inr", 2);
                                            }
                                            //ye agar balance rs me h to
                                            else if (amt[1].contains("rs")) {
                                                avail = amt[1].split("rs", 2);
                                            }
                                        } else if (body.contains("rs") && body.contains("bal")) {
                                            amt = body.split("rs", 2);
                                            if (amt[1].contains("inr")) {
                                                avail = amt[1].split("inr", 2);
                                            } else if (amt[1].contains("rs")) {
                                                avail = amt[1].split("rs", 2);
                                            }
                                        }
                                        //ye for credited wale ka h
                                        for (int i = 0; i < amt[1].length(); i++) {
                                            if (amt[1].charAt(i) >= 48 && amt[1].charAt(i) <= 57 || amt[1].charAt(i) == ' ' || amt[1].charAt(i) == ',' || amt[1].charAt(i) == '.') {
                                                if (amt[1].charAt(i) == ',') {
                                                    amtsfordisplay = amtsfordisplay + amt[1].charAt(i);
                                                } else {
                                                    amts = amts + amt[1].charAt(i);
                                                    amtsfordisplay = amtsfordisplay + amt[1].charAt(i);
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        amts = "\ncredited amount" + amts;
                                        //ye for available ka h
                                        for (int i = 0; i < avail[1].length(); i++) {
                                            if (avail[1].charAt(i) >= 48 && avail[1].charAt(i) <= 57 || avail[1].charAt(i) == ' ' || avail[1].charAt(i) == ',' || avail[1].charAt(i) == '.') {
                                                if (avail[1].charAt(i) == ',') {
                                                    availAmtfordisplay = availAmtfordisplay + avail[1].charAt(i);
                                                } else {
                                                    availAmt = availAmt + avail[1].charAt(i);
                                                    availAmtfordisplay = availAmtfordisplay + avail[1].charAt(i);
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        //ye akhri wala dot htane k liye
                                        availAmt = availAmt.substring(0, availAmt.length() - 2);
                                        //ye available amount print krne k liye
                                        availAmt = "\navailable amount" + availAmt;
                                    }
                                    // is wali se sab kuch list me add ho rha h
                                    lst.add(dateString + number + body + amts + availAmt);
                                    //ye value print krne k baad unko null kr rha h
                                    amts = "";
                                    availAmt = "";
                                }
                            }

//                            if(number.length() == 10|| number.length() == 13) {
//                                s = s + "NULL message";
//                            }
//                            else{
//
//                                //if (body.contains("RS") || body.contains("Rs") || body.contains("rs") || body.contains("INR") || body.contains("Inr"))
//                                if (body.contains(" rs") || body.contains(" rs ") || body.contains("rs") || body.contains(" rs.") || body.contains(" inr") || body.contains(" inr ")) {
//
//                                    if (body.contains("a/c ") || body.contains("ac ") || body.contains(" a/c")|| body.contains(" ac") || body.contains("a/c") || body.contains(" ac ") || body.contains("curr o/s")) {
//
//                                        if (body.contains("credited ") || body.contains("debited ") || body.contains("paid ") || body.contains(" paid ") || body.contains("credited") || body.contains("debited") || body.contains("avail") || body.contains("avail ") || body.contains("spent") || body.contains("spent ") || body.contains("received") || body.contains("received ") || body.contains(" received ")) {
//
//                                            if(splitedate[1].trim().equals(month)&&splitedate[2].trim().equals(year))
//
//                                                //amount extract karne wala
//                                                //abhi debited ka
//                                                if(body.contains("debited")){
//                                                    String amt[] ={};
//                                                    if(body.contains("inr")) {
//                                                        amt = body.split("inr", 2);
//                                                    }
//                                                    else if(body.contains("rs")) {
//                                                        amt = body.split("rs", 2);
//                                                    }
//                                                    for(int i=0;i<amt[1].length();i++){
//                                                        if(amt[1].charAt(i)>=48&&amt[1].charAt(i)<=57||amt[1].charAt(i)==' '||amt[1].charAt(i)==','||amt[1].charAt(i)=='.'){
//                                                            if(amt[1].charAt(i)==',') {
//                                                                amtsfordisplay = amtsfordisplay+amt[1].charAt(i);
//                                                            }
//                                                            else {
//                                                                amts=amts+ amt[1].charAt(i);
//                                                                amtsfordisplay= amtsfordisplay+ amt[1].charAt(i);
//                                                            }
//                                                        }
//                                                        else {
//                                                            break;
//                                                        }
//                                                    }
//                                                    amts="\ndebited amount" + amts;
//                                                }
//                                                //abhi credited ka
//                                                else if(body.contains("credited")){
//                                                    String amt[] ={};
//                                                    if(body.contains("inr")) {
//                                                        amt = body.split("inr", 2);
//                                                    }
//                                                    else if(body.contains("rs")) {
//                                                        amt = body.split("rs", 2);
//                                                    }
//                                                    for(int i=0;i<amt[1].length();i++){
//                                                        if(amt[1].charAt(i)>=48&&amt[1].charAt(i)<=57||amt[1].charAt(i)==' '||amt[1].charAt(i)==','||amt[1].charAt(i)=='.'){
//                                                            if(amt[1].charAt(i)==',') {
//                                                                amtsfordisplay = amtsfordisplay+amt[1].charAt(i);
//                                                            }
//                                                            else {
//                                                                amts=amts+ amt[1].charAt(i);
//                                                                amtsfordisplay= amtsfordisplay+ amt[1].charAt(i);
//                                                            }
//                                                        }
//                                                        else {
//                                                            break;
//                                                        }
//                                                    }
//                                                    amts ="\ncredited amount" + amts;
//                                                }
                            //ynha se saara amount add hota h
//                                                    lst.add(dateString + number + body + amts);
//                                                s = s + dateString + number + body + amts;
//                                                amts="";
//
//                                            //bank ka naam print krega
//                                            if(number.contains("IDFC")) {
//                                                ban = ban + "IDFC Bank";
//                                            }
//                                            else if(number.contains("SBI")) {
//                                                ban = ban + "SBI Bank";
//                                            }
//                                            else if(number.contains("HDFC")) {
//                                                ban = ban + "HDFC Bank" ;
//                                            }
//                                            else if(number.contains("ICICI")) {
//                                                ban = ban + "ICICI Bank";
//                                            }
//                                            else {
//                                                ban = ban + "NULL Bank";
//                                            }
//                                            // bank ka naam wala khtm
//
//                                            //debit wale transactions
//                                            if (body.contains("debited") || body.contains(" debited ")) {
//                                                count++;
//                                                lst1.add("debited " + count);
//                                            }
//                                        }
//                                        //cred wale ka else
//                                        else {
//                                            s = s + "NULL message";
//                                        }
//                                    }
//                                    //ac wale ka else
//                                    else {
//                                        s = s + "NULL message";
//                                    }
//                                }
//                                //rs wale ka else
//                                else {
//                                    s = s + "NULL message";
//                                }
//                                // number ki length wale ka else
//                            }

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
                //}
            }
            Toast.makeText(context, ""+ban, Toast.LENGTH_LONG).show();
            c.close();

        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
        }
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, lst);
        adapter1 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, lst1);

        lv.setAdapter(adapter);

        return s;
    }


    //mohit ka code
//    public static void complete(String s) {
//        ArrayList<Double> ans = extract(m);
//        //System.out.println(ans);
//
//        for (int i = 0; i < ans.size(); i++) {
//            if (i == 0) {
//                //System.out.println("debited amount is " + ans.get(i));
//                amts = amts + "debited amount is " + ans.get(i);
//            }
//            else if (i == 1) {
//                //System.out.println("remaining amount is " + ans.get(i));
//                amtsfordisplay = amtsfordisplay + "remaining amount is " + ans.get(i);
//            }
//        }
//    }
//
//    public static ArrayList<Double> extract(String s) {
//
//        String[] sarr = s.split(" ");
//
//        isdebited = false;
//
//        ArrayList<Double> results = new ArrayList<>();
//        for(String val:sarr) {
//
//            if(val.equals("debited"))
//                isdebited = true;
//
//            if(val.length()>0)
//                if(val.charAt(0)>=48 && val.charAt(0)<=(48+9))
//                    if(isNumber(val))
//                        results.add(makeNum(val));
//        }
//        return results;
//    }
//
//    public static boolean isNumber(String s) {
//        boolean Num = true;
//        String num = "";
//        double ans = 0.0;
//        for(int i = 0 ; i <s.length();i++)
//        {
//            if(!((s.charAt(i)>=48 && s.charAt(i)<=(48+9)) || s.charAt(i)==',' || s.charAt(i)=='.'))
//            {
//                Num = false;
//                break;
//            }
//        }
//
//        return Num;
//    }
//
//    public static double makeNum(String s){
//        String num = "";
//        for (int i = 0; i < s.length(); i++) {
//            if (s.charAt(i) != ',')
//                num = num + s.charAt(i);
//        }
//
//        double ans = 0.0;
//        if (num.charAt(num.length() - 1) == '.')
//            num = num.substring(0, num.length() - 1);
//
//        ans = (Double.parseDouble(num));
//        return ans;
//    }

}