package com.king.applib.ui.recyclerview.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerView Item 事件处理
 *
 * @author VanceKing
 * @since 2018/1/10.
 */

public class RecyclerItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {
    private GestureDetectorCompat gestureDetectorCompat;

    public RecyclerItemTouchListener(RecyclerView recyclerView, OnRecyclerItemListener clickListener) {
        gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new RecyclerGestureListener(recyclerView, clickListener));
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    private class RecyclerGestureListener extends GestureDetector.SimpleOnGestureListener {
        private RecyclerView recyclerView;
        private OnRecyclerItemListener clickListener;

        private RecyclerGestureListener(RecyclerView recyclerView, OnRecyclerItemListener clickListener) {
            this.recyclerView = recyclerView;
            this.clickListener = clickListener;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && clickListener != null) {
                clickListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && clickListener != null) {
                clickListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
            }
        }
    }

    public interface OnRecyclerItemListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public static class OnRecyclerItemListenerAdapter implements OnRecyclerItemListener {

        @Override public void onItemClick(View view, int position) {

        }

        @Override public void onItemLongClick(View view, int position) {

        }
    }
}
