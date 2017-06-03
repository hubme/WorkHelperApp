package com.king.app.workhelper.model;

import java.util.List;

/**
 * @author VanceKing
 * @since 2017/6/3.
 */

public class ServiceModel {
    public List<EntryGroupModel> entrys;

    public static class EntryGroupModel {
        public String firsttitle;
        public List<EntryItemModel> content;
    }

}
