package org.hmoe.hm_music;

import android.app.Application;  
  
/** 
 * �ڿ���Ӧ��ʱ�����Activity�򽻵�����Applicationʹ�õľ���Խ����ˡ� 
 * Application����������Ӧ�ó����ȫ��״̬�ģ�����������Դ�ļ��� 
 * ��Ӧ�ó���������ʱ��Application�����ȴ�����Ȼ��Ż�������(Intent)������Ӧ��Activity����Service�� 
 * �ڱ��Ľ���Application��ע��δ�����쳣�������� 
 */  
public class HmoeApplication extends Application {  
    @Override  
    public void onCreate() {  
        super.onCreate();  
        CrashHandler crashHandler = CrashHandler.getInstance();  
        // ע��crashHandler  
        crashHandler.init(getApplicationContext());  
        // ������ǰû���͵ı���(��ѡ)  
        crashHandler.sendPreviousReportsToServer();  
    }  
}  