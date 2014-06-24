package com.vivogaming.livecasino.screens.game;

import android.app.Activity;
import android.view.WindowManager;

public class ScreenWorker {
	/**
     * keep screen
     * Developer Sam
     * 2014年5月5日
     */
    public static void screenKeep(Activity _activity){
    	_activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	_activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
}
