package com.example.serializable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author VanceKing
 * @since 2018/4/23.
 */

class ExternalizableSample {
    public static void main(String[] args) {
        testExternalizable();
    }

    private static void testExternalizable() {
        String path = "c://user.txt";
        Person person = new Person(100, "Vance");
        System.out.println("序列化：" + person.toString());

        Utils.serialize(person, path);
        
        Person person2 = (Person) Utils.unSerialize(path);
        
        System.out.println("反序列化：" + person2.toString());
        System.out.println("前后对象相同（==）吗？： " + (person == person2));

    }

    private static class Person implements Externalizable {
        private int id;
        //此时用 transient 修饰没有意义。因为自己控制序列化过程。 
        private transient String name;

        public Person() {
        }

        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        //实现序列化
        @Override public void writeExternal(ObjectOutput objectOutput) throws IOException {
            objectOutput.writeObject(name);
            objectOutput.writeObject(id);
        }

        //实现反序列化
        @Override public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
            name = (String) objectInput.readObject();
            id = (Integer) objectInput.readObject();
        }

        @Override public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
