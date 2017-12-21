package com.king.app.workhelper.fragment;

import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.expandablelistview.MyExpandableListAdapter;
import com.king.app.workhelper.common.AppBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * ExpandableListView 的父View必须要指定确切的值。
 *
 * @author VanceKing
 * @since 2017/12/21.
 */
public class ExpandableListViewFragment extends AppBaseFragment {
    @BindView(R.id.expand_list_view) ExpandableListView mExpandListView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_expandable_list_view;
    }

    @Override protected void initData() {
        super.initData();

        simpleExpandableListAdapter();
//        customAdapter();
    }

    private void simpleExpandableListAdapter() {
        //组数据
        List<Map<String, String>> groups = new ArrayList<Map<String, String>>() {
            {
                //组1
                add(new HashMap<String, String>() {
                    {
                        put("groupkey", "aaa");
                    }
                });
                //组2
                add(new HashMap<String, String>() {
                    {
                        put("groupkey", "bbb");
                    }
                });
                //组3
                add(new HashMap<String, String>() {
                    {
                        put("groupkey", "ccc");
                    }
                });
            }
        };

        //组1的子数据
        List<Map<String, String>> child1Items = new ArrayList<Map<String, String>>() {
            {
                add(new HashMap<String, String>() {
                    {
                        put("name", "group1 child1");
                    }
                });
                add(new HashMap<String, String>() {
                    {
                        put("name", "group1 child2");
                    }
                });
                add(new HashMap<String, String>() {
                    {
                        put("name", "group1 child3");
                    }
                });
            }
        };
        //组2的子数据
        List<Map<String, String>> child2Items = new ArrayList<Map<String, String>>() {
            {
                add(new HashMap<String, String>() {
                    {
                        put("name", "group2 child1");
                    }
                });
                add(new HashMap<String, String>() {
                    {
                        put("name", "group2 child2");
                    }
                });
                add(new HashMap<String, String>() {
                    {
                        put("name", "group2 child3");
                    }
                });
            }
        };

        //组2的子数据
        List<Map<String, String>> child3Items = new ArrayList<Map<String, String>>() {
            {
                add(new HashMap<String, String>() {
                    {
                        put("name", "group3 child1");
                    }
                });
                add(new HashMap<String, String>() {
                    {
                        put("name", "group3 child2");
                    }
                });
                add(new HashMap<String, String>() {
                    {
                        put("name", "group3 child3");
                    }
                });
            }
        };


        List<List<Map<String, String>>> childItems = new ArrayList<List<Map<String, String>>>() {
            {
                add(child1Items);
                add(child2Items);
                add(child3Items);
            }
        };

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(getContext(), groups, R.layout.elv_group_item,
                new String[]{"groupkey"}, new int[]{R.id.tv_groupName}, childItems, R.layout.elv_child_item, new String[]{"name"}, new int[]{R.id.tv_elv_childName});
        mExpandListView.setAdapter(adapter);
    }

    private void customAdapter() {
        //模拟数据（数组，集合都可以，这里使用数组）
        final String[] classes = new String[]{"一班", "二班", "三班", "四班", "五班"};
        final String[][] students = new String[][]{{"张三1", "李四1", "王五1", "赵六1", "钱七1", "高八1"}, {"张三1", "李四1", "王五1",
                "赵六1", "钱七1", "高八1"}, {"张三1", "李四1", "王五1", "赵六1", "钱七1", "高八1"}, {"张三1", "李四1", "王五1", "赵六1", "钱七1",
                "高八1"}, {}};

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(classes, students, getContext());
        mExpandListView.setAdapter(adapter);
    }
}
