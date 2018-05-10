package com.king.app.workhelper.model;

/**
 * @author VanceKing
 * @since 2018/5/10.
 */
public class Book {
    private long id;
    private String name;

    public Book() {
    }

    public Book(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
