package com.moczul.notepad;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
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
	private boolean isEditable;
	private DBHelper dbhelper;
	private SQLiteDatabase db;
	private String editTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.createlayout);
		addNoteToDB = (Button) findViewById(R.id.addNoteToDB);
		titleEditText = (EditText) findViewById(R.id.TitleEditText);
		contentEditText = (EditText) findViewById(R.id.ContentEditText);
		dbhelper = new DBHelper(getApplicationContext());
		
		Intent mIntent = getIntent();
		editTitle = mIntent.getStringExtra("title");
		isEditable = mIntent.getBooleanExtra("isEditable", false);
		
		if(isEditable) {
			Log.d(TAG, "isEditable");
			db = dbhelper.getReadableDatabase();
			Cursor c = dbhelper.getNote(db, editTitle);
			
			db.close();
			titleEditText.setText(c.getString(0));
			contentEditText.setText(c.getString(1));
			addNoteToDB.setText("Aktualizuj");
		}
		
		
		addNoteToDB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String title = titleEditText.getText().toString();
				String content = contentEditText.getText().toString();
				
				if (title.equals("") || content.equals("")) {
					Toast.makeText(getApplicationContext(), "Musisz podać treść!", Toast.LENGTH_LONG).show();
					return;
				}
				
				//add note to db and finish activity
				if (!isEditable) {
					dbhelper = new DBHelper(getApplicationContext());
					dbhelper.addNote(title, content);
					finish();
				} else {
					dbhelper.updateNote(title, content, editTitle);
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
