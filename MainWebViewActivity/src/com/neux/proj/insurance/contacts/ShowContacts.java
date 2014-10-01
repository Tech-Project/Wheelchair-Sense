package com.neux.proj.insurance.contacts;


import java.util.ArrayList;

import android.content.ContentResolver;
import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONObject;


import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;


public class ShowContacts
{
    private Context mContext;

    public ShowContacts(Context context)
    {
        mContext = context;
    }

    public Cursor GetContacts()
    {
        Cursor cursor = null;
        String[] projection = new String[]{Contacts._ID,Contacts.DISPLAY_NAME};

        cursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, null, null,
                Contacts.DISPLAY_NAME + " ASC");

        return cursor;
    }


    public String GetContactsJSON() throws Exception {

        JSONArray jsonArray = new JSONArray();

        Cursor c = GetContacts();

        if (c.getCount() > 0)
        {

            if (c.moveToFirst())
            {

                do {

                    JSONObject jsonItem = new JSONObject();	// JSON Object


                    String id = c.getString(c.getColumnIndex(Contacts._ID));

                    jsonItem.put("_id",id);
                    jsonItem.put("displayName",c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

                    addPhoneInfo(id, jsonItem,mContext.getContentResolver());
                    addEmailInfo(id, jsonItem,mContext.getContentResolver());
                    addAddrInfo(id,jsonItem,mContext.getContentResolver());
                    addCompanyInfo(id,jsonItem,mContext.getContentResolver());
                    addBirthdayInfo(id,jsonItem,mContext.getContentResolver());

                    jsonArray.put(jsonItem);


                }while(c.moveToNext());
            }
        }
        c.close();

        return jsonArray.toString();
    }

    private void addBirthdayInfo(String id,JSONObject jsonObject,ContentResolver cr) throws Exception {

        String columns[] = {
                ContactsContract.CommonDataKinds.Event.START_DATE,
                ContactsContract.CommonDataKinds.Event.TYPE,
                ContactsContract.CommonDataKinds.Event.MIMETYPE,
        };

        String where = ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = ? and "  + ContactsContract.Data.CONTACT_ID + " = ?";

        String[] selectionArgs = new String[]{ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,id};

        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

        String birthday = "";
        Cursor birthdayCur = cr.query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, sortOrder);
        if (birthdayCur.getCount() > 0) {
            while (birthdayCur.moveToNext()) {
                birthday = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
            }
        }

        jsonObject.put("birthday",birthday);

        birthdayCur.close();
    }

    private void addCompanyInfo(String id,JSONObject jsonItem,ContentResolver cr) throws Exception {
        String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ?";

        String[] orgWhereParams = new String[]{id,
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};

        Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                null, orgWhere, orgWhereParams, null);

        String value = "";
        if (orgCur.moveToFirst()) {
            value = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
        }

        jsonItem.put("companyInfo",value);

        orgCur.close();
    }

    private void addAddrInfo(String id,JSONObject jsonItem,ContentResolver cr) throws Exception {
        String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ?";

        String[] addrWhereParams = new String[]{id,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};

        Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
                null, addrWhere, addrWhereParams, null);

        while(addrCur.moveToNext()) {
            String street = addrCur.getString(addrCur.getColumnIndex(
                    ContactsContract.CommonDataKinds.StructuredPostal.STREET));
            String city = addrCur.getString(addrCur.getColumnIndex(
                    ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            String state = addrCur.getString(addrCur.getColumnIndex(
                    ContactsContract.CommonDataKinds.StructuredPostal.REGION));
            String postalCode = addrCur.getString(addrCur.getColumnIndex(
                    ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
            String country = addrCur.getString(addrCur.getColumnIndex(
                    ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
            int code = Integer.valueOf(addrCur.getString(addrCur.getColumnIndex(
                    ContactsContract.CommonDataKinds.StructuredPostal.TYPE)));

            String value = street+city+state+country+postalCode;

            switch(code){
                case 0:{
                    jsonItem.put("addr_customize", value);
                    break;
                }
                case 1:{
                    jsonItem.put("addr_home", value);
                    break;
                }
                case 2:{
                    jsonItem.put("addr_company", value);
                    break;
                }

            }
        }
        addrCur.close();
    }

    private void addEmailInfo(String id,JSONObject jsonItem,ContentResolver cr) throws Exception {
        Cursor emailCur = cr.query( ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{id}, null);
        while (emailCur.moveToNext()) {
            int code = Integer.valueOf(emailCur.getString(emailCur.getColumnIndex(
                    ContactsContract.CommonDataKinds.Email.TYPE)));

            String value = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));


            switch(code) {
                case 0:{
                    jsonItem.put("email_customize", value);
                    break;
                }
                case 1:{
                    jsonItem.put("email_home", value);
                    break;
                }
                case 2:{
                    jsonItem.put("email_company", value);
                    break;
                }
                case 3:{
                    jsonItem.put("email_other", value);
                    break;
                }
                case 4:{
                    jsonItem.put("email_mobile", value);
                    break;
                }
            }
        }
        emailCur.close();
    }

    private void addPhoneInfo(String id,JSONObject jsonItem,ContentResolver cr) throws Exception {
        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{id}, null);
        while (pCur.moveToNext()) {
            int code = Integer.valueOf(pCur.getString(
                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));

            String value = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            switch(code){
                case 1:{
                    jsonItem.put("phone_home", value);
                    break;
                }
                case 2:{
                    jsonItem.put("phone_mobile", value);
                    break;
                }
                case 3:{
                    jsonItem.put("phone_company", value);
                    break;
                }
                case 4:{
                    jsonItem.put("phone_company_fax", value);
                    break;
                }
                case 9:{
                    jsonItem.put("phone_car", value);
                    break;
                }
                case 17:{
                    jsonItem.put("phone_company_mobile", value);
                    break;
                }
            }
        }
        pCur.close();
    }

}
