package com.example.b00063271.safesplit;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SafeSplitApp extends Application {

    static ArrayList<HashMap<String,String>> contactData;

    @Override
    public void onCreate() {
        super.onCreate();

        // CONTACT RETRIEVAL ----------------------------------------------
        new DownloadContacts().execute();
    }

    class DownloadContacts extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params){
            contactData = new ArrayList<HashMap<String,String>>();
            ContentResolver cr = getContentResolver();
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
            while (cursor.moveToNext()) {
                try{
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor phones = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
                        while (phones.moveToNext()) {
                            String phoneNumber = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
                            HashMap<String,String> map=new HashMap<String,String>();
                            map.put("name", name);
                            map.put("number", phoneNumber);
                            contactData.add(map);
                        }
                        phones.close();
                    }
                }catch(Exception e){}
            }
            return "Downloaded";
        }
        @Override
        protected void onPostExecute(String result){
            Collections.sort(contactData, new MapComparator("name"));
        }
    }

    class MapComparator implements Comparator<Map<String, String>> {
        private final String key;

        public MapComparator(String key){
            this.key = key;
        }

        @Override
        public int compare(Map<String, String> first, Map<String, String> second){
            String firstValue = first.get(key);
            String secondValue = second.get(key);
            return firstValue.compareTo(secondValue);
        }
    }



}
