package org.hmoe.hm_music;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.hmoe.hm_music.R;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

public class _hmMusic extends Activity implements AnimationListener {
	
	private static final int MSG_SUCCESS = 0;  
    private static final int MSG_FAILURE = 1; 
    private static final int MSG_SHOW_MENU=2;
    private static final int MSG_DOWNLOAD_FINISH=3;
	
	private List<String> titleTagList = new ArrayList<String>();
	private WebView mWebView;  
	private MediaPlayer mp= new MediaPlayer();
	private ImageView  imageView = null;   
	private Animation alphaAnimation = null;   
	private Button btnYes=null;
	private Button btnNo=null;
	public static config conf;
	private static SQLiteDatabase db = null;
	
	
	private Handler mHandler = new Handler(){  
        @Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case MSG_SUCCESS:  
                
                Toast.makeText(getApplication(), "���ظ����ɹ�"+(String)msg.obj, Toast.LENGTH_LONG).show();  
                mWebView.loadUrl("javascripr:onDownloadSucceed()");
                break;   
  
            case MSG_FAILURE:    
            	mWebView.loadUrl("javascript:onDownloadError("+(String)msg.obj+")");
                Toast.makeText(getApplication(), "���ظ���ʧ��"+(String)msg.obj, Toast.LENGTH_LONG).show(); 
                break;  
            case MSG_SHOW_MENU:
            	openOptionsMenu();
            	break;
            case MSG_DOWNLOAD_FINISH:
            	Toast.makeText(getApplicationContext(), "ȫ�������������",Toast.LENGTH_SHORT).show();
            	mWebView.loadUrl("javascripr:onDownloadAllFinish()");
            	break;
            }  
            super.handleMessage(msg);  
        }  
          
    };  
    
    private void init()
    {
    	conf =new config(this);//��δ���ŵ�Activity���в���this
		
		if(db==null)db = conf.getReadableDatabase();
		
        if(!conf.loadConfigFromDB(db))
        {
        	Toast.makeText(getApplicationContext(), "���ݿ��ȡ�쳣",Toast.LENGTH_SHORT).show();;
        }
        if(!conf.saveConfigToDB(db))
        {
        	Toast.makeText(getApplicationContext(), "���ݿ�д���쳣",Toast.LENGTH_SHORT).show();;
        }
        
        if(conf.setting.get("screen_type").equals("pad"))
        {
        	conf.mainFrameUri=conf.HD_Web_Uri;
        }
        else
        {
        	conf.mainFrameUri=conf.Web_Uri;
        }
        
        conf.nameList=new LinkedList<String>();
		conf.uriList=new LinkedList<String>();
		conf.downloadingNameList=new LinkedList<String>();
		conf.downLoadingUriList=new LinkedList<String>();
		conf.offlineNameList=new LinkedList<String>();
		conf.offlineUriList=new LinkedList<String>();
		if(!conf.getCacheData(db, conf.offlineNameList, conf.offlineUriList))
			Toast.makeText(getApplicationContext(), "��ȡ�������ݴ���",Toast.LENGTH_SHORT).show();
		conf.offlineCount=conf.offlineNameList.size();
    }

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.welcome);   
		
		if(conf==null)init();
        
        addTitleTagList();
        
        imageView = (ImageView)findViewById(R.id.welcome_image_view);   
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);   
        alphaAnimation.setFillEnabled(true); //����Fill����   
        alphaAnimation.setFillAfter(true);  //���ö��������һ֡�Ǳ�����View����   
        imageView.setAnimation(alphaAnimation);   
        alphaAnimation.setAnimationListener(this);  //Ϊ�������ü���   
		
	}
	
	final class DemoJavaScriptInterface {  
        
        
        
		DemoJavaScriptInterface() {
			
		}  
        
        
        @JavascriptInterface
        public String setPlayerSongUri(String uri)
        {
        	try {
        		mp.stop();
        		mp.reset();
				mp.setDataSource(uri);
				new Thread(){  
	                public void run(){  
	                	try {
							mp.prepare();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
	    				mp.start();
	                }  
	            }.start();  
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return "false";
			} catch (SecurityException e) {
				e.printStackTrace();
				return "false";
			} catch (IllegalStateException e) {
				e.printStackTrace();
				return "false";
			} catch (IOException e) {
				e.printStackTrace();
				return "false";
			}
        	return "true";
        }
        
        @JavascriptInterface
        public String setPlayerSongIndex(int index)
        {
        	
        	try {
        		mp.stop();
        		mp.reset();
				mp.setDataSource(conf.uriList.get(index));
				if(conf.setting.get("ifAutoDownload").equals("true"))
				{
					addToDownloadList(conf.nameList.get(index),conf.uriList.get(index));
				}
				new Thread(){  
	                public void run(){  
	                	try {
							mp.prepare();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							// 
							e.printStackTrace();
						}
	    				mp.start();
	                }  
	            }.start();  
	            
	            conf.nowPlayingIndex=index;
			} catch (IllegalArgumentException e) {
				// 
				e.printStackTrace();
				return "false";
			} catch (SecurityException e) {
				e.printStackTrace();
				return "false";
			} catch (IllegalStateException e) {
				e.printStackTrace();
				return "false";
			} catch (IOException e) {
				e.printStackTrace();
				return "false";
			}
        	return "true";
        }
        
        @JavascriptInterface
        public String test()
        {
        	return "true";
        }
        
        @JavascriptInterface
        public void play()
        {
        	mp.start();
        }
        
        @JavascriptInterface
        public void pause()
        {
        	mp.pause();
        }
        
        @JavascriptInterface
        public void stop()
        {
        	mp.stop();
        }
        
        @JavascriptInterface
        public void seekTo(int time)
        {
        	mp.seekTo(time);
        }
        
        @JavascriptInterface
        public String getTime()
        {
        	return String.valueOf(mp.getCurrentPosition());
        }
        
        @JavascriptInterface
        public void addPlayList(String name,String uri)
        {
        	conf.nameList.add(name);
        	conf.uriList.add(uri);
        	conf.counts++;
        }
        
        @JavascriptInterface
        public String getPlayingIndex()
        {
	        return String.valueOf(conf.nowPlayingIndex);
        }
        
        @JavascriptInterface
        public String getApiLevel()
        {
        	return String.valueOf(conf.apiVerson);
        }
        
        @JavascriptInterface
        public String getDownloadCounts()
        {
        	return String.valueOf(conf.downloadCounts);
        }
        
        @JavascriptInterface
        public String getDownloadName(int index)
        {
        	return conf.downloadingNameList.get(index);
        }
        
        @JavascriptInterface
        public String getDownloadUri(int index)
        {
        	return conf.downLoadingUriList.get(index);
        }
        
        @JavascriptInterface
        public String getNowDownloading()
        {
        	return String.valueOf(conf.nowDownloading);
        }
        
        @JavascriptInterface
        public void addToDownloadList(String name,String uri)
        {
        	if(!conf.downloadingNameList.contains(name))
        	{
        		conf.downloadingNameList.add(name);
        		conf.downLoadingUriList.add(uri);
        		conf.downloadCounts++;
        		if(conf.downloadCounts-conf.nowDownloading>=0)
        			new Thread(){  
	                	public void run(){
	                		while(conf.downloadCounts-conf.nowDownloading>0)
	                		{
	                			String uri=conf.downLoadingUriList.get(conf.nowDownloading),name=conf.downloadingNameList.get(conf.nowDownloading);
	                			if(uri!=null&&name!=null)	                			
	                				if(downloadUriToFile(uri,name))
	                				{
	                					conf.offlineNameList.add(name);
	                					conf.offlineUriList.add(uri);
	                					conf.offlineCount++;
	                					conf.addCacheData(db, name, uri);
	                				}
	                			
	                			conf.nowDownloading++;
	                		}
	                		mHandler.obtainMessage(MSG_DOWNLOAD_FINISH).sendToTarget();
	                	}  
	            	}.start();  
	            
        	}
        	else
        	{
        		Toast.makeText(getApplicationContext(), "�Ѿ��������б���",Toast.LENGTH_SHORT).show();
        	}
        }
        
        @JavascriptInterface
        public String getPlayListCounts()
        {
        	return String.valueOf(conf.counts);
        }
        
        @JavascriptInterface
        public String getPlayListName(int index)
        {
        	return conf.nameList.get(index);
        }
        
        @JavascriptInterface
        public String getPlayListUri(int index)
        {
        	return conf.nameList.get(index);
        }
        
        @JavascriptInterface
        public String getConfig(String name)
        {
        	return conf.setting.get(name);
        }
        
        @JavascriptInterface
        public String setConfig(String name,String value)
        {
        	try {
				conf.setting.put(name, value);
				conf.saveConfigToDB(db);
			} catch (Exception e) {
				e.printStackTrace();
				return "false";
			}
        	return "true";
        }
        
        @JavascriptInterface
        public String cancleDownloadFile(int index)
        {
        	try {
				conf.downloadingNameList.remove(index);
				conf.downLoadingUriList.remove(index);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "false";
			}
        	return "true";
        }
        
        @JavascriptInterface
        public void showMenu()
        {
        	mHandler.obtainMessage(MSG_SHOW_MENU).sendToTarget();
        }
        
        @JavascriptInterface
        public void exitProgram()
        {
        	stopService(new Intent("org.hmoe.hm_music.SERVICE_DEMO"));
			NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancelAll();
			System.exit(0);
        }
        
        @JavascriptInterface
        public String getOfflineCount()
        {
        	return String.valueOf(conf.offlineCount);
        }
        
        @JavascriptInterface
        public String getOfflineName(int index)
        {
        	return conf.offlineNameList.get(index);
        }
        
        @JavascriptInterface
        public String getOfflineUri( int index)
        {
        	return conf.offlineUriList.get(index);
        }
        
        @JavascriptInterface
        public String delOffline(int index)
        {
        	
        	try {
				File file = new File(conf.offlineNameList.get(index));
				if (file.exists()) {
					file.delete();
				}
				conf.delCacheData(db, conf.offlineNameList.get(index));
				conf.offlineUriList.remove(index);
				conf.offlineNameList.remove(index);
				conf.offlineCount--;
			} catch (Exception e) {
				try {
					// TODO: handle exception
					conf.delCacheData(db, conf.offlineNameList.get(index));
					conf.offlineUriList.remove(index);
					conf.offlineNameList.remove(index);
					conf.offlineCount--;
					return "true";
				} catch (Exception e2) {
					// TODO: handle exception
					e.printStackTrace();
					return "false";
				}
		
			}
			return "true";
        }
    }  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu._hm_music, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId())//�õ��������item��itemId
		{
		case R.id.action_settings: //��Ӧ��ID������add���������趨��Id
			setContentView(R.layout.setting);
			CheckBox boolSetting=(CheckBox)findViewById(R.id.ifUseGPRS);
			if(conf.setting.get("ifRunGPRS").equals("true"))
				boolSetting.setChecked(true);
			
			boolSetting=(CheckBox)findViewById(R.id.ifHD);
			if(conf.setting.get("screen_type").equals("pad"))
				boolSetting.setChecked(true);
			
			boolSetting=(CheckBox)findViewById(R.id.ifAutoDownload);
			if(conf.setting.get("ifAutoDownload").equals("true"))
				boolSetting.setChecked(true);
			
			boolSetting=(CheckBox)findViewById(R.id.DownloadLocation);
			if(!conf.setting.get("cacheLocation").equals( Environment.getExternalStorageDirectory().getPath() +"/hmmusic/"))
				boolSetting.setChecked(true);
			
			btnYes=(Button)findViewById(R.id.button1);
	        btnNo=(Button)findViewById(R.id.button2);
	        btnYes.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v)
	        	{
	        		CheckBox boolSetting=(CheckBox)findViewById(R.id.ifUseGPRS);
	        		if(boolSetting.isChecked())conf.setting.put("ifRunGPRS","true");
	        		else conf.setting.put("ifRunGPRS","false");
	        		
	        		boolSetting=(CheckBox)findViewById(R.id.ifHD);
	        		if(boolSetting.isChecked())conf.setting.put("screen_type","pad");
	        		else conf.setting.put("screen_type","phone");
	        		
	        		boolSetting=(CheckBox)findViewById(R.id.ifAutoDownload);
	        		if(boolSetting.isChecked())conf.setting.put("ifAutoDownload","true");
	        		else conf.setting.put("ifAutoDownload","false");
	        		
	        		boolSetting=(CheckBox)findViewById(R.id.DownloadLocation);
	        		if(boolSetting.isChecked())conf.setting.put("cacheLocation",Environment.getExternalStorageDirectory().getPath() +"/hmmusic/");
	        		else conf.setting.put("cacheLocation",Environment.getExternalStorageDirectory().getPath() +"/hmmusic/");
	        		
	        		conf.saveConfigToDB(db);
	        		
	        		onResume();
	        		setContentView(R.layout.activity__hm_music);
	        		initWebView();
	        		if(conf.setting.get("screen_type").equals("pad"))
	                {
	                	conf.mainFrameUri=conf.HD_Web_Uri;
	                }
	                else
	                {
	                	conf.mainFrameUri=conf.Web_Uri;
	                }
	        		mWebView.loadUrl(conf.mainFrameUri);
	        		if(mWebView!=null)mWebView.loadUrl("javascript:onResume()");
	        	}
			});
	        btnNo.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v)
	        	{
	        		setContentView(R.layout.activity__hm_music);
	        		initWebView();
	        		if(mWebView!=null)mWebView.loadUrl("javascript:onResume()");
	        	}
			});
			break;
		case R.id.item1:
			stopService(new Intent("org.hmoe.hm_music.SERVICE_DEMO"));
			NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancelAll();
			System.exit(0);
			break;
		}
		return true;
	}

	
	@Override
	protected void onResume() {
	 
		if(conf.setting.get("screen_type").equals("pad"))
		{
			/*** ����Ϊ����  */
			if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
			{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}
		else
		{
			/*** ����Ϊ����  */
			if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
	 
	 if(this.mWebView!=null)
		 this.mWebView.loadUrl("javascript:onResume()");
	 super.onResume();
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true);//true���κ�Activity������
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	
	@Override  
    public void onAnimationStart(Animation animation) {   
           
    }   
	
	@SuppressLint("SetJavaScriptEnabled")
	public void initWebView()
	{
		mWebView = (WebView) findViewById(R.id.webView1);       
        WebSettings webSettings = mWebView.getSettings();       
        webSettings.setJavaScriptEnabled(true);    
        webSettings.setSupportZoom(false);  
        webSettings.setAppCacheEnabled(true);

        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  
        //	webSettings.setWebContentsDebuggingEnabled(true);  
        //}  
       // webSettings.setAppCacheMaxSize(999999999);
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(),"android"); 
        
        WebViewClient  mWebviewclient = new WebViewClient(){  
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){  
                 handler.proceed();  
            }  
        };  
        mWebView.setWebViewClient(mWebviewclient);  
        mWebView.loadUrl(conf.mainFrameUri);
	}
	
       
    @SuppressWarnings("deprecation")
	@Override  
    public void onAnimationEnd(Animation animation) {   
        //��������ʱ������ӭ���沢ת�������������   
    	setContentView(R.layout.activity__hm_music);
		
		try{
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
					.build());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		ConnectivityManager connectMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
         
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if(info==null)
        {
        	Toast.makeText(getApplicationContext(), "�޿�������",Toast.LENGTH_SHORT).show();
        	Dialog dialog=new AlertDialog.Builder(this).setTitle("�޿�������").setMessage("û��������������ɵ�DA��ZE").setPositiveButton("ȷ��",
        			new DialogInterface.OnClickListener() {
        	      	@Override
        	      	public void onClick(DialogInterface dialog, int which) {
        	      		System.exit(0);
        	      	}
        	     }).create();
        	dialog.show();  
        	
        	return;
        }
        if(info.getType() != ConnectivityManager.TYPE_WIFI)
        {
        	Dialog dialog=new AlertDialog.Builder(this).setTitle("����ʹ���ƶ�����").setMessage("û��������������ɵ�DA��ZE").setPositiveButton("ȷ��",
        		new DialogInterface.OnClickListener() {
    	      	@Override
    	      	public void onClick(DialogInterface dialog, int which) {
    	      		System.exit(0);
    	      	}
    	    }).create(); 
        	if(conf.setting.get("ifRunGPRS").equals("false"))dialog.show();  
        	Toast.makeText(getApplicationContext(), "����ʹ���ƶ����磬���������������Ѱ�",Toast.LENGTH_SHORT).show();
        	Toast.makeText(getApplicationContext(), "һҹû�����������Ӿ����ƶ����ˡ���",Toast.LENGTH_SHORT).show();
        	
        	if(conf.setting.get("ifRunGPRS").equals("false"))return;
        }
        
        
		startService(new Intent("org.hmoe.hm_music.SERVICE_DEMO")); 
		
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);               
		Notification n = new Notification(R.drawable.ic_launcher, "h����������!", System.currentTimeMillis());             
		n.flags = Notification.FLAG_NO_CLEAR;                
		Intent i = new Intent(getApplicationContext(), _hmMusic.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);           
		//PendingIntent
		//i.setAction(Intent.ACTION_MAIN);
		//i.setAction(Intent.ACTION_DEFAULT);
        //i.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent  = PendingIntent.getActivity(this, 0, i, 0);  
		Random random = new Random();
		
		                 
		n.setLatestEventInfo(
				getApplicationContext(),
		        "h����������", 
		        titleTagList.get(random.nextInt(titleTagList.size())), 
		        contentIntent);
		nm.notify(R.string.app_name, n);
		
		initWebView();
        
       
        mp.setOnCompletionListener(new OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                mWebView.loadUrl("javascript:onend()");
            }});
        
    }   
       
    @Override  
    public void onAnimationRepeat(Animation animation) {   
           
    }   
    
    
    private void addTitleTagList()
    {
    	titleTagList.add("���浱�Լ��Ǵ�С�㰡");
    	titleTagList.add("�Թԡ�վ��");
    	titleTagList.add("����450��������Ϸ");
    	titleTagList.add("�������Ͳ�����");
    	titleTagList.add("�������ͻ���");
    	titleTagList.add("���ǵ�1999�����Щ����ô");
    	titleTagList.add("�Ⱦ�������");
    	titleTagList.add("ƶ�����ϡȱ�ļ�ֵ");
    	titleTagList.add("Сѧ������̫����");
    	titleTagList.add("��ô�ɰ���һ�����к���");
    	titleTagList.add("�б��·�ѧ����");
    	titleTagList.add("���Ҵ����");
    }
    
    private Boolean downloadUriToFile(String urlDownload,String fileName)
    {    	//Ҫ���ص��ļ�·��
    	//urlDownload =  "http://192.168.3.39/text.txt";
    	//urlDownload = "http://www.baidu.com/img/baidu_sylogo1.gif";
    	// ��ô洢��·�������� �����ļ���Ŀ��·��
        
    	String dirName = "";
    	dirName = conf.setting.get("cacheLocation");
    	File f = new File(dirName);
    	if(!f.exists())
    	{
    	    f.mkdir();
    	}
    	//׼��ƴ���µ��ļ����������ڴ洢������ļ�����
    	String newFilename = dirName + fileName;
    	File file = new File(newFilename);
    	//���Ŀ���ļ��Ѿ����ڣ���ɾ�����������Ǿ��ļ���Ч��
    	if(file.exists())
    	{
    	    file.delete();
    	}
    	try {
    	         // ����URL   
    	         URL url = new URL(urlDownload);   
    	         // ������   
    	         URLConnection con = url.openConnection();
    	         //����ļ��ĳ���
    	         int contentLength = con.getContentLength();
    	         System.out.println("���� :"+contentLength);
    	         // ������   
    	         InputStream is = con.getInputStream();  
    	         // 1K�����ݻ���   
    	         byte[] bs = new byte[1024];   
    	         // ��ȡ�������ݳ���   
    	         int len;   
    	         // ������ļ���   
    	         OutputStream os = new FileOutputStream(newFilename);   
    	         // ��ʼ��ȡ   
    	         while ((len = is.read(bs)) != -1) {   
    	             os.write(bs, 0, len);   
    	         }  
    	         // ��ϣ��ر���������   
    	         os.close();  
    	         is.close();
    	            
    	} catch (Exception e) {
    	        e.printStackTrace();
    	        
    	        
    	        Message msg=mHandler.obtainMessage(MSG_FAILURE);
    	        msg.obj=fileName;
    	        msg.sendToTarget();
    	        return false;
    	}
    	return true;
    }
    
    
}
