package com.example.hm_music;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public final class config extends SQLiteOpenHelper{
	private static final String DB_NAME = "mydata.db"; //数据库名称
    private static final int version = 1; //数据库版本
    
   
    public Map<String,String> setting = new HashMap<String,String>();
	public int apiVerson = 1;
	public String mainFrameUri="";
	public String updateURI="";
	public String HD_Web_Uri="https://music2.hmacg.cn/androidHD.php";
	public String Web_Uri="https://music2.hmacg.cn/androidPhone.php";
	
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
			String sql = "insert or replace into cache(name,value) values ('"+name + "','" + Value+"')";
			db.execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Boolean delCacheData(SQLiteDatabase db,String name)
	{
		try {
			String[] del={name};
			db.delete("cache","name=",del);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public config(Context context) {
        super(context, DB_NAME, null, version);
        //---------------------------------------------
        //下面的是配置默认值
        setting.put("ifRunGPRS","false");
        setting.put("screen_type", "pad");
        setting.put("ifAutoDownload","false");
        setting.put("cacheLocation", Environment.getExternalStorageDirectory().getPath() +"/hmmusic/");
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
        	String sql = "create table config(name varchar(255) PRIMARY KEY not null , value varchar(255) not null );";          
        	db.execSQL(sql);
        	sql = "create table cache(name varchar(255) PRIMARY KEY not null , value varchar(255) not null );";          
        	db.execSQL(sql);
        }
        catch(Exception e)
        {
        	e.getStackTrace();
        }
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
 
    }
	
}
