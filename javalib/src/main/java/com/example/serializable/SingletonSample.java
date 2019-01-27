package com.example.serializable;

/**
 * @author VanceKing
 * @since 19-1-27.
 */
public class SingletonSample {
    public static void main(String[] args) {
        SingletonObject instance = SingletonObject.getInstance();
        String path = "/home/fei/my/instance.bat";
        Utils.serialize(instance, path);
        SingletonObject instance2 = (SingletonObject) Utils.unSerialize(path);
        System.out.println(instance == instance2);
    }
}
