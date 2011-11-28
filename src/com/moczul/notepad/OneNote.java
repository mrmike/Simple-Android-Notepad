package com.moczul.notepad;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class OneNote extends Activity {
	
	private static final String TAG = "notepad";
	
	private TextView noteTitle;
	private TextView noteContent;
	private DBHelper dbhelper;
	private SQLiteDatabase db;
	private String title = "defaultTitle";
	private String content = "defaultContent";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.onenote);
		
		dbhelper = new DBHelper(getApplicationContext());
		
		noteTitle = (TextView) findViewById(R.id.noteTitle);
		noteContent = (TextView) findViewById(R.id.noteContent);
		
		// getting intent
		Intent mIntent = getIntent();
		
		//getting the note's title
		title = mIntent.getStringExtra("title");
		
		//getting the readable database
		db = dbhelper.getReadableDatabase();
		
		Cursor c = dbhelper.getNote(db, title);
		c.moveToFirst();
		db.close();
		content = c.getString(1).toString();
		
		noteTitle.setText(title);
		noteContent.setText(content);
	}
	

}
