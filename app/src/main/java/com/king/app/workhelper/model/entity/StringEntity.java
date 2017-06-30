package com.king.app.workhelper.model.entity;

import android.support.annotation.IntDef;

/**
 * @author huoguangxu
 * @since 2017/3/30.
 */

public class StringEntity {
    @IntDef({ItemType.UNKNOWN, ItemType.CONTENT, ItemType.CATEGORY})
    public @interface ItemType {
        int UNKNOWN = -1;
        int CONTENT = 1;
        int CATEGORY = 2;
    }

    @ItemType public int type;
    public String text;

    public StringEntity(String text) {
        this.type = ItemType.CONTENT;
        this.text = text;
    }

    public StringEntity(String text, @ItemType int type) {
        this.type = type;
        this.text = text;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringEntity)) return false;

        StringEntity that = (StringEntity) o;

        if (type != that.type) return false;
        return text != null ? text.equals(that.text) : that.text == null;

    }

    @Override public int hashCode() {
        int result = type;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
