package com.moczul.notepad;

/**
 * @author Michał Moczulski
 * twitter_url: http://twitter.com/#!/moczul
 */

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Simple_NotepadActivity extends Activity implements
		OnItemClickListener {

	// Tag for debugging
	private static final String TAG = "notepad";

	// our views from layout
	private ListView noteList;
	private Button addNoteBtn;

	// adapter use to populate the listview
	private ArrayAdapter<String> adapter;
	// cursor will contain notes from database
	private Cursor notes;
	// database helper
	private DBHelper dbhelper;

	// items contain notes titles
	private ArrayList<String> titles;
	private ArrayList<Item> items;

	// variable will contain the position of clicked item in listview
	private int position = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		noteList = (ListView) findViewById(R.id.noteList);
		addNoteBtn = (Button) findViewById(R.id.addNote);

		// initialization of database helper
		dbhelper = new DBHelper(getApplicationContext());

		// setting note's titles to item in listview
		setNotes();

		// setting that longclick on listview will open the context menu
		this.registerForContextMenu(noteList);

		// onClicklistener for our "new note" button
		// click on button will open CreateNote Activity
		addNoteBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						CreateNote.class));

			}
		});

	}

	//
	public void setNotes() {
		// init the items arrayList
		titles = new ArrayList<String>();
		items = new ArrayList<Item>();

		// getting readable database
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		// getting notes from db
		// see dbhelper for more details
		notes = dbhelper.getNotes2(db);

		// this should fix the problem
		// now the activity will be managing the cursor lifecycle
		startManagingCursor(notes);

		// closing database connection !important
		// always close connection with database
		// we closing database connection here because we don't use db anymore
		db.close();

		// populating ArrayList items with notes titles
		if (notes.moveToFirst()) {
			do {
				items.add(new Item(notes.getShort(0), notes.getString(1)));
			} while (notes.moveToNext());
		}
		
		
		for (Item i : items) {
			titles.add(i.getTitle());
		}

		// creating new adapter
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, titles);
		noteList.setAdapter(adapter);
		// setting listener to the listView
		noteList.setOnItemClickListener(this);

	}

	// always when we start this activity we want to refresh the list of notes
	@Override
	protected void onResume() {
		super.onResume();
		setNotes();
	}

	// this method is called when user long clicked on listview
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// using menuInfo to determine which item of listview was clicked
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		position = info.position;
		// setting the title of context menu header
		menu.setHeaderTitle(getResources().getString(R.string.CtxMenuHeader));
		// inflating the menu from xml file
		// for details see context_menu.xml file in /res/menu folder
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);

	}

	// method is called when user clicks on contextmenu item
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// getting the textView from ListView which item was clicked
		TextView tv = (TextView) noteList.getChildAt(position);
		// getting the title of this textView
		String title = tv.getText().toString();

		// performing one of actions, depending on user choice
		switch (item.getItemId()) {

		case R.id.showNote:
			Intent mIntent = new Intent(this, OneNote.class);
			mIntent.putExtra("id", items.get(position).getId());
			startActivity(mIntent);
			break;

		case R.id.editNote:
			Intent i = new Intent(this, CreateNote.class);
			i.putExtra("id", items.get(position).getId());
			Log.d(TAG, title);
			// this is important
			// we send boolean to CreateNote activity
			// thanks to this boolean activity knows that user want to edit
			// notes
			i.putExtra("isEdit", true);
			startActivity(i);
			break;

		case R.id.removeNote:
			// removing this notes
			dbhelper.removeNote(items.get(position).getId());
			// refreshing the listView
			setNotes();
			break;

		}

		return false;

	}

	// when user click on note's title we're opening this note in OneNote
	// activity
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TextView tv = (TextView) arg1;
		String title = tv.getText().toString();
		Intent mIntent = new Intent(this, OneNote.class);
		mIntent.putExtra("title", title);
		mIntent.putExtra("id", items.get(arg2).getId());
		startActivity(mIntent);
	}
	
}