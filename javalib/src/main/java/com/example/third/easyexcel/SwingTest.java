package com.example.third.easyexcel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * @author VanceKing
 * @since 2020/2/18.
 */
public class SwingTest {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Java标签组件示例");    //创建Frame窗口
        JPanel jp = new JPanel();    //创建面板
        JLabel label1 = new JLabel("普通标签");    //创建标签
        JLabel label2 = new JLabel();
        label2.setText("调用setText()方法");
        ImageIcon img = new ImageIcon("f:\\image.jpg");    //创建一个图标
        //创建既含有文本又含有图标的JLabel对象
        JLabel label3 = new JLabel("Hello World!", img, JLabel.CENTER);
        jp.add(label1);    //添加标签到面板
        jp.add(label2);
        jp.add(label3);
        frame.add(jp);
        frame.setBounds(300, 200, 400, 100);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
