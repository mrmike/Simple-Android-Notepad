package com.moczul.notepad;

/**
 * @author Michał Moczulski
 * twitter_url: http://twitter.com/#!/moczul
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNote extends Activity {
	

	private static final String TAG = "notepad";

	private Button addNoteToDB;
	private EditText titleEditText;
	private EditText contentEditText;
	
	//this variable will inform us if user want to create a new note or just update
	private boolean isEdit;
	
	private DBHelper dbhelper;
	private SQLiteDatabase db;
	
	//variable will contain the title of editing note
	private String editTitle;
	private int id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.createlayout);
		
		//binding views from layout to variable
		//quite simple, huh ? :)
		addNoteToDB = (Button) findViewById(R.id.addNoteToDB);
		titleEditText = (EditText) findViewById(R.id.TitleEditText);
		contentEditText = (EditText) findViewById(R.id.ContentEditText);
		
		//initialization of DBHelper
		//again :)
		//it takes context as argument
		dbhelper = new DBHelper(getApplicationContext());
		
		//getting the intent
		Intent mIntent = getIntent();
		//if user is editting the note we bind the title of this note to editTitle variable
		editTitle = mIntent.getStringExtra("title");
		id = mIntent.getIntExtra("id", 0);
		
		//we're getting the isEdit value
		//if user is editing note, the value if true
		//otherwise the default value is false
		isEdit = mIntent.getBooleanExtra("isEdit", false);
		
		//we're checking if user want to edit note
		if(isEdit) {
			Log.d(TAG, "isEdit");
			//getting the readable database
			db = dbhelper.getReadableDatabase();
			Cursor c = dbhelper.getNote(db, id);
			//closing db connection
			db.close();
			//here we're set title and content of note to editText views
			titleEditText.setText(c.getString(0));
			contentEditText.setText(c.getString(1));
			//and we're changing the button text to something more appropriate
			//from add note to update note 
			//you can change button text in /res/values/strings.xml file
			addNoteToDB.setText(getResources().getString(R.string.updateNoteButton));
		}
		
		
		//setting listener for button
		addNoteToDB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//when user clicks button 
				//we're grabbing the title and content from editText
				String title = titleEditText.getText().toString();
				String content = contentEditText.getText().toString();
				
				//if user left title or content field empty
				//we show the toast, and tell to user to fill the fields
				
				if (title.equals("") || content.equals("")) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation), Toast.LENGTH_LONG).show();
					return;
				}
				
				//adding note to db
				if (!isEdit) {
					//if it isn't edit mode we just add a new note to db
					dbhelper = new DBHelper(getApplicationContext());
					dbhelper.addNote(title, content);
					//and finish the activity here
					//so we came back to Simple_NotepadActivity
					finish();
				} else {
					//if this is edit mode, we just update the old note
					dbhelper.updateNote(title, content, editTitle);
					//and the same finish activity
					finish();
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		dbhelper.close();
	}
	

}
