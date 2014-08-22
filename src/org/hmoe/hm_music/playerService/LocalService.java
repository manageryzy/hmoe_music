package org.hmoe.hm_music.playerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.hmoe.hm_music._hmMusic;
import org.hmoe.hm_music.config;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 负责播放音乐的服务
 * @author manageryzy
 */
public class LocalService extends Service { 
	
	private MediaPlayer mp= new MediaPlayer();

    private static final String TAG = "LocalService"; 
    
    // This is the object that receives interactions from clients.    See 
    // RemoteService for a more complete example. 
    private final IBinder mBinder = new LocalBinder(); 
    private _hmMusic mainActvity;
    
    /**
     * 服务回调
     * @deprecated
     */
    @Override 
    public IBinder onBind(Intent intent) { 
            Log.i(TAG, "onBind"); 
            return mBinder; 
    } 
    
    /**
     * 服务回调
     * @deprecated
     */
    @Override 
    public void onCreate() { 
            Log.i(TAG, "onCreate"); 
            super.onCreate(); 
    } 
    
    /**
     * 服务回调
     * @deprecated
     */
    @Override 
    public void onDestroy() { 
            Log.i(TAG, "onDestroy"); 
            super.onDestroy(); 
    } 

    @SuppressWarnings("deprecation")
	@Override 
    public void onStart(Intent intent, int startId) { 
            Log.i(TAG, "onStart"); 
            super.onStart(intent, startId); 
    } 
    
    /**
     * 注册主要活动的activity，传入this就好了
     * @param mainActvity_
     */
    public void RegMainActvity(_hmMusic mainActvity_)
    {
    	mainActvity=mainActvity_;
//    	mainActvity.mHandler.obtainMessage(_hmMusic.MSG_SHOW_MENU).sendToTarget();
    }
    
