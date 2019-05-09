package com.customer.admin.cpepsi_customers.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SessionManager {


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String MyPREFERENCES = "MyPrefss";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_MOBILE = "mobile";
    private static final String UserID = "user_id";
    private static final String REGISTRATION = "registration";
    private static final String IS_FirstPayment = "firstpayment";
    private static final String After_servie_ID = "afterservice";
    private static final String After_servie_Nmae = "afterservicename";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = pref.edit();
//        editor = pref.edit();
    }


    public void serverLogin(String strName, int user_Id) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, strName);
        editor.putInt(UserID, user_Id);
        editor.commit();
    }

    public void adminLogin(String strName) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, strName);
//        editor.putInt(UserID, user_Id);
        editor.commit();
    }

    public void dieselSubsidyLogin(String strMobile) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_MOBILE, strMobile);
        editor.commit();
    }


    public void malegaonLogin() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    // Clearing all data from Shared Preferences
    public void logoutUser() {
        editor.clear();
        editor.commit();

    }

    public void setAfterName(String strAfterName) {
       // editor.putBoolean(IS_LOGIN, true);
        editor.putString(After_servie_Nmae, strAfterName);
        editor.clear();
        editor.commit();
    }


    public String isAfter_servie_Name() {
        return pref.getString(After_servie_Nmae,"null");
    }


    public boolean isFirstPayment(){
        return pref.getBoolean(IS_FirstPayment,false);
    }


    public boolean  isAfter_servie_ID() {
        return pref.getBoolean(After_servie_ID,false);
    }
}