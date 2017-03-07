package com.king.applib;

import com.king.applib.util.ExtendUtil;
import com.king.applib.util.SPUtil;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author huoguangxu
 * @since 2017/3/6.
 */

public class SPUtilTest extends BaseTestCase{

    public static class TestOo{
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
    public void testPutStringList() throws Exception{
        List<String> stringList = asList("哈哈哈", "呵呵呵", "哦哦哦");
//        SPUtil.putStringList(mContext, "1111", stringList);
        List<String> strings = stringList.subList(0, 1);
        ExtendUtil.printList(strings);
    }
    
    public void testGetStringList() throws Exception {
        List<String> stringList = SPUtil.getStringList(mContext, "1111");
        ExtendUtil.printList(stringList);
    }
    
    public void testPutListToSp() throws Exception {
//        List<TestOo> listOo  = Arrays.asList(new TestOo("1111", 1111), new TestOo("2222", 2222), new TestOo("3333", 3333));
//        SPUtil.putListToSp(mContext, "2222", listOo);
//        SPUtil.putListToSp(mContext, "3333", Arrays.asList(1, 2, 3));
        List<Integer> listFromSp = SPUtil.getListFromSp(mContext, "3333", Integer.class);
        ExtendUtil.printList(listFromSp);
    }
    
    public void testGetListFromSp() throws Exception {
        List<TestOo> listFromSp = SPUtil.getListFromSp(mContext, "2222", TestOo.class);
        ExtendUtil.printList(listFromSp);
    }
    
    public void testSPUtils() throws Exception {
        SPUtil.putBoolean(mContext, "aaaaaaaaa", true);
//        SPUtil.putBoolean(mContext, "boolean", false);
//        SPUtil.putFloat(mContext, "float", 5.20f);
//        SPUtil.putInt(mContext, "int", 100);
//        SPUtil.putLong(mContext, "long", 321651);
//        SPUtil.putString(mContext, "string", "双方而上方的");
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
//        SPUtil.putIntList(mContext, "int_list", intList);
//
//        List<String> stringList = new ArrayList<>();
//        stringList.add("我##fdf");
//        stringList.add("是");
//        stringList.add("谁");
//        SPUtil.putStringList(mContext, "string_list", stringList);


//        SPUtil.clear(mContext);

//        SPUtil.remove(mContext, "string_list");
//        SPUtil.putInt(mContext, "int", 999);

//        ExtendUtil.printList(SPUtil.getStringList(mContext, "string_list"));
//        ExtendUtil.printList(SPUtil.getIntList(mContext, "int_list"));
//        Logger.i(SPUtil.getInt(mContext, "sdjfo")+"");


//        SPUtil.getSP(mContext).edit().putBoolean("aaa", true).putString("bbb", "龙胆啊")
//                .putLong("ccc", 7).putInt("ddd", 678).putFloat("eee", 20.222f).apply();
    }
    
}
