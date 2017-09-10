package com.king.app.workhelper.common;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

/**
 * @author VanceKing
 * @since 2017/5/18.
 */

public class PassTransformationMethod implements TransformationMethod{
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return "* | * | *******";
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

    }
}
