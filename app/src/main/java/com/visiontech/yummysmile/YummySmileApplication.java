package com.visiontech.yummysmile;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * @author hector.torres
 */
public class YummySmileApplication extends Application {

    private static Bus eventBus;

    public static Bus getEventBus() {
        if(eventBus == null){
            eventBus = new Bus(ThreadEnforcer.ANY);
        }
        return eventBus;
    }
}
