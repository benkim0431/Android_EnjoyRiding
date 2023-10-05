package com.skh.enjoyriding.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skh.enjoyriding.model.Bike;
import com.skh.enjoyriding.model.Booking;
import com.skh.enjoyriding.model.Hours;
import com.skh.enjoyriding.model.User;

public class DBHelper extends SQLiteOpenHelper {

    //Database name and version
    static final String DBNAME = "EnjoyRiding.db";
    static final int VERSION = 1;

    //User Table
    //userId, username, password
    static final String USER_TABLE_NAME = "User";
    static final String USER_COL1 = "userId";
    static final String USER_COL2 = "username";
    static final String USER_COL3 = "password";

    //Bike Table
    //bikeId, location
    static final String BIKE_TABLE_NAME = "Bike";
    static final String BIKE_COL1 = "bikeId";
    static final String BIKE_COL2 = "latitude";
    static final String BIKE_COL3 = "longitude";
    static final String BIKE_COL4 = "title";
    static final String BIKE_COL5 = "snippet";
    static final String BIKE_COL6 = "drawableId";

    //Booking Table
    //userId, bikeId, startHour
    static final String BOOKING_TABLE_NAME = "Booking";
    static final String BOOKING_COL1 = "bookingId";
    static final String BOOKING_COL2 = "userId";
    static final String BOOKING_COL3 = "bikeId";
    static final String BOOKING_COL4 = "startHour";

    //Hours Table
    //hourId, hour
    static final String HOUR_TABLE_NAME = "Hours";
    static final String HOUR_COL1 = "hourId";
    static final String HOUR_COL2 = "hour";

    //Create User Table
    static final String CREATE_USER_TABLE = " CREATE TABLE " + USER_TABLE_NAME
            + " ( "
            + USER_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_COL2 + " TEXT NOT NULL, "
            + USER_COL3 + " TEXT NOT NULL "
            + " ); ";

    //Create Bike Table
    static final String CREATE_BIKE_TABLE = " CREATE TABLE " + BIKE_TABLE_NAME
            + " ( "
            + BIKE_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + BIKE_COL2 + " DOUBLE NOT NULL, "
            + BIKE_COL3 + " DOUBLE NOT NULL, "
            + BIKE_COL4 + " TEXT, "
            + BIKE_COL5 + " TEXT, "
            + BIKE_COL6 + " INTEGER "
            + " ); ";

    //Create Book Table
    static final String CREATE_BOOKING_TABLE = " CREATE TABLE " + BOOKING_TABLE_NAME
            + " ( "
            + BOOKING_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + BOOKING_COL2 + " INTEGER NOT NULL, "
            + BOOKING_COL3 + " INTEGER NOT NULL, "
            + BOOKING_COL4 + " INTEGER NOT NULL "
            + " ); ";

    //Create Hours Table
    static final String CREATE_HOUR_TABLE = " CREATE TABLE " + HOUR_TABLE_NAME
            + " ( "
            + HOUR_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HOUR_COL2 + " INTEGER NOT NULL "
            + " ); ";

    //Drop tables
    static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;
    static final String DROP_BIKE_TABLE = "DROP TABLE IF EXISTS " + BIKE_TABLE_NAME;
    static final String DROP_BOOKING_TABLE = "DROP TABLE IF EXISTS " + BOOKING_TABLE_NAME;
    static final String DROP_HOUR_TABLE = "DROP TABLE IF EXISTS " + HOUR_TABLE_NAME;

