package com.king.app.workhelper.model.entity;

/**
 * Created by VanceKing on 2017/1/6 0006.
 */

public class User {
    public String login;
    public long id;

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", id=" + id +
                '}';
    }
}
