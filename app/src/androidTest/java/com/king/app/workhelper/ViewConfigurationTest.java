package com.king.app.workhelper;

import android.view.ViewConfiguration;

import com.king.applib.log.Logger;

/**
 * @author VanceKing
 * @since 2017/5/29 0029.
 */

public class ViewConfigurationTest extends BaseTestCase {
    public void testAAA() throws Exception {
        Logger.i("getScaledOverflingDistance: " + ViewConfiguration.get(mContext).getScaledOverflingDistance());
        Logger.i("getScaledDoubleTapSlop: " + ViewConfiguration.get(mContext).getScaledDoubleTapSlop());

    }
}
