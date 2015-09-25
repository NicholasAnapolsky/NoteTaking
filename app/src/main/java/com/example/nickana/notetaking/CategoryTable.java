package com.example.nickana.notetaking;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ian on 15-08-27.
 */
public class CategoryTable {

    private static final String TABLE_NAME = "category";
    private static final String COLUMN_NAME = "name";

    private static final String[] allColumns = { "_id", COLUMN_NAME };

    // DatabaseHandler creation sql statement
    private static final String CREATE_TABLE = "create table " + TABLE_NAME +
                                               "(_id integer primary key autoincrement, " +
                                               COLUMN_NAME + " text not null unique);";

    private DatabaseHandler databaseHandle;

    public CategoryTable(DatabaseHandler databaseHandle) {
        this.databaseHandle = databaseHandle;
    }

    public String getCreateSQL() {
        return CREATE_TABLE;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    private Category cursorToCategory(Cursor cursor) {
        Category project = new Category();
        project.setId(cursor.getLong(0));
        project.setCategoryName(cursor.getString(1));
        return project;
    }

    /**
     * Insert the Category into the table.
     * @param category The category to be inserted.
     * @postconditions The category's ID field will be set to the value returned by the database.
     */
    public void createCategory(Category category) {

        SQLiteDatabase database = databaseHandle.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, category.getCategoryName());

        long insertId = database.insertOrThrow(TABLE_NAME, null, values);
        category.setId(insertId);

        database.close();
    }

    /**
     * Read the category specified by id.
     * @param id The ID of the category.
     * @return The Category object containing the values from the database.
     */
    public Category readCategory(long id) {
        SQLiteDatabase db = databaseHandle.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "_id", COLUMN_NAME }, "_id =?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Category category = cursorToCategory(cursor);
        return category;
    }

    /**
     * Read the category specified by name.
     * @param name The name of the category.
     * @return The Category object containing the values from the database.
     */
    public Category readCategoryByName(String name) {
        SQLiteDatabase db = databaseHandle.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "_id", COLUMN_NAME }, COLUMN_NAME + " =?",
                new String[] { name }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Category category = cursorToCategory(cursor);
        return category;
    }


    /**
     * Read all categories from the table.
     * @return All rows from the table as a List of Category objects.
     */
    public List<Category> getAllCategories() {
        SQLiteDatabase database = databaseHandle.getReadableDatabase();
        List<Category> comments = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NAME, allColumns, null, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                comments.add(cursorToCategory(cursor));
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
    public int getCategoryCount() {
        return -1;
    }


    /**
     * Update a row in the table.
     * @param category The Category object containing updates. The ID field is used to retrieve the correct row.
     * @return ?
     */
    public int updateCategory(Category category) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Delete a row from the table.
     * @param category The Category object containing the row to delete. The ID fields is used to
     */
    public void deleteCategory(Category category) {
        SQLiteDatabase database = databaseHandle.getWritableDatabase();
        database.delete(TABLE_NAME, "_id = " + category.getId(), null);
        database.close();
    }



}
