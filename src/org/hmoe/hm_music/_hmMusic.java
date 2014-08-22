package org.hmoe.hm_music;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.hmoe.hm_music.playerService.LocalService;
import org.hmoe.hm_music.playerService.PlayList;
import org.hmoe.hm_music.view.PlayPageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class _hmMusic extends Activity implements AnimationListener {
	
	public static final int MSG_SUCCESS = 0;  
	public static final int MSG_FAILURE = 1; 
	public static final int MSG_SHOW_MENU=2;
	public static final int MSG_DOWNLOAD_FINISH=3;
    
    private LocalService mBoundService; 
    private List<String> titleList;//viewpager的标题  
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    
    private View view1, view3;//需要滑动的页卡 
    private PlayPageView view2;
    private List<View> viewList;//把需要滑动的页卡添加到这个list中  
	
    private List<String> titleTagList = new ArrayList<String>();//播放器卖萌的话
    ListView listView ;
    private ImageView  imageView = null;   
	private Animation alphaAnimation = null;   
	private Button btnYes=null;
	private Button btnNo=null;
	public static SQLiteDatabase db = null;
	
	
	private ServiceConnection mConnection = new ServiceConnection() { 
	        public void onServiceConnected(ComponentName className, IBinder service) { 
	                // This is called when the connection with the service has been 
	                // established, giving us the service object we can use to 
	                // interact with the service.    Because we have bound to a explicit 
	                // service that we know is running in our own process, we can 
	                // cast its IBinder to a concrete class and directly access it. 
	                mBoundService = ((LocalService.LocalBinder)service).getService();  
	                Log.i("hmMusic", "service connected");
	        } 
	
	        public void onServiceDisconnected(ComponentName className) { 
	                // This is called when the connection with the service has been 
	                // unexpectedly disconnected -- that is, its process crashed. 
	                // Because it is running in our same process, we should never 
	                // see this happen. 
	                mBoundService = null; 
	                Log.i("hmMusic", "Service Disconnected");
	        } 
	};
	
	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler(){  
        @Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case MSG_SUCCESS:  
                
                Toast.makeText(getApplication(), "下载歌曲成功"+(String)msg.obj, Toast.LENGTH_LONG).show();  
                break;   
  
            case MSG_FAILURE:    
                Toast.makeText(getApplication(), "下载歌曲失败"+(String)msg.obj, Toast.LENGTH_LONG).show(); 
                break;  
            case MSG_SHOW_MENU:
            	openOptionsMenu();
            	break;
            case MSG_DOWNLOAD_FINISH:
            	Toast.makeText(getApplicationContext(), "全部下载任务完成",Toast.LENGTH_SHORT).show();
            	break;
            }  
            super.handleMessage(msg);  
        }         
    };  
    
    private void init()
    {
    	config.conf =new config(this);//这段代码放到Activity类中才用this
		
		if(db==null)db = config.conf.getReadableDatabase();
		
        if(!config.conf.loadConfigFromDB(db))
        {
        	Toast.makeText(getApplicationContext(), "数据库读取异常",Toast.LENGTH_SHORT).show();;
        }
        if(!config.conf.saveConfigToDB(db))
        {
        	Toast.makeText(getApplicationContext(), "数据库写入异常",Toast.LENGTH_SHORT).show();;
        }
        
        String OriMusicJson = config.conf.getCacheData(db, "OriMusicJson");
        
        config.conf.OriPlayList=new PlayList();
        config.conf.OriPlayList.InitListByJSON(OriMusicJson);
//      config.conf.nameList=new LinkedList<String>();
//      config.conf.uriList=new LinkedList<String>();
//      config.conf.downloadingNameList=new LinkedList<String>();
//      config.conf.downLoadingUriList=new LinkedList<String>();
//      config.conf.offlineNameList=new LinkedList<String>();
//      config.conf.offlineUriList=new LinkedList<String>();

//		if(!config.conf.getCacheData(db, config.conf.offlineNameList, config.conf.offlineUriList))
//			Toast.makeText(getApplicationContext(), "获取缓存数据错误",Toast.LENGTH_SHORT).show();
//		config.conf.offlineCount=config.conf.offlineNameList.size();
    }

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.welcome);   
		
		if(config.conf==null)init();
        
        addTitleTagList();
        
        imageView = (ImageView)findViewById(R.id.welcome_image_view);   
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);   
        alphaAnimation.setFillEnabled(true); //启动Fill保持   
        alphaAnimation.setFillAfter(true);  //设置动画的最后一帧是保持在View上面   
        imageView.setAnimation(alphaAnimation);   
        alphaAnimation.setAnimationListener(this);  //为动画设置监听   
        
        startService(new Intent("org.hmoe.hm_music.SERVICE_DEMO")); 
		
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);               
		Notification n = new Notification(R.drawable.ic_launcher, "h萌在线音乐!", System.currentTimeMillis());             
		n.flags = Notification.FLAG_NO_CLEAR;                
		Intent i = new Intent(getApplicationContext(), _hmMusic.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);           
		PendingIntent contentIntent  = PendingIntent.getActivity(this, 0, i, 0);  
		Random random = new Random();             
		n.setLatestEventInfo(
				getApplicationContext(),
		        "h萌在线音乐", 
		        titleTagList.get(random.nextInt(titleTagList.size())), 
		        contentIntent);
		nm.notify(R.string.app_name, n);
        
		 // supporting component replacement by other applications). 
        bindService(new Intent(this,LocalService.class), mConnection, Context.BIND_AUTO_CREATE); 
        
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
		switch(item.getItemId())//得到被点击的item的itemId
		{
		case R.id.action_settings: //对应的ID就是在add方法中所设定的Id
			setContentView(R.layout.setting);
			CheckBox boolSetting=(CheckBox)findViewById(R.id.ifUseGPRS);
			if(config.conf.setting.get("ifRunGPRS").equals("true"))
				boolSetting.setChecked(true);
			
			boolSetting=(CheckBox)findViewById(R.id.ifHD);
			if(config.conf.setting.get("screen_type").equals("pad"))
				boolSetting.setChecked(true);
			
			boolSetting=(CheckBox)findViewById(R.id.ifAutoDownload);
			if(config.conf.setting.get("ifAutoDownload").equals("true"))
				boolSetting.setChecked(true);
			
			boolSetting=(CheckBox)findViewById(R.id.DownloadLocation);
			if(!config.conf.setting.get("cacheLocation").equals( Environment.getExternalStorageDirectory().getPath() +"/hmmusic/"))
				boolSetting.setChecked(true);
			
			btnYes=(Button)findViewById(R.id.button1);
	        btnNo=(Button)findViewById(R.id.button2);
	        btnYes.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v)
	        	{
	        		CheckBox boolSetting=(CheckBox)findViewById(R.id.ifUseGPRS);
	        		if(boolSetting.isChecked())config.conf.setting.put("ifRunGPRS","true");
	        		else config.conf.setting.put("ifRunGPRS","false");
	        		
	        		boolSetting=(CheckBox)findViewById(R.id.ifHD);
	        		if(boolSetting.isChecked())config.conf.setting.put("screen_type","pad");
	        		else config.conf.setting.put("screen_type","phone");
	        		
	        		boolSetting=(CheckBox)findViewById(R.id.ifAutoDownload);
	        		if(boolSetting.isChecked())config.conf.setting.put("ifAutoDownload","true");
	        		else config.conf.setting.put("ifAutoDownload","false");
	        		
	        		boolSetting=(CheckBox)findViewById(R.id.DownloadLocation);
	        		if(boolSetting.isChecked())config.conf.setting.put("cacheLocation",Environment.getExternalStorageDirectory().getPath() +"/hmmusic/");
	        		else config.conf.setting.put("cacheLocation",Environment.getExternalStorageDirectory().getPath() +"/hmmusic/");
	        		
	        		config.conf.saveConfigToDB(db);
	        		
	        		onResume();
	        		setContentView(R.layout.main_list_layout);
	        		
	        	}
			});
	        btnNo.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v)
	        	{
	        		setContentView(R.layout.main_list_layout);
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
	 
		if(config.conf.setting.get("screen_type").equals("pad"))
		{
			/*** 设置为横屏  */
			if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
			{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}
		else
		{
			/*** 设置为竖屏  */
			if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
	 
	 super.onResume();
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		listView.getChildAt(0).setBackgroundResource(R.drawable.hmoe_music_ui_mobile_welcome);
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true);//true对任何Activity都适用
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override  
    public void onAnimationStart(Animation animation) {   
           
    }   
	

	@Override  
    public void onAnimationEnd(Animation animation) {   
        //动画结束时结束欢迎界面并转到软件的主界面   
    	setContentView(R.layout.main_frame);
    	
    	
    	viewPager = (ViewPager) findViewById(R.id.viewpager); 
    	getLayoutInflater();
		LayoutInflater lf = LayoutInflater.from(this);  
        view1 = lf.inflate(R.layout.main_list_layout, null);  
        view2 = (PlayPageView) lf.inflate(R.layout.play_control_layout, null);  
        view3 = lf.inflate(R.layout.play_list_layout, null);  
        

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
        viewList.add(view1);  
        viewList.add(view2);  
        viewList.add(view3);  
        
        titleList = new ArrayList<String>();// 每个页面的Title数据  
        titleList.add("wp");  
        titleList.add("jy");  
        titleList.add("jh");  
        
        pagerAdapter = new PagerAdapter() {  
        	  
            @Override  
            public boolean isViewFromObject(View arg0, Object arg1) {  
  
                return arg0 == arg1;  
            }  
  
            @Override  
            public int getCount() {  
  
                return viewList.size();  
            }  
  
            @Override  
            public void destroyItem(ViewGroup container, int position,  
                    Object object) {  
                container.removeView(viewList.get(position));  
  
            }  
  
            @Override  
            public int getItemPosition(Object object) {  
  
                return super.getItemPosition(object);  
            }  
  
            @Override  
            public CharSequence getPageTitle(int position) {  
  
                return titleList.get(position);  
            }  
  
            @Override  
            public Object instantiateItem(ViewGroup container, int position) {  
                container.addView(viewList.get(position));  
                return viewList.get(position);  
            }  
  
        };  
        viewPager.setAdapter(pagerAdapter);  
        
        //
        listView=(ListView)view1.findViewById(R.id.MainListView);
    	SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.list_item_layout,  
                new String[]{"image","item"}, new int[]{R.id.play_btn_back,R.id.textView1});  
        listView.setAdapter(adapter);  

		
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
			e.getCause();
			e.printStackTrace();
		}
		
		CheckNetWork();
        
        mBoundService.RegMainActvity(this);
//        mp.setOnCompletionListener(new OnCompletionListener(){
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                
//            }});
    }   
    
       
    @Override  
    public void onAnimationRepeat(Animation animation) {   
    	
    }   
    
    
    private void addTitleTagList()
    {
    	titleTagList.add("你真当自己是大小姐啊");
    	titleTagList.add("乖乖♂站好");
    	titleTagList.add("不交450还想玩游戏");
    	titleTagList.add("不做死就不会死");
    	titleTagList.add("人作死就会死");
    	titleTagList.add("还记得1999年的那些事情么");
    	titleTagList.add("萌就是正义");
    	titleTagList.add("贫乳才是稀缺的价值");
    	titleTagList.add("小学生真是太棒了");
    	titleTagList.add("这么可爱的一定是男孩子");
    	titleTagList.add("有本事放学别走");
    	titleTagList.add("吃我大屌啦");
    }
    
    
    /** 
     * @author chenzheng_java 
     * @description 准备一些测试数据 
     * @return 一个包含了数据信息的hashMap集合 
     */  
    private ArrayList<HashMap<String, Object>> getData(){  
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String,Object>>();  
        for(int i=0;i<10;i++){  
            HashMap<String, Object> tempHashMap = new HashMap<String, Object>();  
            tempHashMap.put("image", R.drawable.ic_launcher);  
            tempHashMap.put("item", "用户"+i);  
            arrayList.add(tempHashMap);  
              
        }  
          
          
        return arrayList;  
          
    }  
    
    /**
     * 处理按下返回按钮的事件的方法
     */
    public void OnBtnBackClick(View view){   
    	this.viewPager.setCurrentItem(0);
    }
    
    
    private void CheckNetWork()
    {

		//获得网络状况
		ConnectivityManager connectMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
         
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if(info==null)
        {
        	Toast.makeText(getApplicationContext(), "无可用网络",Toast.LENGTH_SHORT).show();
        	Dialog dialog=new AlertDialog.Builder(this).setTitle("无可用网络").setMessage("没有网络可是听不成的DA☆ZE").setPositiveButton("确定",
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
        	Dialog dialog=new AlertDialog.Builder(this).setTitle("正在使用移动网络").setMessage("没有网络可是听不成的DA☆ZE").setPositiveButton("确定",
        		new DialogInterface.OnClickListener() {
    	      	@Override
    	      	public void onClick(DialogInterface dialog, int which) {
    	      		System.exit(0);
    	      	}
    	    }).create(); 
        	if(config.conf.setting.get("ifRunGPRS").equals("false"))dialog.show();  
        	Toast.makeText(getApplicationContext(), "正在使用移动网络，土豪，我们做朋友吧",Toast.LENGTH_SHORT).show();
        	Toast.makeText(getApplicationContext(), "一夜没关流量，房子就是移动的了……",Toast.LENGTH_SHORT).show();
        	
        	if(config.conf.setting.get("ifRunGPRS").equals("false"))return;
        }
    }
}