    public class LocalBinder extends Binder { 
        public LocalService getService() { 
                return LocalService.this; 
        } 
    } 
    
    
    /**
     * 设置直接播放某个URI的歌曲
     * @param uri 目标URI
     * @return 如果成功返回true
     * @deprecated
     */
    public boolean setPlayerSongUri(String uri)
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
			return false;
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    /**
     * 播放列表中的歌曲
     * @param index 索引序号
     * @return 返回true如果成功
     */
    public boolean setPlayerSongIndex(int index)
    {
    	
    	try {
    		mp.stop();
    		mp.reset();
			mp.setDataSource(config.conf.uriList.get(index));
			if(config.conf.setting.get("ifAutoDownload").equals("true"))
			{
				addToDownloadList(config.conf.nameList.get(index),config.conf.uriList.get(index));
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
            
            config.conf.nowPlayingIndex=index;
		} catch (IllegalArgumentException e) {
			// 
			e.printStackTrace();
			return false;
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    /**
     * 播放歌曲
     */
    public void play()
    {
    	mp.start();
    }
    
    /**
     * 暂停播放歌曲
     */
    public void pause()
    {
    	mp.pause();
    }
    
    /**
     * 停止播放歌曲
     */
    public void stop()
    {
    	mp.stop();
    }
    
    /**
     * 跳转到特定的时间继续播放
     * @param time 目标时间
     */
    public void seekTo(int time)
    {
    	mp.seekTo(time);
    }
    
    /**
     * 获得当前时间
     * @return 当前时间
     */
    public int getTime()
    {
    	return mp.getCurrentPosition();
    }
    
    /**
     * 添加歌曲到播放列表
     * @param name
     * @param uri
     */
    public void addPlayList(String name,String uri)
    {
    	config.conf.nameList.add(name);
    	config.conf.uriList.add(uri);
    	config.conf.counts++;
    }
    
    /**
     * 获得当前正在播放的歌曲在播放列表的位置
     * @return
     */
    public int getPlayingIndex()
    {
        return config.conf.nowPlayingIndex;
    }
    
    /**
     * 获得下载计数
     * @return
     */
    public String getDownloadCounts()
    {
    	return String.valueOf(config.conf.downloadCounts);
    }
    
    
    public String getDownloadName(int index)
    {
    	return config.conf.downloadingNameList.get(index);
    }
    
    
    public String getDownloadUri(int index)
    {
    	return config.conf.downLoadingUriList.get(index);
    }
    
    
    public String getNowDownloading()
    {
    	return String.valueOf(config.conf.nowDownloading);
    }
    
    
    public void addToDownloadList(String name,String uri)
    {
    	if(!config.conf.downloadingNameList.contains(name))
    	{
    		config.conf.downloadingNameList.add(name);
    		config.conf.downLoadingUriList.add(uri);
    		config.conf.downloadCounts++;
    		if(config.conf.downloadCounts-config.conf.nowDownloading>=0)
    			new Thread(){  
                	public void run(){
                		while(config.conf.downloadCounts-config.conf.nowDownloading>0)
                		{
                			String uri=config.conf.downLoadingUriList.get(config.conf.nowDownloading),name=config.conf.downloadingNameList.get(config.conf.nowDownloading);
                			if(uri!=null&&name!=null)	                			
                				if(downloadUriToFile(uri,name))
                				{
                					config.conf.offlineNameList.add(name);
                					config.conf.offlineUriList.add(uri);
                					config.conf.offlineCount++;
                					config.conf.addCacheData(_hmMusic.db, name, uri);
                				}
                			
                			config.conf.nowDownloading++;
                		}
                		mainActvity.mHandler.obtainMessage(_hmMusic.MSG_DOWNLOAD_FINISH).sendToTarget();
                	}  
            	}.start();  
            
    	}
    	else
    	{
    		Toast.makeText(getApplicationContext(), "已经在下载列表中",Toast.LENGTH_SHORT).show();
    	}
    }
    
    
    public String getPlayListCounts()
    {
    	return String.valueOf(config.conf.counts);
    }
    
    
    public String getPlayListName(int index)
    {
    	return config.conf.nameList.get(index);
    }
    
    
    public String getPlayListUri(int index)
    {
    	return config.conf.nameList.get(index);
    }
    
    
    public String getConfig(String name)
    {
    	return config.conf.setting.get(name);
    }
    
    
    public String setConfig(String name,String value)
    {
    	try {
    		config.conf.setting.put(name, value);
    		config.conf.saveConfigToDB(_hmMusic.db);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
    	return "true";
    }
    
    
    public String cancleDownloadFile(int index)
    {
    	try {
    		config.conf.downloadingNameList.remove(index);
    		config.conf.downLoadingUriList.remove(index);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		}
    	return "true";
    }
    
    
    public void showMenu()
    {
    	mainActvity.mHandler.obtainMessage(_hmMusic.MSG_SHOW_MENU).sendToTarget();
    }
    
    
    public void exitProgram()
    {
    	stopService(new Intent("org.hmoe.hm_music.SERVICE_DEMO"));
		NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancelAll();
		System.exit(0);
    }
    
    
    public String getOfflineCount()
    {
    	return String.valueOf(config.conf.offlineCount);
    }
    
    
    public String getOfflineName(int index)
    {
    	return config.conf.offlineNameList.get(index);
    }
    
    
    public String getOfflineUri( int index)
    {
    	return config.conf.offlineUriList.get(index);
    }
    
    
    public String delOffline(int index)
    {
    	
    	try {
			File file = new File(config.conf.offlineNameList.get(index));
			if (file.exists()) {
				file.delete();
			}
			config.conf.delCacheData(_hmMusic.db, config.conf.offlineNameList.get(index));
			config.conf.offlineUriList.remove(index);
			config.conf.offlineNameList.remove(index);
			config.conf.offlineCount--;
		} catch (Exception e) {
			try {
				// TODO: handle exception
				config.conf.delCacheData(_hmMusic.db, config.conf.offlineNameList.get(index));
				config.conf.offlineUriList.remove(index);
				config.conf.offlineNameList.remove(index);
				config.conf.offlineCount--;
				return "true";
			} catch (Exception e2) {
				// TODO: handle exception
				e.printStackTrace();
				return "false";
			}
	
		}
		return "true";
    }
    
    private Boolean downloadUriToFile(String urlDownload,String fileName)
    {   //要下载的文件路径
    	//urlDownload =  "http://192.168.3.39/text.txt";
    	//urlDownload = "http://www.baidu.com/img/baidu_sylogo1.gif";
    	// 获得存储卡路径，构成 保存文件的目标路径
        
    	String dirName = "";
    	dirName = config.conf.setting.get("cacheLocation");
    	File f = new File(dirName);
    	if(!f.exists())
    	{
    	    f.mkdir();
    	}
    	//准备拼接新的文件名（保存在存储卡后的文件名）
    	String newFilename = dirName + fileName;
    	File file = new File(newFilename);
    	//如果目标文件已经存在，则删除。产生覆盖旧文件的效果
    	if(file.exists())
    	{
    	    file.delete();
    	}
    	try {
    	         // 构造URL   
    	         URL url = new URL(urlDownload);   
    	         // 打开连接   
    	         URLConnection con = url.openConnection();
    	         //获得文件的长度
    	         int contentLength = con.getContentLength();
    	         System.out.println("长度 :"+contentLength);
    	         // 输入流   
    	         InputStream is = con.getInputStream();  
    	         // 1K的数据缓冲   
    	         byte[] bs = new byte[1024];   
    	         // 读取到的数据长度   
    	         int len;   
    	         // 输出的文件流   
    	         OutputStream os = new FileOutputStream(newFilename);   
    	         // 开始读取   
    	         while ((len = is.read(bs)) != -1) {   
    	             os.write(bs, 0, len);   
    	         }  
    	         // 完毕，关闭所有链接   
    	         os.close();  
    	         is.close();
    	            
    	} catch (Exception e) {
    	        e.printStackTrace();
    	        
    	        
    	        Message msg=mainActvity.mHandler.obtainMessage(_hmMusic.MSG_FAILURE);
    	        msg.obj=fileName;
    	        msg.sendToTarget();
    	        return false;
    	}
    	return true;
    }
}