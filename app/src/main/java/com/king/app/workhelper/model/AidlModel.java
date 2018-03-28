package com.king.app.workhelper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author VanceKing
 * @since 2018/3/27.
 */

public class AidlModel implements Parcelable {
    private String name;
    private int age;

    public AidlModel() {
    }

    public AidlModel(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.age);
    }

    protected AidlModel(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
    }

    public static final Creator<AidlModel> CREATOR = new Creator<AidlModel>() {
        @Override public AidlModel createFromParcel(Parcel source) {
            return new AidlModel(source);
        }

        @Override public AidlModel[] newArray(int size) {
            return new AidlModel[size];
        }
    };

    @Override public String toString() {
        return "AIDLModel{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
