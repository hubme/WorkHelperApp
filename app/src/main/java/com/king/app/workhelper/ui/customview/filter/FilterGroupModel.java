package com.king.app.workhelper.ui.customview.filter;

import java.util.List;
import java.util.Objects;

public class FilterGroupModel {
    private String title;
    private boolean isExpanded;
    private List<FilterItem> conditions;

    public FilterGroupModel() {
    }

    public FilterGroupModel(String title) {
        this.title = title;
    }

    public FilterGroupModel(String title, boolean isExpanded, List<FilterItem> conditions) {
        this.title = title;
        this.isExpanded = isExpanded;
        this.conditions = conditions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<FilterItem> getConditions() {
        return conditions;
    }

    public void setConditions(List<FilterItem> conditions) {
        this.conditions = conditions;
    }

    public static class FilterItem {
        private String text;
        private boolean isChecked;

        public FilterItem() {
        }

        public FilterItem(String text, boolean isChecked) {
            this.text = text;
            this.isChecked = isChecked;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FilterItem that = (FilterItem) o;
            return text.equals(that.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text);
        }
    }

}
