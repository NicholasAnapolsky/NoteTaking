package com.example.nickana.notetaking;

/**
 * Created by ian on 15-08-28.
 */
public class Note {

    private long id;
    private String title;
    private long categoryId;
    private String main;

    public Note() {

    }

    public Note(String title, long categoryId, String main) {
        this.title = title;
        this.categoryId = categoryId;
        this.main = main;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

   public String toString() {
        return String.format("%s:%d:%s", title, categoryId, main);
    }

}

