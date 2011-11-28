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
	
	//our views for display note title and content
	private TextView noteTitle;
	private TextView noteContent;
	
	//dbhelper
	private DBHelper dbhelper;
	private SQLiteDatabase db;
	
	//default values for title and content variables
	private String title = "defaultTitle";
	private String content = "defaultContent";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.onenote);
		
		//initialization of DBHelper
		dbhelper = new DBHelper(getApplicationContext());
		
		
		noteTitle = (TextView) findViewById(R.id.noteTitle);
		noteContent = (TextView) findViewById(R.id.noteContent);
		
		// getting intent
		Intent mIntent = getIntent();
		
		//getting the note's title from title
		title = mIntent.getStringExtra("title");
		
		//getting the readable database
		db = dbhelper.getReadableDatabase();
		
		//getting the note from database
		Cursor c = dbhelper.getNote(db, title);
		//closing the database connection
		db.close();
		
		//getting the content from cursor
		//getString(1) because first column is noteTitle and second is noteContent
		content = c.getString(1).toString();
		
		//setting notes to our views
		noteTitle.setText(title);
		noteContent.setText(content);
	}
	

}
