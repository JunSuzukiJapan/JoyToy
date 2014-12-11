/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")savedInstanceState;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.sourceforge.soopy.joytoy.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class NotesList extends ListActivity {
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    private static final String NOT_FIRST_RUN = "Not first run";

    private NotesDbAdapter mDbHelper;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_list);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        // set example source code.
        boolean isFirstRun = true;
        if(savedInstanceState != null){
          isFirstRun = ! savedInstanceState.getBoolean(NOT_FIRST_RUN);
        }
        File f = new File(this.getFilesDir() + "/notFirstRun");
        if(f.isFile()){
          isFirstRun = false;
        }else{
          try {
            File dir = this.getFilesDir();
            if( ! dir.isDirectory()){
              dir.mkdir();
            }
            f.createNewFile();
          } catch (IOException e) {
            // do nothing
            e.printStackTrace();
          }
        }

        if(isFirstRun){
          setExamples();
        }
        
        fillData();
    }
    
    private void setExamples(){
      setOneExample("Example: while", R.raw.while_statement);
      setOneExample("Example: userFunction", R.raw.user_function);
      setOneExample("Example: string", R.raw.string);
      setOneExample("Example: object 1", R.raw.object1);
      setOneExample("Example: object 2", R.raw.object2);
      setOneExample("Example: object 3", R.raw.object3);
    }
    
    private void setOneExample(String title, int id){
      Resources r = getResources();
      InputStream stream = r.openRawResource(id);
      InputStreamReader reader = new InputStreamReader(stream);
      BufferedReader in = new BufferedReader(reader);
      try {
        StringBuilder builder = new StringBuilder();
        String text = null;
        while((text = in.readLine()) != null){
          builder.append(text + "\n");
        }
        mDbHelper.createNote(title, builder.toString());
      } catch (IOException e) {
        // do nothing
      }
    }
    
    private void fillData() {
        // Get all of the rows from the database and create the item list
        Cursor notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor);
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{NotesDbAdapter.KEY_TITLE};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = 
        	    new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        setListAdapter(notes);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        menu.add(0, DELETE_ID, 0,  R.string.menu_delete);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            createNote();
            return true;
        case DELETE_ID:
            mDbHelper.deleteNote(getListView().getSelectedItemId());
            fillData();
            return true;
        }
       
        return super.onMenuItemSelected(featureId, item);
    }

    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, MainView.ACTIVITY_EDIT);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(this, MainView.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NOT_FIRST_RUN, true);
    }

}
