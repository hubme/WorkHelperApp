package com.example.third.dom4j;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * dom4j 测试类。
 *
 * @author VanceKing
 * @since 2020/2/14.
 */
class Dom4jTest {
    private static SAXReader reader;

    static {
        reader = new SAXReader();
        reader.setXMLFilter(new XMLFilterImpl() {
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                String text = new String(ch, start, length);
                System.out.println("text is: " + text);

                if (length == 1) {
                    if ((int) ch[0] == 64) {
                        char[] escape = "&#064;".toCharArray();
                        super.characters(escape, 0, escape.length);
                        return;
                    }
                }

                super.characters(ch, start, length);
            }
        });
    }

    public static void main(String[] args) {

    }

    public static void read_xml_demo() throws Exception {
        Document document = reader.read(new File("c:\\test_xml.xml"));

        //获取整个文档
        Element rootElement = document.getRootElement();

        //获取Response节点的Result属性值
        String responseResult = rootElement.attributeValue("Result");

        //获取第一个Media元素
        Element mediaElement = rootElement.element("Media");
        //获取所有的Media元素
        List allMeidaElements = rootElement.elements("Media");


        //获取第一个Media元素的Name属性值
        String mediaName = mediaElement.attributeValue("Name");
        //遍历所有的Media元素的Name属性值
        for (int i = 0; i < allMeidaElements.size(); i++) {
            Element element = (Element) allMeidaElements.get(i);
            String elementName = element.getName();
            String name = element.attributeValue("Name");
            String text = element.getText();
            System.out.println("elementName: " + elementName + ", name: " + name + ", text: " + text);
        }
    }

    public static void read_xml_strings() throws Exception {
        Document document = reader.read(new File("strings.xml"));
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            String name = element.attributeValue("name");
            String text = element.getText();
            System.out.println(name + " " + text);

            if ("app_name".equals(name)) {
                element.setText("VanceKing");
            }
        }

    }

    public static void write_xml_strings() throws Exception {
        Document document = reader.read(new File("c:\\strings.xml"));
        FileOutputStream out = new FileOutputStream("./src/test/dom_test.xml");
        //OutputFormat format = OutputFormat.createCompactFormat();//生成物理文件，布局较乱适合电脑
        OutputFormat format = OutputFormat.createPrettyPrint();//标准化布局，适合查看时显示。
        format.setEncoding("utf-8");

        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            String name = element.attributeValue("name");
            String text = element.getText();
            System.out.println(name + " " + text);

            if ("app_name".equals(name)) {
                element.setText("VanceKing");
            }
        }


        XMLWriter writer = new XMLWriter(out, format);
        writer.setEscapeText(false);//不转义
        writer.write(document);
        System.out.println("写入成功");
        writer.close();


    }
}
