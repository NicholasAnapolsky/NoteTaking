package com.example.nickana.notetaking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class NoteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        DatabaseHandler dbh = new DatabaseHandler(this);

        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner_note);

        //Get categories saved in database and place them in the category spinner.
        List<Category> categories = dbh.getCategoryTable().getAllCategories();
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
                R.layout.spinner_row_main,
                categories);
        categorySpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final DatabaseHandler dbh = new DatabaseHandler(this);

        //noinspection SimplifiableIfStatement
        //Save current note title, category and body into a database.
        if (id == R.id.action_save) {
            //Get currently written notes.
            Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner_note);
            TextView titleTextView = (TextView) findViewById(R.id.titleTextView_note);
            TextView bodyTextView = (TextView) findViewById(R.id.bodyTextView_note);

            //Initialize the note that will be saved.
            Note newNote = new Note(titleTextView.getText().toString(),
                    categorySpinner.getSelectedItemId(),
                    bodyTextView.getText().toString());

            dbh.getNoteTable().createNote(newNote);
        }

        //Get content from database and show in log.
        if (id == R.id.action_debug) {
           Log.d("SQLITE", "=== Start of CategoryTable ===");
           for(Category c : dbh.getCategoryTable().getAllCategories())
               Log.d("SQLITE", " " + c.toString());
           Log.d("SQLITE", "=== End of CategoryTable ===");

           Log.d("SQLITE", "=== Start of NoteTable ===");
           for(Note n : dbh.getNoteTable().getAllNotes())
               Log.d("SQLITE", " " + n.toString());
           Log.d("SQLITE", "=== End of NoteTable ===");
       }

        return super.onOptionsItemSelected(item);
    }

    //Brings up dialogue that creates a new category.
    public void onClick(View view) {
        final AlertDialog.Builder newCategoryDialogueBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        final Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner_note);
        final DatabaseHandler dbh = new DatabaseHandler(NoteActivity.this);

        //Create text input for the dialogue
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        newCategoryDialogueBuilder.setView(input);

        //Create dialogue and it's functionality.
        newCategoryDialogueBuilder.setTitle("Create new note category");
        newCategoryDialogueBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogue, int which) {

                //Place inputted category in the spinner.
                dbh.getCategoryTable().createCategory(new Category(input.getText().toString())); //this puts le thingy in the database lulz
                List<Category> categories = dbh.getCategoryTable().getAllCategories();
                ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(NoteActivity.this,
                        R.layout.spinner_row_main,
                        categories);
                categorySpinner.setAdapter(adapter);
                dialogue.cancel();
            }
        });

        newCategoryDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogue, int which) {
                dialogue.cancel();
            }
        });

        //Run the created dialogue.
        AlertDialog newCategoryDialogue = newCategoryDialogueBuilder.create();

        newCategoryDialogue.show();
    }
}
