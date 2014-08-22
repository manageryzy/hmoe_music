package org.hmoe.hm_music;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hmoe.hm_music.playerService.PlayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public final class config extends SQLiteOpenHelper{
	private static final String DB_NAME = "mydata.db"; //数据库名称
    private static final int version = 1; //数据库版本
    private Context theContext;
    public static config conf;
    
    public PlayList OriPlayList;//hazx的歌曲列表
    public PlayList UserPlayList;//用户自己的歌曲列表
    public PlayList RandPlayList;//随机播放的时候生成的随机播放列表
    public PlayList DownloadList;//下载列表（单线程，不写成多线程的主要还是懒
    
    public static String MusicResURI = "http://music.hmacg.net/gsc.php?mod=music&file=";
    public static String LyricResURI = "http://music.hmacg.net/gsc.php?mod=lrc&file=";
    public static String BGPicResURI = "http://music.hmacg.net/gsc.php?mod=gbg&file=";
    public static String AblumResURI = "http://music.hmacg.net/gsc.php?mod=bk&file=";
    
    
    //以下代码全部报废
  public List<String>nameList;
  public List<String>uriList;
  public int nowPlayingIndex=0;
  public int counts=0;
  public List<String>downloadingNameList;
  public List<String>downLoadingUriList;
  public int downloadCounts=0;
  public int nowDownloading;
  public List<String>offlineNameList;
  public List<String>offlineUriList;
  public int offlineCount=0;
   
    public Map<String,String> setting = new HashMap<String,String>();
	public int apiVerson = 2;
	public String mainFrameUri="";
	public String updateURI="";
	public String Web_Uri="http://res1.hmacg.cn/hmoe_music_web/android/normal/index.html";
	
	//---------------------------------------------
	//下面是实现过程
	public boolean loadConfigFromDB(SQLiteDatabase db)
	{
		try {
			Cursor c = db.query("config", null, null, null, null, null, null);//查询并获得游标
			if (c.moveToFirst()) {//判断游标是否为空
				while(!c.isAfterLast())
				{
					String theName = c.getString(c.getColumnIndex("name"));
					String theValue = c.getString(c.getColumnIndex("value"));
					setting.put(theName, theValue);
					c.moveToNext();
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			return false;
		}
		return true;
	}
	public boolean saveConfigToDB(SQLiteDatabase db)
	{
		try{
			Set<Map.Entry<String, String>> set = setting.entrySet();
			for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) 
			{
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				String sql = "insert or replace into config(name,value) values ('"+entry.getKey() + "','" + entry.getValue()+"')";
				db.execSQL(sql);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public Boolean addCacheData(SQLiteDatabase db,String name,String Value)
	{
		
		try {
//			String sql = "insert or replace into cache(name,value) values ('"+name + "','" + Value+"')";
			ContentValues values = new ContentValues();
			values.put("name", name);
			values.put("value", Value);
			db.insert("cache",null, values);
//			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Boolean delCacheData(SQLiteDatabase db,String name)
	{
		try {
			String[] del={name};
			db.delete("cache","name=?",del);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 读取数据从缓存里面
	 * @param db 数据库连接
	 * @param offLineName 项目名称
	 * @return 如果成功返回包含结果的string，否则返回null
	 */
	public String getCacheData(SQLiteDatabase db,String offLineName)
	{
		try {
			Cursor c = db.query("cache", null, " name = ?",new String[]{offLineName}, null, null, null);//查询并获得游标
			if (c.moveToFirst()) {//判断游标是否为空
				while(!c.isAfterLast())
				{
					String theValue = c.getString(c.getColumnIndex("value"));
					return theValue;
				}
			}
			return null;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}
	
	public config(Context context) {
		super(context, DB_NAME, null, version);
		theContext=context;
        //---------------------------------------------
        //下面的是配置默认值
        setting.put("ifRunGPRS","false");
        setting.put("screen_type", "phone");
        setting.put("ifAutoDownload","false");
        setting.put("cacheLocation", Environment.getExternalStorageDirectory().getPath() +"/hmmusic/");
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
        	//创建数据库
        	String sql = "create table config(name varchar(255) PRIMARY KEY not null , value varchar(255) not null );";          
        	db.execSQL(sql);
        	sql = "create table cache(name text PRIMARY KEY not null , value text not null );";          
        	db.execSQL(sql);
        	//添加默认数据
        	String MusicJson=this.getFromAssets("hmmusic.json");
        	this.addCacheData(db, "OriMusicJson", MusicJson);
        }
        catch(Exception e)
        {
        	e.getStackTrace();
        }
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
    }
    
    public String getFromAssets(String fileName)
    { 
        try { 
             InputStreamReader inputReader = new InputStreamReader( theContext.getResources().getAssets().open(fileName) ); 
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) { 
            e.printStackTrace(); 
            return "";
        }
    } 
	
}
