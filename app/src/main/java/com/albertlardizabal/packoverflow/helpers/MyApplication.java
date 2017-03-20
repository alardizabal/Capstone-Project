package com.albertlardizabal.packoverflow.helpers;

import android.app.Application;
import android.content.Context;

/**
 * Created by albertlardizabal on 3/20/17.
 */

public class MyApplication extends Application {

	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
	}

	public static Context getContext(){
		return context;
	}
}
