package com.king.app.workhelper.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * &#64SerializedName标签定义属性序列化后的名字.当接口定义的名称可读性很差的时候很实用。
 * &#64Expose可以区分实体中不想被序列化的属性.&#64deserialize (boolean) 反序列化 默认true;&#64serialize(boolean) 序列化默认true.
 * 避免大量使用，因为当不用Gson时要大量修改model。
 *
 * @author VanceKing
 * @since 2017/1/8 0008
 */
public class Student implements Serializable {
    @Expose
    @SerializedName("pId")
    public long id;
    @Expose(serialize = false)
    public int age;
    @Expose(deserialize = false)
    public String name;

    public List<Course> courses;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
