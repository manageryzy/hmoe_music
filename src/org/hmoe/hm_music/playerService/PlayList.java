package org.hmoe.hm_music.playerService;

import java.util.HashMap;
import java.util.Map;

import org.hmoe.hm_music.config;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

/**
 * 音乐列表类，用来实现原始播放列表，随机播放列表，离线下载列表等
 * @author manageryzy
 *
 */
public class PlayList {
	public class PlayListNode
	{
		public String MusicId;//音乐的id
		public String FileName;//音乐的文件名，就是保存在本地的文件名
		public String FileUri;//音乐的URI
		public String Title;//音乐的标题
		public String Album;//专辑名称
		public String LyricFileName;//歌词文件本地文件名
		public String LyricUri;//歌词文件URI
		public String AlbumPicFileName;//专辑图片文件文件名
		public String AlbumPicUri;//专辑图片文件URI
		public String BGPicFileName;//背景图片文件名称
		public String BGPicUri;//背景图片URI
		public PlayListNode()
		{
			this.MusicId="";
			this.FileName="";
			this.FileUri="";
			this.Title="";
			this.Album="";
			this.LyricUri="";
			this.LyricFileName="";
			this.AlbumPicFileName="";
			this.AlbumPicUri="";
			this.BGPicFileName="";
			this.BGPicUri="";
		}
	}
	
	private Map<String,PlayListNode> theList;
	public int nowIndex=0;//当前正在处理的项目的索引，可能是正在播放的音乐，也可能是正在下载的歌曲
	public int dealedIndex=0;//已经处理完成的项目的索引，可能是上一首音乐，也可能是下载完成的音乐
	
	public PlayList()
	{
		theList = new HashMap<String,PlayListNode>();
	}
	
	/**
	 * 获得节点对象
	 * @param index 成员在列表中的索引
	 * @return 如果成功，返回节点对象，否则返回null
	 */
	public PlayListNode getItem(int index)
	{
		try
		{
			return theList.get(index+"");
		}
		catch(Exception e)
		{
			Log.e("PlayList", "Out of border");
			return null;
		}
	}
	
	/**
	 * 获得当前列表中条目的数目
	 * @return 当前列表中条目的数目
	 */
	public int getCount()
	{
		return theList.size();
	}
	
	/**
	 * 添加一个拥有默认值的空条目进入列表
	 * @return 返回新增加条目，如果失败返回null
	 */
	public PlayListNode addItem()
	{
		PlayListNode newItem=new PlayListNode();
		try
		{
			theList.put(this.theList.size()+1+"", newItem);
			return newItem;
		}
		catch(Exception e)
		{
			Log.i("Playlist","Failed to add item");
			return null;
		}
	}
	
	/**
	 * 添加一个拥有默认值的空条目进入列表的指定位置
	 * @param index 目标位置索引
	 * @return 如果添加失败，返回null，否则返回条目
	 */
	public PlayListNode addItem(int index)
	{
		PlayListNode newItem=new PlayListNode();
		try
		{
			theList.put(index+"", newItem);
			return newItem;
		}
		catch(Exception e)
		{
			Log.e("PlayList", "Failed to add item");
			return null;
		}
	}
	
	
	public boolean InitListByJSON(String Json)
	{
		JSONTokener jsonParser = new JSONTokener(Json);
		try {
			JSONArray theArr=(JSONArray) jsonParser.nextValue();
			for(int i=0;i<theArr.length();i++)
			{
				JSONObject theSong=theArr.getJSONObject(i);
				PlayListNode newNode=new PlayListNode();
				if(this.theList.get(theSong.getString("music_id"))==null)
				{
					theList.put(theSong.getString("music_id"),newNode);
				}
				else
				{
					Log.i("PlayList", "the key has alread used,please check the json");
					theList.put(theSong.getString("music_id"),newNode);
				}
				try
				{
					newNode.MusicId=theSong.getString("music_id");
					newNode.FileName=theSong.getString("music_id")+".mp3";
					newNode.FileUri=config.MusicResURI+theSong.getString("music_id");
					newNode.Title=theSong.getString("title");
					newNode.AlbumPicFileName=theSong.getString("music_id")+"_album.jpg";
					newNode.LyricFileName=theSong.getString("music_id")+".lrc";
					newNode.BGPicFileName=theSong.getString("music_id")+"_bg.jpg";
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					e.getCause();
					Log.e("PlayList", "error in reading some item in json.");
					theList.remove(newNode);
					continue;
				}
					
					
				try
				{	
					newNode.Album=theSong.getString("album");
				}
				catch(JSONException e)
				{
					newNode.Album="";
					e.getCause();
					Log.i("PlayList", "error in reading album in json.");
				}
				
				try
				{
					newNode.LyricUri=config.LyricResURI+theSong.getString("lrc");
				}
				catch(JSONException e)
				{
					newNode.LyricUri="";
					e.getCause();
					Log.i("PlayList", "error in reading lyric in json.");
				}	
				
				try
				{

					newNode.AlbumPicUri=config.AblumResURI+theSong.getString("bg_video");
				}
				catch(JSONException e)
				{
					newNode.AlbumPicUri="";
					e.getCause();
					Log.i("PlayList", "error in reading AlbumPicUri in json.");
				}
				
				try
				{
					newNode.BGPicUri=config.BGPicResURI+theSong.getString("bg");
				}
				catch(JSONException e)
				{
					newNode.BGPicUri="";
					e.getCause();
					Log.i("PlayList", "error in reading BGPicUri in json.");
				}
				
			}
			return true;
		} catch (Exception e) {
			Log.e("playlist","can't init from json");
			e.printStackTrace();
			return false;
		} 
	}
}
