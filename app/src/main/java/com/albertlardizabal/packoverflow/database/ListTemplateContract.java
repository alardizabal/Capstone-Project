package com.albertlardizabal.packoverflow.database;

import android.provider.BaseColumns;

/**
 * Created by albertlardizabal on 3/9/17.
 */

public final class ListTemplateContract {

    private ListTemplateContract() {}

    public static class ListTemplateEntry implements BaseColumns {
        public static final String TABLE_NAME = "template";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ITEM_TITLE = "item_title";
    }
}
