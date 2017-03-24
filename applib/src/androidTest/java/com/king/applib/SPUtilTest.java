package com.king.applib;

import com.king.applib.util.ContextUtil;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.SPUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author huoguangxu
 * @since 2017/3/6.
 */

public class SPUtilTest extends BaseTestCase {
    @Override protected void setUp() throws Exception {
        super.setUp();
        ContextUtil.init(mContext);
    }

    public static class TestOo {
        public String name;
        public int age;

        public TestOo(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override public String toString() {
            return "TestOo{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public void testPutStringList() throws Exception {
        List<String> stringList = Arrays.asList("哈哈哈", "呵呵呵", "哦哦哦");
        SPUtil.putStringList("1111", stringList);
    }

    public void testGetStringList() throws Exception {
        List<String> stringList = SPUtil.getStringList("1111");
        ExtendUtil.printList(stringList);
    }

    public void testPutListToSp() throws Exception {
        List<TestOo> listOo  = Arrays.asList(new TestOo("1111", 1111), new TestOo("2222", 2222), new TestOo("3333", 3333));
        SPUtil.putListToSp("2222", listOo);
//        SPUtil.putListToSp("3333", Arrays.asList(1, 2, 3));
//        List<Integer> listFromSp = SPUtil.getListFromSp("3333", Integer.class);
//        ExtendUtil.printList(listFromSp);
    }

    public void testGetListFromSp() throws Exception {
        List<TestOo> listFromSp = SPUtil.getListFromSp("2222", TestOo.class);
        ExtendUtil.printList(listFromSp);
    }

    public void testSPUtils() throws Exception {
        SPUtil.putBoolean("aaaaaaaaa", true);
//        SPUtil.putBoolean("boolean", false);
//        SPUtil.putFloat("float", 5.20f);
//        SPUtil.putInt("int", 100);
//        SPUtil.putLong("long", 321651);
//        SPUtil.putString("string", "双方而上方的");
//
//        Set<String> texts = new HashSet<>();
//        texts.add("董藩");
//        texts.add("带看佛陈");
//        texts.add("董双管");
//        texts.add("；困啊");
//        SPUtil.getSP(mContext).edit().putStringSet("list_set", texts).apply();
//
//        List<Integer> intList = new ArrayList<>();
//        intList.add(1);
//        intList.add(2);
//        intList.add(3);
//        SPUtil.putIntList("int_list", intList);
//
//        List<String> stringList = new ArrayList<>();
//        stringList.add("我##fdf");
//        stringList.add("是");
//        stringList.add("谁");
//        SPUtil.putStringList("string_list", stringList);


//        SPUtil.clear(mContext);

//        SPUtil.remove("string_list");
//        SPUtil.putInt("int", 999);

//        ExtendUtil.printList(SPUtil.getStringList("string_list"));
//        ExtendUtil.printList(SPUtil.getIntList("int_list"));
//        Logger.i(SPUtil.getInt("sdjfo")+"");


//        SPUtil.getSP(mContext).edit().putBoolean("aaa", true).putString("bbb", "龙胆啊")
//                .putLong("ccc", 7).putInt("ddd", 678).putFloat("eee", 20.222f).apply();
    }

}
