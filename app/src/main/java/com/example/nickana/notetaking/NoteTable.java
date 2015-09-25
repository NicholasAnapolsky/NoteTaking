package com.example.nickana.notetaking;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ian on 15-08-27.
 */
public class NoteTable {

    private static final String TABLE_NAME = "notes";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_MAIN = "main";

    private static final String[] allColumns = { "_id", COLUMN_TITLE, COLUMN_CATEGORY, COLUMN_MAIN };

    // DatabaseHandler creation sql statement
    private static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(_id integer primary key autoincrement, " +
            COLUMN_TITLE + " text not null unique, " +
            COLUMN_CATEGORY + " integer not null, " +
            COLUMN_MAIN + " text not null" +
            ", FOREIGN KEY (" + COLUMN_CATEGORY + ") REFERENCES category(_id));";

    private DatabaseHandler databaseHandle;

    public NoteTable(DatabaseHandler databaseHandle) {
        this.databaseHandle = databaseHandle;
    }

    public String getCreateSQL() {
        return CREATE_TABLE;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    private Note cursorToNote(Cursor cursor) {
        Note project = new Note();
        project.setId(cursor.getLong(0));
        project.setTitle(cursor.getString(1));
        project.setCategoryId(cursor.getLong(2));
        project.setMain(cursor.getString(3));
        return project;
    }

    /**
     * Insert the Main into the table.
     * @param note The main to be inserted.
     * @postconditions The main's ID field will be set to the value returned by the database.
     */
    public void createNote(Note note) {

        SQLiteDatabase database = databaseHandle.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CATEGORY, note.getCategoryId());
        values.put(COLUMN_MAIN, note.getMain());

        long insertId = database.insertOrThrow(TABLE_NAME, null, values);
        note.setId(insertId);

        database.close();
    }

    /**
     * Read the note specified by id.
     * @param id The ID of the note.
     * @return The Note object containing the values from the database.
     */
    public Note readNote(long id) {
        SQLiteDatabase db = databaseHandle.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "_id", COLUMN_TITLE, COLUMN_CATEGORY, COLUMN_MAIN }, "_id =?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = cursorToNote(cursor);
        return note;
    }

    /**
     * Read the note specified by name.
     * @param title The name of the note.
     * @return The Note object containing the values from the database.
     */
    public Note readNoteByTitle(String title) {
        SQLiteDatabase db = databaseHandle.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "_id", COLUMN_TITLE }, COLUMN_TITLE + " =?",
                new String[] { title }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = cursorToNote(cursor);
        return note;
    }


    /**
     * Read all categories from the table.
     * @return All rows from the table as a List of Note objects.
     */
    public List<Note> getAllNotes() {
        SQLiteDatabase database = databaseHandle.getReadableDatabase();
        List<Note> comments = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NAME, allColumns, null, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                comments.add(cursorToNote(cursor));
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        return comments;
    }

    /**
     * Get the number of rows in the table.
     * @return The number of rows in the table.
     */
    public int getNoteCount() {
        return -1;
    }


    /**
     * Update a row in the table.
     * @param note The Note object containing updates. The ID field is used to retrieve the correct row.
     * @return ?
     */
    public int updateNote(Note note) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Delete a row from the table.
     * @param note The Note object containing the row to delete. The ID fields is used to
     */
    public void deleteNote(Note note) {
        SQLiteDatabase database = databaseHandle.getWritableDatabase();
        database.delete(TABLE_NAME, "_id = " + note.getId(), null);
        database.close();
    }
}

