package com.library.model;

public class Book {
    private String title;
    private String author;
    private String action; // ADD, BORROW, RETURN

    public Book(String title, String author, String action) {
        this.title = title;
        this.author = author;
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAction() {
        return action;
    }
}