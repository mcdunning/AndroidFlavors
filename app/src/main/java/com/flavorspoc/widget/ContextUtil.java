package com.flavorspoc.widget;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;

public class ContextUtil {
    public ContextUtil() {
    }

    public static Activity getActivity(Context context) {
        while(context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }

            context = ((ContextWrapper)context).getBaseContext();
        }

        return null;
    }

    public static Service getService(Context context) {
        while(context instanceof ContextWrapper) {
            if (context instanceof Service) {
                return (Service)context;
            }

            context = ((ContextWrapper)context).getBaseContext();
        }

        return null;
    }
}
