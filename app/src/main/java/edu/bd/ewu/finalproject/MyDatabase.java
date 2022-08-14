package edu.bd.ewu.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import android.content.ContentValues;
import android.database.Cursor;


class MyDatabase extends SQLiteOpenHelper {

    // TABLE INFORMATTION
    static final String DATABASE_NAME = "Muhasaba.db";
    static final String TABLE_NAME = "user_auth_info";
    static final String USERNAME = "Username";
    static final String EMAIL = "Email";
    static final String PASSWORD = "Password";
    static final String USER_KEY = "Userkey";
    static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+USERNAME+
            " VARCHAR(255),"+EMAIL+" VARCHAR(255),"+PASSWORD+" VARCHAR(255),"+USER_KEY+" VARCHAR(255))";

    private final Context context;
    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("DB@OnCreate");
        createKeyValueTable(db);
    }
    private void createKeyValueTable(SQLiteDatabase db){
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //createKeyValueTable(db);
        // SQL statements to change the database schema
    }

    private void handleError(SQLiteDatabase db, Exception e){
        String errorMsg = e.getMessage();
        assert errorMsg != null;
        if (errorMsg.contains("no such table")){
            if (errorMsg.contains(USERNAME)){
                createKeyValueTable(db);
            }
        }
    }
    public Cursor execute(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        try {
            res = db.rawQuery(query, null);
        }catch (Exception e) {
            //e.printStackTrace();
            handleError(db, e);
            res = db.rawQuery(query, null);
        }
        return res;
    }

    public void registerUser(String username,String email, String password, String user_key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERNAME, username);
        cv.put(EMAIL, email);
        cv.put(PASSWORD, password);
        cv.put(USER_KEY, user_key);

        try{
            db.insert(TABLE_NAME, null, cv);
            Toast.makeText(context, "আপনার রেজিস্ট্রেশন সম্পন্ন হয়েছে", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            handleError(db, e);
            registerUser(username, email, password, user_key);
        }
    }

    public Cursor getAllKeyValues() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

//    public boolean updateValueByKey(String username, String password, String user_key) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(USERNAME, username);
//        cv.put(PASSWORD, password);
//        cv.put(USER_KEY, user_key);
//        int nr = 0;
//        try{
//            nr = db.update(TABLE_NAME, cv, USERNAME + "=?",
//                    new String[] { username });
//        }catch (Exception e){
//            handleError(db, e);
//            try {
//                nr = db.update(TABLE_NAME, cv, USERNAME + "=?",
//                        new String[]{username});
//            }catch (Exception ex){
//                Toast.makeText(context, "ERROR: "+e.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }
//        if (nr == 0) {
//            registerUser(username, email, password, user_key);
//        }
//        return true;
//    }
//
//    public Integer deleteDataByKey(String key) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int isDeleted = 0;
//        try{
//            isDeleted = db.delete(TABLE_NAME, USERNAME + " = ?", new String[] { key });
//        }catch (Exception e){
//            handleError(db, e);
//            try {
//                isDeleted = db.delete(TABLE_NAME, USERNAME + " = ?", new String[]{key});
//            }catch (Exception ex){}
//        }
//        return isDeleted;
//    }
//
    public UserInfo getValueByKey(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        try{
            res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                    + USER_KEY + "='" + key + "'", null);
        }catch (Exception e){
            handleError(db, e);
            res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                    + USER_KEY + "='" + key + "'", null);
        }
        if(res.getCount()>0){
            UserInfo userInfo = null;
            String name, email, user_key;
            while(res.moveToNext()){
                userInfo = new UserInfo(
                        res.getString(0),
                        res.getString(1),
                        res.getString(2),
                        res.getString(3));
            }
            return userInfo;
        }
        return null;
    }
//    //
//    public void deleteQuery(String query) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res;
//        try {
//            db.execSQL(query);
//        }catch (Exception e){
//            // e.printStackTrace();
//            handleError(db, e);
//            db.execSQL(query);
//        }
//    }
}
