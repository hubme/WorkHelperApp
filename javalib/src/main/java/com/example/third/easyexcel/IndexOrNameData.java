package com.example.third.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author VanceKing
 * @since 2020/2/17.
 */
public class IndexOrNameData {
    /**
     * 强制读取第三个 这里不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
     */
    @ExcelProperty(index = 4)
    private String string_path_in_english;
    /**
     * 用名字去匹配，这里需要注意，如果名字重复，会导致只有一个字段读取到数据
     */
    @ExcelProperty("Strings in English")
    private String string_in_english;

    @ExcelProperty("Module")
    private String module;

    @ExcelProperty("Wrong translation")
    private String wrong_translation;

    @ExcelProperty("Correct Translation")
    private String correct_translation;

    public String getString_path_in_english() {
        return string_path_in_english;
    }

    public void setString_path_in_english(String string_path_in_english) {
        this.string_path_in_english = string_path_in_english;
    }

    public String getString_in_english() {
        return string_in_english;
    }

    public void setString_in_english(String string_in_english) {
        this.string_in_english = string_in_english;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getWrong_translation() {
        return wrong_translation;
    }

    public void setWrong_translation(String wrong_translation) {
        this.wrong_translation = wrong_translation;
    }

    public String getCorrect_translation() {
        return correct_translation;
    }

    public void setCorrect_translation(String correct_translation) {
        this.correct_translation = correct_translation;
    }

    @Override public String toString() {
        return "IndexOrNameData{" +
                "string_path_in_english='" + string_path_in_english + '\'' +
                ", string_in_english='" + string_in_english + '\'' +
                ", module='" + module + '\'' +
                ", wrong_translation='" + wrong_translation + '\'' +
                ", correct_translation='" + correct_translation + '\'' +
                '}';
    }
}
