package com.example.third.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * @author VanceKing
 * @since 2020/2/17.
 */
class TranslationSample {
    private static final String EXCEL_FILE_PATH = "C:\\excel.xlsx";
    private static final String XML_SOURCE_FILE_PATH = "C:\\source.xml";
    private static final String XML_DEST_FILE_PATH = "C:\\dest.xml";
    private static final String SHEET_NAME = "SheetSource";

    private static final Map<String, String> ESCAPES = new HashMap<String, String>() {
        {
            put("&", "&amp;");
            //put("<", "&lt;");
            //put(">", "&gt;");
            //put("'", "&apos;");
            //put("\"", "&quot;");
        }
    };

    private static SAXReader reader;

    static {
        reader = new SAXReader();
        reader.setXMLFilter(new XMLFilterImpl() {
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (length == 1) {
                    String key = String.valueOf(ch[0]);
                    if (ESCAPES.containsKey(key)) {
                        System.out.println("特殊字符：" + key);
                        String escape = ESCAPES.get(key);
                        super.characters(escape.toCharArray(), 0, escape.length());
                    } else {
                        super.characters(ch, start, length);
                    }
                } else {
                    super.characters(ch, start, length);
                }

            }
        });
    }

    public static void main(String[] args) {
        read1();
    }

    private static void read1() {
        ExcelReader excelReader = EasyExcel.read(EXCEL_FILE_PATH, IndexOrNameData.class, new DemoDataListener()).build();
        ReadSheet readSheet = EasyExcel.readSheet(SHEET_NAME).build();
        excelReader.read(readSheet);
        excelReader.finish();
    }

    private static class DemoDataListener extends AnalysisEventListener<IndexOrNameData> {
        private final HashMap<String, IndexOrNameData> translationsMap = new HashMap<>(100);

        /**
         * 这个每一条数据解析都会来调用
         */
        @Override public void invoke(IndexOrNameData data, AnalysisContext analysisContext) {
            System.out.println("解析到一条数据: " + data.toString());
            translationsMap.put(data.getWrong_translation(), data);
        }

        /**
         * 所有数据解析完成了 都会来调用
         */
        @Override public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            //LOGGER.info("doAfterAllAnalysed");
            System.out.println("doAfterAllAnalysed");
            System.out.println("the length of translations is: " + translationsMap.size());
            /*IndexOrNameData translation = getTranslation(transitions, WRONG_TRANSLATION);
            if (translation != null) {
                System.out.println(WRONG_TRANSLATION + ": " + translation.toString());
            } else {
                System.out.println("没有翻译：" + WRONG_TRANSLATION);
            }*/
            if (!translationsMap.isEmpty()) {
                try {
                    write_xml_strings(translationsMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static IndexOrNameData getTranslation(List<IndexOrNameData> translations, String key) {
        if (translations == null || translations.isEmpty() || key == null || key.isEmpty()) {
            return null;
        }
        Optional<IndexOrNameData> first = translations.stream()
                .filter(indexOrNameData -> key.equals(indexOrNameData.getWrong_translation()))
                .findFirst();
        return first.orElse(null);
    }

    private static void write_xml_strings(Map<String, IndexOrNameData> translations) throws Exception {
        if (translations == null || translations.isEmpty()) {
            return;
        }

        Document document = reader.read(new File(XML_SOURCE_FILE_PATH));
        File file = new File(XML_DEST_FILE_PATH);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("目标文件已存在且无法删除。");
            }
        }
        FileOutputStream out = new FileOutputStream(XML_DEST_FILE_PATH);
        //OutputFormat format = OutputFormat.createCompactFormat();//生成物理文件，布局较乱适合电脑
        OutputFormat format = OutputFormat.createPrettyPrint();//标准化布局，适合查看时显示。
        format.setEncoding("utf-8");
        format.setIndent(true);//设置是否缩进
        //format.setIndent("");//设置缩进字符串
        format.setIndentSize(0);//设置缩进，空格为单位
        format.setPadText(false);//元素行首是否多一个空格
        format.setNewlines(false);//每个元素之间是否有空行
        format.setTrimText(false);//是否清除元素值得前后空格

        int translationsCount = 0;
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            String name = element.attributeValue("name");
            String text = element.getText();
            if (text == null || text.isEmpty()) {
                continue;
            }

            //包含需要修改的翻译
            if (translations.containsKey(text)) {
                IndexOrNameData data = translations.get(text);
                System.out.println(String.format("字符串 id: %s；英文字符串：%s；错误翻译：%s；正确翻译：%s",
                        name, data.getString_in_english(), data.getWrong_translation(), data.getCorrect_translation()));
                element.setText(data.getCorrect_translation());
                translationsCount++;
            }
        }

        XMLWriter writer = new XMLWriter(out, format);
        writer.setEscapeText(false);//不转义
        writer.write(document);
        System.out.println(String.format(Locale.US,
                "文件修改成功。共修改 %d 条翻译，修改后的文件路径：%s", translationsCount, XML_DEST_FILE_PATH));
        writer.close();
    }
}






















