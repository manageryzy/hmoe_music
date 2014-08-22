package org.hmoe.hm_music;

import org.hmoe.hm_music.CrashHandler.CrashHandler;

import android.app.Application;  
  
/** 
 * 应用的Application，用来自定义CrashHandler
 */  
public class HmoeApplication extends Application {  
    @Override  
    public void onCreate() {  
        super.onCreate();  
        CrashHandler crashHandler = CrashHandler.getInstance();  
        // 注册crashHandler  
        crashHandler.init(getApplicationContext());  
        // 发送以前没发送的报告(可选)  
        crashHandler.sendPreviousReportsToServer();  
    }  
}  