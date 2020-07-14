package vztrack.gls.com.vztrack_user.utils;

/**
 * Created by sandeep on 21/4/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION =108;
    private static final String DATABASE_NAME = "vztrack_user_database.db";
    private static final String VISITORS_TABLE = "visitors";
    private static final String NOTICES_TABLE = "notices";
    private static final String VISITORS_ID = "id";
    public static final String VISITORS_INDEX_ID = "new_id";
    public static final String VISITORS_FIRST_NAME = "first_name";
    public static final String VISITORS_LAST_NAME = "last_name";
    public static final String VISITORS_MOBILE_NO = "mobile_no";
    public static final String VISITORS_DATE = "date";
    public static final String VISITORS_OutTime = "out_time";
    public static final String VISITORS_PURPOSE = "purpose";
    public static final String VISITORS_FROM = "Visitorfrom";
    public static final String VISITORS_VEHICLE_NO = "vehicle_no";
    public static final String VISITORS_VISITORS_NO = "visitors_no";
    public static final String VISITORS_TOMEET = "to_meet";
    public static final String VISITORS_PHOTO_STRING = "photo_string";
    public static final String VISITORS_DOC_STRING = "doc_string";
    public static final String VISITOR_TEMPERATURE = "temperature";
    public static final String IS_MASK = "is_mask";
    public static final String ERROR_ENTRY = "error_entry";
    public static final String BADGE_NO = "badge_no";
    public static final String APPROVALCOUNT = "approvalCount";
    public static final boolean VISITORS_ASKFORAPPROVAL_BOOLEAN = false;
    public static final String NOTICES_ID = "id";
    public static final String NOTICES_PHOTO_STRING = "photo_string";
    public static final String NOTICES_TITLE = "title";
    public static final String NOTICES_DESCRIPTION = "decription";
    public static final String NOTICES_START_DATE = "start_date";
    public static final String NOTICES_END_DATE = "end_date";
    public static final String NOTICES_FILE_TYPE = "file_type";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS '" + VISITORS_TABLE + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + NOTICES_TABLE + "'");

        db.execSQL("create table "+VISITORS_TABLE+"("+VISITORS_ID+" integer primary key, "
                +VISITORS_INDEX_ID+" text, "
                +VISITORS_FIRST_NAME+" text, "
                +VISITORS_LAST_NAME+" text, "
                +VISITORS_MOBILE_NO+" text, "
                +VISITORS_PURPOSE+" text, "
                +VISITORS_VEHICLE_NO + " text, "
                +VISITORS_VISITORS_NO+" text, "
                +VISITORS_FROM+" text, "
                +VISITORS_TOMEET+" text, "
                +VISITORS_PHOTO_STRING+" text, "
                +VISITORS_DOC_STRING+" text, "
                +VISITORS_DATE+" text, "
                +VISITORS_OutTime+" text, "
                +ERROR_ENTRY+" INTEGER, "
                +VISITOR_TEMPERATURE+" text, "
                +IS_MASK+" INTEGER DEFAULT 0, "
                +BADGE_NO+" text "+")");
         db.execSQL("create table "+NOTICES_TABLE+"("+NOTICES_ID+" integer primary key, "+NOTICES_TITLE+" text, "+NOTICES_DESCRIPTION+" text, "+""+NOTICES_PHOTO_STRING+" text, "+NOTICES_START_DATE+" text, "+NOTICES_END_DATE+" text, "+NOTICES_FILE_TYPE+" text"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //date_and_time,purpose,from,vehicle_no,total_visitors,"",imgString

    public boolean insertDataInVisitors(String index,String strFname,String strLname,String strMobile,
                                        String date_and_time,String Out_time,String purpose,String from,String vehicleNo,
                                        String total_visitors,String toMeet,String strPhotoUrl, String doc, int isIncorrect,
                                        String badgeNo, String visitorTemperature, int mask)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VISITORS_INDEX_ID, index);
        contentValues.put(VISITORS_FIRST_NAME, strFname);
        contentValues.put(VISITORS_LAST_NAME, strLname);
        contentValues.put(VISITORS_MOBILE_NO, strMobile);
        contentValues.put(VISITORS_DATE, date_and_time);
        contentValues.put(VISITORS_OutTime, Out_time);
        contentValues.put(VISITORS_PURPOSE, purpose);
        contentValues.put(VISITORS_VEHICLE_NO, vehicleNo);
        contentValues.put(VISITORS_VISITORS_NO, total_visitors);
        contentValues.put(VISITORS_TOMEET, toMeet);
        contentValues.put(VISITORS_FROM, from);
        contentValues.put(VISITORS_PHOTO_STRING, strPhotoUrl);
        contentValues.put(ERROR_ENTRY, isIncorrect);
        contentValues.put(BADGE_NO, badgeNo);
        contentValues.put(VISITOR_TEMPERATURE, visitorTemperature);
        contentValues.put(IS_MASK, mask);
        db.insert(VISITORS_TABLE, null, contentValues);
        return true;
    }

    public boolean insertDataInNotices(String strTitle, String strDescription, String strStartDate, String strEndDate, String strPhotoUrl, String strFileType)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTICES_TITLE, strTitle);
        contentValues.put(NOTICES_DESCRIPTION, strDescription);
        contentValues.put(NOTICES_START_DATE, strStartDate);
        contentValues.put(NOTICES_END_DATE, strEndDate);
        contentValues.put(NOTICES_PHOTO_STRING, strPhotoUrl);
        contentValues.put(NOTICES_FILE_TYPE, strFileType);
        db.insert(NOTICES_TABLE, null, contentValues);
        return true;
    }

    public Cursor getDataFromVisitorsTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from "+VISITORS_TABLE, null );
    }

    public Cursor getDataFromVisitorsTableById(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = "where "+VISITORS_INDEX_ID+" = "+id;
        return db.rawQuery( "select * from "+VISITORS_TABLE+" "+where, null );
    }

    public Cursor getDataFromNoticesTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from "+NOTICES_TABLE, null );
    }

    public int getNumberOfRowsOfNotices(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, NOTICES_TABLE);
    }

    public int getNumberOfRowsOfVisitors(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, VISITORS_TABLE);
    }

    public void deleteDataFromVisitors () {
        getWritableDatabase().execSQL("DELETE FROM " + VISITORS_TABLE+ ";");
    }

    public void deleteDataFromNotices() {
        getWritableDatabase().execSQL("DELETE FROM " + NOTICES_TABLE+ ";");
    }
}