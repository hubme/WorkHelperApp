package com.example.third.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * EasyExcel 测试类。
 *
 * @author VanceKing
 * @since 2020/2/14.
 */
class EasyExcelTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelTest.class);

    public static void main(String[] args) {
        testRead();
    }

    private static void testRead() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource("ExcelDemo.xlsx").getFile());
        EasyExcel.read(file, DemoData.class, new DemoDataListener()).sheet().doRead();
    }

    private static class DemoDataListener extends AnalysisEventListener<DemoData> {

        /**
         * 这个每一条数据解析都会来调用
         */
        @Override public void invoke(DemoData demoData, AnalysisContext analysisContext) {
            LOGGER.info("解析到一条数据:" + demoData.toString());
        }

        /**
         * 所有数据解析完成了 都会来调用
         */
        @Override public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            LOGGER.info("doAfterAllAnalysed");
        }
    }

    public static class DemoData {
        private String string_id;
        private String values;
        private String values_temp;
        private String values_zh;
        private String values_qaag;

        public DemoData() {
        }

        public String getString_id() {
            return string_id;
        }

        public void setString_id(String string_id) {
            this.string_id = string_id;
        }

        public String getValues() {
            return values;
        }

        public void setValues(String values) {
            this.values = values;
        }

        public String getValues_temp() {
            return values_temp;
        }

        public void setValues_temp(String values_temp) {
            this.values_temp = values_temp;
        }

        public String getValues_zh() {
            return values_zh;
        }

        public void setValues_zh(String values_zh) {
            this.values_zh = values_zh;
        }

        public String getValues_qaag() {
            return values_qaag;
        }

        public void setValues_qaag(String values_qaag) {
            this.values_qaag = values_qaag;
        }

        @Override public String toString() {
            return "DemoData{" +
                    "string_id='" + string_id + '\'' +
                    ", values='" + values + '\'' +
                    ", values_temp='" + values_temp + '\'' +
                    ", values_zh='" + values_zh + '\'' +
                    ", values_qaag='" + values_qaag + '\'' +
                    '}';
        }

    }

}

