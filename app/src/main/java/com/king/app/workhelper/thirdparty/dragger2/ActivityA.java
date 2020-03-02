package com.king.app.workhelper.thirdparty.dragger2;

import com.king.app.workhelper.app.WorkHelperApp;
import com.king.applib.log.Logger;

import javax.inject.Inject;

/**
 * @author VanceKing
 * @since 2020/2/28.
 */
public class ActivityA {
    @Inject
    Heater heater;
    @Inject
    Heater heater2;

    public ActivityA() {
        DaggerHeaterComponent.builder().baseComponent(((WorkHelperApp) WorkHelperApp.getApplication())
                .getBaseComponent()).build().inject(this);
    }

    public void printHeater() {
        Logger.i("heater in ActivityA: " + heater.toString());
        Logger.i("heater2 in ActivityA: " + heater2.toString());
    }
}
