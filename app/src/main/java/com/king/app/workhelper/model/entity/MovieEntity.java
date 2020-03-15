package com.king.app.workhelper.model.entity;

import java.util.List;

/**
 * @author VanceKing
 * @since 2017/3/13.
 */

public class MovieEntity {
    public int count;
    public int start;
    public int total;
    public String title;
    public List<Movie> subjects;

    @Override public String toString() {
        return "MovieEntity{" +
                "count=" + count +
                ", start=" + start +
                ", total=" + total +
                ", title='" + title + '\'' +
                ", subijets=" + subjects +
                '}';
    }
}
