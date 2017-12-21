package com.king.app.workhelper.adapter.expandablelistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.workhelper.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VanceKing
 * @since 2017/12/21.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private String[] classes;
    private String[][] students;
    private Context context;
    private View.OnClickListener ivGoToChildClickListener;

    public MyExpandableListAdapter() {

    }

    public MyExpandableListAdapter(String[] classes, String[][] students, Context context) {
        this.classes = classes;
        this.students = students;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return classes.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return students[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return classes[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return students[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHold groupHold;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.elv_group_item, null);
            groupHold = new GroupHold();
            groupHold.tvGroupName = (TextView) convertView.findViewById(R.id.tv_groupName);
            groupHold.ivGoToChildLv = (ImageView) convertView.findViewById(R.id.iv_goToChildLV);

            convertView.setTag(groupHold);

        } else {
            groupHold = (GroupHold) convertView.getTag();

        }

        String groupName = classes[groupPosition];
        groupHold.tvGroupName.setText(groupName);

        //取消默认的groupIndicator后根据方法中传入的isExpand判断组是否展开并动态自定义指示器
        if (isExpanded) {   //如果组展开
            groupHold.ivGoToChildLv.setImageResource(R.drawable.icon_1);
        } else {
            groupHold.ivGoToChildLv.setImageResource(R.drawable.icon_2);
        }

        //setTag() 方法接收的类型是object ，所以可将position和converView先封装在Map中。Bundle中无法封装view,所以不用bundle
        Map<String, Object> tagMap = new HashMap<>();
        tagMap.put("groupPosition", groupPosition);
        tagMap.put("isExpanded", isExpanded);
        groupHold.ivGoToChildLv.setTag(tagMap);

        //图标的点击事件
        groupHold.ivGoToChildLv.setOnClickListener(ivGoToChildClickListener);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHold childHold;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.elv_child_item, null);
            childHold = new ChildHold();
            childHold.tvChildName = (TextView) convertView.findViewById(R.id.tv_elv_childName);
            childHold.cbElvChild = (CheckBox) convertView.findViewById(R.id.cb_elvChild);
            convertView.setTag(childHold);
        } else {
            childHold = (ChildHold) convertView.getTag();
        }

        String childName = students[groupPosition][childPosition];
        childHold.tvChildName.setText(childName);

        childHold.cbElvChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(context, "条目选中:" + classes[groupPosition] + "的" +
                            students[groupPosition][childPosition] + "被选中了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "条目选中:" + classes[groupPosition] + "的" +
                            students[groupPosition][childPosition] + "被取消选中了", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;    //默认返回false,改成true表示组中的子条目可以被点击选中
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    class GroupHold {
        TextView tvGroupName;
        ImageView ivGoToChildLv;
    }

    class ChildHold {
        TextView tvChildName;
        CheckBox cbElvChild;
    }
}
