package com.king.app.workhelper.model.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.king.app.workhelper.BR;

/**
 * BR.java是类似R.java的资源文件，是 Binding Resources 的缩写，由框架自动生成。
 * note: BR 中的 id 生成的依据是 @Bindable 修饰的方法名 getXXX()，而非方法体的内容。
 * 当数据发生变化时还是需要手动发出通知。
 * 通过调用notifyPropertyChanged(BR.name)来通知系统 BR.name 这个 entry 的数据已经发生变化，需要更新 UI。
 *
 * @author VanceKing
 * @since 2018/4/4.
 */

public class UserModel extends BaseObservable {
    public final ObservableField<String> gender = new ObservableField<>();
    
    private String name;
    private int age;
    private String pic;

    public UserModel(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getAge() {
        return age;
    }


    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    @Bindable
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
        notifyPropertyChanged(BR.pic);
    }
}
