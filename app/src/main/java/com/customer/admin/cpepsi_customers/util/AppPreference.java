package com.customer.admin.cpepsi_customers.util;

import android.content.Context;
import android.content.SharedPreferences;


public class AppPreference {

    public static final String SHARED_PREFERENCE_NAME = "SATSUNG";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String CONTACT = "contact";
    public static final String AfterID = "AfterId";
    public static final String AfterName = "AfterName";
    public static final String PROBLEM = "problem";
    public static final String SID = "sid";
    public static final String SNAME = "sname";
    public static final String ADDRESS = "address";
    public static final String PROFILE_IMAGE= "profile_image";
    public static final String Refid= "RefID";


    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    public static void setId(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ID, value);

        editor.commit();
    }

    public static String getId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(ID, "");
    }

    public static void setRefid(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Refid, value);

        editor.commit();
    }

    public static String getRefid(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(Refid, "");
    }


    //--------------------------------------
    public static void setName(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, value);
        editor.commit();
    }

    public static String getName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(NAME, "");
    }

    //--------------------------------
    public static void setMobile(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOBILE, value);
        editor.commit();
    }

    public static String getMobile(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(MOBILE, "");
    }

    //-------------------------------------
    public static void setEmail(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, value);
        editor.commit();
    }

    public static String getEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(EMAIL, "");
    }

    //------------------------------
    public static void setContact(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CONTACT, value);
        editor.commit();
    }

    public static String getContact(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(CONTACT, "");
    }

    //-----------------------------------

    public static void setAfterId(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AfterID, value);
        editor.clear();
        editor.commit();
    }

    public static String getAfterID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(AfterID, "null");
    }

    public static void setAfterName(Context context, String serName) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AfterName, serName);
        editor.clear();
        editor.commit();
    }

    public static String getAfterName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(AfterName, "null");
    }

    //-------------------------------------
    public static void setProblem(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROBLEM, value);
        editor.commit();
    }

    public static String getProblem(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(PROBLEM, "");
    }

    //---------------------------------
    public static void setSid(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SID, value);
        editor.commit();
    }

    public static String getSid(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(SID, "");
    }

    //---------------------------------
    public static void setSname(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SNAME, value);
        editor.commit();
    }

    public static String getSname(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(SNAME, "");
    }
    //---------------------------------
    public static void setAddress(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ADDRESS, value);
        editor.commit();
    }

    public static String getAddress(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(ADDRESS, "");
    }
//----------------------------------------------------------
public static void setProfileImage(Context context, String value) {
    SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(PROFILE_IMAGE, value);
    editor.commit();
}

    public static String getProfileImage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(PROFILE_IMAGE, "");
    }


    //-------------------------------------------------------------

    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }


}
