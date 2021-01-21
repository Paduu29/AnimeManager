package com.padunime.animemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";


    private static final String TABLE_NAME_USERS = "users";
    private static final String USERS_COL1 = "email";
    private static final String USERS_COL2 = "UserID";


    private static final String TABLE_NAME_ANIME = "anime";
    private static final String ANIME_COL1 = "ANI_ID";
    private static final String ANIME_COL2 = "UserID";
    private static final String ANIME_COL3 = "Title";
    private static final String ANIME_COL4 = "Score";
    private static final String ANIME_COL5 = "Description";


    private static final String TABLE_NAME_ANIME_COMPLETE = "complete_anime";
    private static final String ANIME_COM_COL1 = "ANI_ID";
    private static final String ANIME_COM_COL2 = "Title";
    private static final String ANIME_COM_COL3 = "Score";
    private static final String ANIME_COM_COL4 = "Description";



    private Context mCtx;



    public DatabaseHelper(Context context) {
        super(context, "Information.db", null, 1);
        mCtx = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsers = "CREATE TABLE " +
                TABLE_NAME_USERS +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_COL1 +
                " TEXT, " +
                USERS_COL2 + " TEXT)";
        db.execSQL(createTableUsers);

        String createTableAnime = "CREATE TABLE " +
                TABLE_NAME_ANIME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ANIME_COL1 +
                " INTEGER, " +
                ANIME_COL2 + " TEXT, " +
                ANIME_COL3 + " TEXT, " +
                ANIME_COL4 + " INTEGER, " +
                ANIME_COL5 + " TEXT)";
        db.execSQL(createTableAnime);

        String createTableAnimeComplete = "CREATE TABLE " +
                TABLE_NAME_ANIME_COMPLETE +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ANIME_COM_COL1 +
                " INTEGER, " +
                ANIME_COM_COL2 + " TEXT, " +
                ANIME_COM_COL3 + " INTEGER, " +
                ANIME_COM_COL4 + " TEXT)";
        System.out.println("executing createTableAnimeComplete");
        db.execSQL(createTableAnimeComplete);
        try {
            get_json(db);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ANIME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ANIME_COMPLETE);
        //onCreate(db);
    }

    public boolean addDataUsers(String email, String UID){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COL1, email);
        contentValues.put(USERS_COL2, UID);
        long ins = db.insert(TABLE_NAME_USERS, null, contentValues);
        if(ins == -1) return false;
        else return true;
    }

    public boolean addDataAnime(int ANI_ID, String UID, String title, int SCORE, String descrip){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANIME_COL1, ANI_ID);
        contentValues.put(ANIME_COL2, UID);
        contentValues.put(ANIME_COL3, title);
        contentValues.put(ANIME_COL4, SCORE);
        contentValues.put(ANIME_COL5, descrip);
        long ins = db.insert(TABLE_NAME_ANIME, null, contentValues);

        if(ins == -1) return false;
        else return true;
    }

    public boolean deleteFromWatching(int ANI_ID){
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteAnimeFromWatching = "DELETE FROM anime where " + ANIME_COL1 + "='" + ANI_ID + "' AND " + ANIME_COL2 + "='" + userID + "'";
        try{
            db.execSQL(deleteAnimeFromWatching);

            return true;
        }
        catch (Error error){

            return false;
        }

    }

    public ArrayList<ArrayList<String>> getAnimeUser(){
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> anime = new ArrayList<String>();
        ArrayList<ArrayList<String>> anime_list = new ArrayList<ArrayList<String>>();

        Cursor cursor = db.rawQuery("select * from anime where UserID='" + userID +"'" , null);
        if(cursor.moveToFirst()){
            do{
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COL1)));
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COL3)));
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COL4)));
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COL5)));
                anime_list.add(anime);
                anime = new ArrayList<String>();
            }while(cursor.moveToNext());
        }


        return anime_list;
    }



    public boolean checkAnimeForUser(int ANI_ID){
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SQLiteDatabase db = this.getReadableDatabase();
        boolean AlreadyInDb = false;
        Cursor cursor = db.rawQuery("SELECT ani_id from anime where UserID='" + userID +"'", null);
        if(cursor.moveToFirst()){
            do{
                if (ANI_ID == Integer.parseInt(cursor.getString(cursor.getColumnIndex(ANIME_COL1)))){
                    AlreadyInDb = true;
                }
            }while(cursor.moveToNext());
        }
        if (AlreadyInDb){

            return false;
        }
        else  return true;
    }

    public ArrayList<ArrayList<String>> getAnimeList(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> anime = new ArrayList<String>();
        ArrayList<ArrayList<String>> anime_list = new ArrayList<ArrayList<String>>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_ANIME_COMPLETE, null);
        if(cursor.moveToFirst()){
            do{
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COM_COL1)));
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COM_COL2)));
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COM_COL3)));
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COM_COL4)));
                anime_list.add(anime);
                anime = new ArrayList<String>();
            }while(cursor.moveToNext());
        }


        return anime_list;
    }

    public ArrayList<ArrayList<String>> getAnimeSorted(String orderBy, String form ){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> anime = new ArrayList<String>();
        ArrayList<ArrayList<String>> anime_list = new ArrayList<ArrayList<String>>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_ANIME_COMPLETE + " order by " + orderBy + " " + form, null);
        if(cursor.moveToFirst()){
            do{
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COM_COL1)));
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COM_COL2)));
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COM_COL3)));
                anime.add(cursor.getString(cursor.getColumnIndex(ANIME_COM_COL4)));
                anime_list.add(anime);
                anime = new ArrayList<String>();
            }while(cursor.moveToNext());
        }


        return anime_list;
    }



    public void get_json(SQLiteDatabase db) throws IOException, JSONException {
        String json;
        InputStream is = mCtx.getAssets().open("animejson.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        json = new String(buffer, "UTF-8");
        JSONArray m_jArray = new JSONArray(json);
        ArrayList<HashMap<String, String>> titleList = new ArrayList<>();
        HashMap<String, String> m_li;

        for (int i = 0; i < m_jArray.length(); i++) {
            JSONObject jo_inside = m_jArray.getJSONObject(i);
            String Title = jo_inside.getString("title");

            m_li = new HashMap<String, String>();
            m_li.put("title", Title);
            titleList.add((m_li));
            int rank = jo_inside.getInt("iscr");
            int ani_id = jo_inside.getInt("ani_id");
            String description = jo_inside.getString("synopsis");

            ContentValues contentValues = new ContentValues();
            contentValues.put(ANIME_COM_COL1, ani_id);
            contentValues.put(ANIME_COM_COL2, Title);
            contentValues.put(ANIME_COM_COL3, rank);
            contentValues.put(ANIME_COM_COL4, description);
            db.insert(TABLE_NAME_ANIME_COMPLETE, null, contentValues);

        }


    }

}
