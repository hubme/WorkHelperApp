package com.king.app.workhelper.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.contentprovider.BookProvider;
import com.king.app.workhelper.model.Book;

import butterknife.OnClick;

/**
 * @author Vanceking
 * @since 2018/5/9.
 */
public class ContentProviderActivity extends AppBaseActivity {
    private ContentObserver mContentObserver;

    @Override protected int getContentLayout() {
        return R.layout.activity_content_provider;
    }

    @Override protected void initData() {
        super.initData();

        mContentObserver = new BookContentObserver(this, mHandler);
        getContentResolver().registerContentObserver(BookProvider.BOOK_CONTENT_URI, true, mContentObserver);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mContentObserver);
    }

    @OnClick(R.id.tv_query)
    public void onQueryClick() {
        Cursor bookCursor = getContentResolver().query(BookProvider.BOOK_CONTENT_URI, new String[]{"_id", "name"}, null, null, null);
        StringBuilder sb = new StringBuilder();
        while (bookCursor.moveToNext()) {
            Book book = new Book(bookCursor.getInt(0), bookCursor.getString(1));
            sb.append(book.toString()).append("\n");
        }
        sb.append("--------------------------------").append("\n");
        bookCursor.close();

        Cursor userCursor = getContentResolver().query(BookProvider.USER_CONTENT_URI, new String[]{"_id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()) {
            sb.append(userCursor.getInt(0)).append(" ,")
                    .append(userCursor.getString(1)).append(" ,")
                    .append(userCursor.getInt(2))
                    .append("\n");
        }
        sb.append("--------------------------------");
        userCursor.close();
    }

    @OnClick(R.id.tv_add)
    public void onAddClick() {
        ContentValues values = new ContentValues();
        values.put("_id", 1123);
        values.put("name", "三国演义");
        getContentResolver().insert(BookProvider.BOOK_CONTENT_URI, values);
    }

    @OnClick(R.id.tv_delete)
    public void onDeleteClick() {
        getContentResolver().delete(BookProvider.BOOK_CONTENT_URI, "_id = 1123", null);
    }

    @OnClick(R.id.tv_update)
    public void onUpdateClick() {
        ContentValues values = new ContentValues();
        values.put("_id", 1123);
        values.put("name", "三国演义新版");
        getContentResolver().update(BookProvider.BOOK_CONTENT_URI, values, "_id = 1123", null);
    }


    private static class BookContentObserver extends ContentObserver {
        private Context context;

        public BookContentObserver(Context context, Handler handler) {
            super(handler);
            this.context = context;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show();
            super.onChange(selfChange, uri);
        }
    }
}
