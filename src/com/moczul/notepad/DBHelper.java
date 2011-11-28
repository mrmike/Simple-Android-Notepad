package com.moczul.notepad;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private Context ctx;
	private static final int version = 1;
	private static final String DB_NAME = "notesDB";
	private static final String TABLE_NAME = "notes";
	private static final String KEY_TITLE = "noteTitle";
	private static final String KEY_CONTENT = "noteContent";
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_TITLE+" TEXT NOT NULL, "+KEY_CONTENT+" TEXT NOT NULL, date TEXT);";
		
	public DBHelper(Context context) {
		super(context, DB_NAME, null, version);
		this.ctx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
		db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
		
		onCreate(db);
		
	}
	
	public void addNote(String title, String content) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("noteTitle", title);
		cv.put("noteContent", content);
		cv.put("date", new Date().toString());
		
		db.insert(TABLE_NAME, null, cv);
		
		db.close();
		
	}
	
	public Cursor getNotes(SQLiteDatabase db) {
		Cursor c = db.query(TABLE_NAME, new String[] {KEY_TITLE, KEY_CONTENT}, null, null, null, null, "date DESC");
		c.moveToFirst();
		return c;
	}
	
	public Cursor getNote(SQLiteDatabase db, String title) {
		Cursor c = db.query(TABLE_NAME, new String[] {KEY_TITLE, KEY_CONTENT}, KEY_TITLE +" LIKE '"+ title +"'", null, null, null, null);
		c.moveToFirst();
		return c;
	}
	
	public void removeNote(String title) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, KEY_TITLE+" like '"+ title + "'", null);
		db.close();
	}
	
	public void updateNote(String title, String content, String editTitle) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("noteTitle", title);
		cv.put("noteContent", content);
		cv.put("date", new Date().toString());
		
		db.update(TABLE_NAME, cv, KEY_TITLE + " LIKE '" +  editTitle +  "'", null);
		
		db.close();
		
		
	}

}