    //Constructor
    public DBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_BIKE_TABLE);
        db.execSQL(CREATE_BOOKING_TABLE);
        db.execSQL(CREATE_HOUR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_BIKE_TABLE);
        db.execSQL(DROP_BOOKING_TABLE);
        db.execSQL(DROP_HOUR_TABLE);
        onCreate(db);
    }

    public void resetBikeTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(DROP_BIKE_TABLE);
        db.execSQL(CREATE_BIKE_TABLE);
    }

    public void resetHourTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(DROP_HOUR_TABLE);
        db.execSQL(CREATE_HOUR_TABLE);
    }

    //Insert User
    public String insertOrUpdateUser(User user){
        String message = user.validate();

        if(message != null){
            return message;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(USER_COL2, user.getUsername());
        cv.put(USER_COL3, user.getPassword());

        long result = -1;

        if(user.getUserId() != 0){
            result = db.update(USER_TABLE_NAME, cv,
                    USER_COL1 + " = ?",
                    new String[]{String.valueOf(user.getUserId())});
        }else{
            result = db.insert(USER_TABLE_NAME, null, cv);
        }

        return (result == -1 ? "User could not be added." : null);
    }

    //Insert Bike
    public String insertOrUpdateBike(Bike bike){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(BIKE_COL2, bike.getLatitude());
        cv.put(BIKE_COL3, bike.getLongitude());
        cv.put(BIKE_COL4, bike.getTitle());
        cv.put(BIKE_COL5, bike.getSnippet());
        cv.put(BIKE_COL6, bike.getDrawableId());

        long result = -1;

        if(bike.getBikeId() != 0){
            result = db.update(BIKE_TABLE_NAME, cv,
                    BIKE_COL1 + " = ?",
                    new String[]{String.valueOf(bike.getBikeId())});
        }else{
            result = db.insert(BIKE_TABLE_NAME, null, cv);
        }

        return (result == -1 ? "Bike could not be added." : null);
    }

    //Insert Booking
    public String insertOrUpdateBooking(Booking booking){
        String message = booking.validate();

        if(message != null){
            return message;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(BOOKING_COL2, booking.getUserId());
        cv.put(BOOKING_COL3, booking.getBikeId());
        cv.put(BOOKING_COL4, booking.getStartHour());

        long result = -1;

        if(booking.getBookingId() != 0){
            result = db.update(BOOKING_TABLE_NAME, cv,
                    BOOKING_COL1 + " = ?",
                    new String[]{String.valueOf(booking.getBookingId())});
        }else{
            result = db.insert(BOOKING_TABLE_NAME, null, cv);
        }

        return (result == -1 ? "Booking could not be added." : null);
    }

    //Insert Hour
    public String insertHour(Hours hour){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(HOUR_COL2, hour.getHour());

        long result = -1;

        if(hour.getHourId() != 0){
            result = db.update(HOUR_TABLE_NAME, cv,
                    HOUR_COL1 + " = ?",
                    new String[]{String.valueOf(hour.getHourId())});
        }else{
            result = db.insert(HOUR_TABLE_NAME, null, cv);
        }

        return (result == -1 ? "Hour could not be added." : null);
    }

    //Retrieve user record by userId
    public User getUserByUsername(String username){
        String sql = "SELECT * FROM " + USER_TABLE_NAME +
                " WHERE " + USER_COL2 + " = ?";
        return getUser(sql, username);
    }

    //Retrieve bike by id
    public Bike getBikeById(int bikeId){
        String sql = "SELECT * FROM " + BIKE_TABLE_NAME +
                " WHERE " + BIKE_COL1 + " = ?";
        String bikeIdStr = String.valueOf(bikeId);
        return getBike(sql, new String[]{bikeIdStr});
    }

    public Hours getHourById(int hourId){
        String sql = "SELECT * FROM " + HOUR_TABLE_NAME +
                " WHERE " + HOUR_COL1 + " = ?";
        String hourIdStr = String.valueOf(hourId);
        return getHour(sql, hourIdStr);
    }

    //Get all bikes
    public Cursor getAllBikes() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursorObj;
        cursorObj=db.rawQuery("select * from "+ BIKE_TABLE_NAME,null);
        if(cursorObj!=null)
        {
            cursorObj.moveToFirst();
        }
        return cursorObj;
    }

    //Get booking by user
    public Cursor getBookingByUser(int userId){
        String sql = "SELECT * FROM " + BOOKING_TABLE_NAME +
                " WHERE " + BOOKING_COL2 + " = " + String.valueOf(userId);

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursorObj;
        cursorObj=db.rawQuery(sql,null);
        if(cursorObj!=null)
        {
            cursorObj.moveToFirst();
        }
        return cursorObj;
    }

    public Cursor getAllBikesAvailableHours(int bikeId){
        String value = String.valueOf(bikeId);
        String sql = "SELECT * FROM " + HOUR_TABLE_NAME +
                " WHERE " + HOUR_COL1 + " NOT IN ( SELECT " + BOOKING_COL4 +
                                                 " FROM " + BOOKING_TABLE_NAME +
                                                 " WHERE " + BOOKING_COL3 + " = " + value +
                ") ORDER BY " + HOUR_COL2;

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursorObj;
        cursorObj=db.rawQuery(sql,null);
        if(cursorObj!=null)
        {
            cursorObj.moveToFirst();
        }
        return cursorObj;

    }

    public String deleteBooking(int bookingId){
        SQLiteDatabase db = this.getWritableDatabase();

        int result =  db.delete(BOOKING_TABLE_NAME,
                BOOKING_COL1 + " = " + bookingId, null);

        return (result == -1 ? "Record could not be deleted." : null);

    }

    private User getUser(String sql, String value){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{value});

        //If data was found, return it.
        if(cursor.moveToFirst()){
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            cursor.close();
            db.close();

            return user;
        }

        return null;
    }

    private Bike getBike(String sql, String[] values){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, values);

        //If data was found, return it.
        if(cursor.moveToFirst()){
            Bike bike = new Bike(
                    cursor.getInt(0),
                    cursor.getDouble(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            );
            cursor.close();
            db.close();

            return bike;
        }

        return null;
    }

    private Hours getHour(String sql, String value){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{value});

        //If data was found, return it.
        if(cursor.moveToFirst()){
            Hours hour = new Hours(
                    cursor.getInt(0),
                    cursor.getInt(1)
            );
            cursor.close();
            db.close();

            return hour;
        }

        return null;
    }
}
