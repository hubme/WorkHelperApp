package com.example.serializable;

import java.io.Serializable;

/**
 * @author huoguangxu
 * @since 2018/12/12.
 */
class SerializableSample {
    public static void main(String[] args) {
        testSerializable();
    }

    private static void testSerializable() {
        User user = new User("Vance", "111");
        System.out.println("序列化：" + user.toString());

        String path = "c://user.txt";
        Utils.serialize(user, path);
        User user2 = (User) Utils.unSerialize(path);

        System.out.println("反序列化：" + user2.toString());
        System.out.println("前后对象相同（==）吗？： " + (user == user2));
    }


    private static class User implements Serializable {
        private String name;

        //使用 transient 修饰的字段不会被序列化
        private transient String passWord;

        public User() {
        }

        public User(String name, String passWord) {
            this.name = name;
            this.passWord = passWord;
        }

        @Override public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", passWord='" + passWord + '\'' +
                    '}';
        }
    }
}
