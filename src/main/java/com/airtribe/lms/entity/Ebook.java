package com.airtribe.lms.entity;

public class Ebook extends Book {

    private final double fileSizeMb;


    public Ebook(String title, String author, String isbn, int publicationYear, double fileSizeMb) {
        super(title, author, isbn, publicationYear);
        this.fileSizeMb = fileSizeMb;
    }

    public double getFileSizeMb() {
        return fileSizeMb;
    }

    @Override
    public String describe() {
        return super.describe() + String.format(" [ebook, %.1fMB]", fileSizeMb);
    }
}
