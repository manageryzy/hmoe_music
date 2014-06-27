<html>
<head>
	<meta charset="utf-8">
	<title>HMoe_Music_UI_Mobile_Main.html</title>
	<style type="text/css">
	@font-face
	{
		font-family: 'FZLTCH';
		src: url('file:///android_asset/FZLTCH.TTF') format('truetype');
	}
	html, body{
		padding: 0;
		margin: 0;
		font-family: "Microsoft JhengHei", "Microsoft YaHei", "Microsoft YaHei UI", "PMingLiu", "SimSun";
		background: #444;
		color: #FFF;
		width: 100%;
		height: 100%;
		/*min-width: 320px;
		min-height: 480px;*/
		position:relative;
		overflow:hidden;
	}
	p{
		padding: 0;
		margin: 0;
	}
	.tc{
		text-align: center;
	}
	.fr{
		float: right;
	}
	.fl{
		float: left;
	}
	#main_header{
		height: 9%;
		background: url("./images/main/title_bar_bg.png") fixed no-repeat;
		background-size: 100% 100%;
	}
	#main_header img{
		margin-top: .5%;
		height: 75%;
	}
	#main_footer{
		background: url("./images/main/title_bar_bg.png") fixed no-repeat;
		height: 3%;
	}
	.wrap_list_item{
		height: 26%;
		width: 100%;
		text-align: center;
		display: block;
	}
	.wrap_list_item h1{
		padding: 0;
		margin: 0;
		width: 5.5em;
	}
	div.cc, div.yc{
		display: inline-block;
	}
	div.yc{
		height: 50%;
	}
	a, a:active, a:visited{
		text-decoration: none;
		color: #FFF;
	}
	#main_listen{
		background: url("./images/main/bg_listen.png") no-repeat;
		background-size: 100% 100%;
	}
	#main_listen h1{
		background: url("./images/main/icon_listen.png") no-repeat;
		background-size: auto 75%;
		background-position: 0 .1em;
	}
	#main_list{
		background: url("./images/main/bg_list.png") no-repeat;
		background-size: 100% 100%;
	}
	#main_list h1{
		background: url("./images/main/icon_list.png") no-repeat;
		background-size: auto 75%;
		background-position: 0 .175em;
	}
	#main_offline{
		background: url("./images/main/bg_offline.png") no-repeat;
		background-size: 100% 100%;
	}
	#main_offline h1{
		background: url("./images/main/icon_offline.png") no-repeat;
		background-size: auto 75%;
		background-position: 0 .2em;
	}
	
	.glass{
		
		-webkit-filter: blur(10px); /* Chrome, Opera */
      	-moz-filter: blur(10px);
        -ms-filter: blur(10px);    
        filter: blur(10px);    
	}
	
	.clearer{
		width:100%;
		height:2px;
		clear:both;
		background-color:rgba(255,255,255,0.5);
	}
	
	.clearer_hide{
		width:100%;
		clear:both;
	}
	
	.screen{
		left:0;
		top:0;
		height:100%;
		width:100%;
		margin:0px;
		position:absolute;
	}
	
	.background{
		position:absolute;
		overflow:hidden;
		top:0px;
		left:0px;
		width:100%;
		height:100%;
		z-index:-1;
	}
	
	.background_img
	{
		position:absolute;
		top:-10px;
		left:-10px;
		
	}
	
	#play_page{
		/*background: url("./images/play/HMoe_Music_UI_Mobile_Play_640x1136_bg.png") fixed no-repeat;
		background-size: 100% 100%;*/
		left:100%;
		z-index:2;
		font-family:"FZLTCH";
	}
	
	#play_header{
		width:100%;
		max-height:8.09%;
		padding-top:1.06%;
		padding-bottom:1.06%;
		overflow:hidden;
		background-color:rgba(54,54,54,.1);
	}
	
	#play_header_btnBack
	{
		float:left;
		width:19.21%;
		margin-left:3.43%;
	}
	
	#play_header_Logo{
		width:20.46%;
		
	}
	
	#play_header_btnList{
		float:right;
		width:19.37%;
		margin-right:3.43%;
	}
	#play_title{
		background-color:rgba(54,54,54,.25);
		padding:1.8%;
	}
	#play_title_imgContainer{
		text-align: center;
		max-width:23%;
		min-width:16%;
		border-radius:5px;
		float:left;
		background-color:rgba(172,172,172,.5);
		border-width:10px;
		padding:3px;
	}
	#play_title_img{
		width:95%;
		min-height:40px;
		min-width:40px;
		
	}
	#play_title_title{
		float:right;
		width:72%;
		font-size:22px;
		font-weight:bolder;
	}
	#play_footer{
		display:inline-block;
		width:100%;
		border-bottom-left-radius:5px;
		border-bottom-right-radius:5px;
		background-color:rgba(72,84,90,.2);
	}
	#play_down_container{
		overflow:hidden;
		top:auto;
		bottom:0px;
		position:absolute;
		height:auto;
		width:100%;
	}
	#play_control_container
	{
		padding:5%;
	}
	#play_prev_button
	{
		width:22%;
	}
	
	#play_next_button
	{
		width:22%;
	}
	
	#play_pause_button
	{
		width:29%;
		padding-top:10px;
	}
	
	#list_page
	{
		z-index:2;
		left:100%;
	}
	#play_time_now
	{
		background-color:rgba(255,255,255,0.19);
		left:0px;
		display:inline;
		font-size:36px;
	}
	#play_time_all
	{
		background-color:rgba(255,255,255,0.19);
		float:right;
		display:inline;
		font-size:36px;
	}
	#play_bar_container
	{
		height:30px;
		width:100%;
	}
	#play_bar
	{
		background-color:rgba(103,103,103,1);
		width:100%;
		height:10px;
		top:10px;
		position:relative;
	}
	#play_bar_played
	{
		height:14px;
		width:50%;
		background-color:rgba(103,164,222,1);
		top:-2px;
		position:relative;
	}
	#play_bar_slider_container
	{
		position:relative;
		top:-10px;
		left:100%;
	}
	#play_bar_slider
	{
		height:30px;
		width:30px;
		background-color:rgba(255,255,255,1);
		border-radius:15px;
		position:relative;
		left:-7px;
	}
	#play_lyric_container
	{
		padding-left:5%;
		padding-right:5%;
		padding-bottom:10px;
		top:10px;
		position:relative;
		height:100%;
		overflow:hidden;
	}
	#play_lyric_background
	{
		background-color:rgba(255,255,255,.36);
		color:#333333;
		width:100%;
		height:100%;
		overflow:hidden;
	}
	
	#offline_page
	{
		z-index:3;
		left:100%;
	}
	</style>
    <script language="javascript" src="./js/jquery-2.1.0.min.js" ></script>
    <script language="javascript" src="./debuggap-1.0.1.js"> </script>
    <script language="javascript" >
	
	nowPage=0;//表示当前的页面，0:主页，1：播放音乐，2：播放列表，3：离线管理
	touchEventStartX=-1;
	touchEventStartY=-1;
	touchEventNowX=-1;
	touchEventNowY=-1;
	isSlidingPage=0;//当前触摸事件是否正在滑动切换页面
	
	
	$(document).ready(function(e) {
		pageX=document.body.offsetWidth;
		pageY=document.body.offsetHeight;
		
		//屏蔽掉默认的滑动消息处理，防止出现浏览器的默认处理
		$('body').on("touchstart",(function(e){
			e.preventDefault();
		}));
		$('body').on("touchmove",(function(e){
			e.preventDefault();
		}));
		$('body').on("touchend",(function(e){
			e.preventDefault();
		}));
		
		//添加按钮的点击的响应
		$('#main_listen').on("touchend",(function(e) {
			//重新处理歌词窗口大小，这个地方的代码有点问题但是实在是处理不好，就这样凑乎的用吧
			$('#play_lyric_container').height($('#play_down_container').get(0).offsetTop-$('#play_up_container').get(0).offsetHeight );
			$('#play_page').animate({left:'0px'},"fast");
			nowPage=1;
    	}));
		$('#play_header_btnBack').on("touchend",(function(e) {
			$('#play_page').animate({left:'100%'},"fast");
			nowPage=0;
        }));
		$('#play_bg_img').on("touchstart",(function(e){
			//好奇怪，收不到这个消息……暂且就当他不存在吧
			touchEventStartX=e.originalEvent.touches[0].clientX;
			touchEventStartY=e.originalEvent.touches[0].clientY;

		}));
		
		//对于滑动切换页面的处理
		$('#play_page').on("touchmove",(function(e){
			touchEventNowX=e.originalEvent.touches[0].clientX;
			touchEventNowY=e.originalEvent.touches[0].clientY;
			if(touchEventStartX==-1)touchEventStartX=touchEventNowX;
			if(touchEventStartY==-1)touchEventStartY=touchEventNowY;
			isSlidingPage=1;
		}));
		$('#play_page').on("touchend",(function(e){
			if((touchEventNowX-touchEventStartX)>(pageX*0.33)&&(isSlidingPage==1))
			{
				$('#play_page').animate({left:'100%'},"fast");
				nowPage=0;
				isSlidingPage=0;
			}
			touchEventStartX=-1;
			touchEventStartY=-1;
		}));
		
    });
	
	//获取元素的纵坐标 
	function getTop(e){ 
		var offset=e.offsetTop; 
		if(e.offsetParent!=null) offset+=getTop(e.offsetParent); 
		return offset; 
	} 
	//获取元素的横坐标 
	function getLeft(e){ 
		var offset=e.offsetLeft; 
		if(e.offsetParent!=null) offset+=getLeft(e.offsetParent); 
		return offset; 
	} 
	
	
	//在背景图片加载完成进行大小自适应的时候调用这个函数
	function resizeBgImg(id) {
        	var imgX=$(id).width();
			var imgY=$(id).height();
			var aimX=0;
			var aimY=0;
			var scaleX=pageX/imgX;
			var scaleY=pageY/imgY;
			if(scaleX>scaleY)
			{
				aimX=pageX+20;
				aimY=scaleX*imgY+20;
			}
			else
			{
				aimX=scaleY*imgX+20;
				aimY=pageY+20;
			}
        	$('.background_img').css('width',aimX);
			$('.background_img').css('height',aimY);
    	}
	
	
	</script>
	
</head>
<body>

<div>
<div id="main_page" class="screen"  >
		<a href="#">
			<img src="./images/main/hmoe_logo.png" />
		</a>
		<!-- end of #header-->
	
		<div id="main_listen" class="wrap_list_item">
			<div class="yc"></div>
			<div class="cc"><h1>　 聆听音乐</h1></div>
		</div><!-- end of #listen-->
	
		<div id="main_list" class="wrap_list_item" href="#">
			<div class="yc"></div>
			<div class="cc"><h1>　 播放列表</h1></div>
		</div><!-- end of #list-->
	
		<div id="main_offline" class="wrap_list_item" href="#">
			<div class="yc"></div>
			<div class="cc"><h1>　 离线管理</h1></div>
		</div><!-- end of #offline-->
	
	<div id="main_footer">
		<p class="fl">Copyright&copy;2013-2014 H萌</p>
		<p class="fr">Ver 0.1 Beta</p>
	</div><!-- end of #footer-->
</div>
</div>




<div>
<div id="play_page" class="screen">
	<div class="background" ><img id="play_bg_img" class="background_img  glass" src="./images/play/HMoe_Music_UI_Mobile_Play_640x1136_bg.png" onLoad="resizeBgImg('#play_bg_img')"/></div>
	
    <div id="play_up_container">
        <div id="play_header" class="tc">
            <div class="glass"></div>
            <img src="./images/play/HMoe_Music_UI_Mobile_Play_640x1136_btnBack.png" id="play_header_btnBack"/>
            <img src="./images/play/HMoe_Music_UI_Mobile_Play_640x1136_logo.png" id="play_header_Logo"/>
            <img src="./images/play/HMoe_Music_UI_Mobile_Play_640x1136_btnList.png" id="play_header_btnList"/>
        </div>
        
        <div class="clearer" ></div>
        
        <div id="play_title">
            <div id="play_title_imgContainer">
                <img id="play_title_img" src="./images/play/test.png"/>
            </div>
            <div id="play_title_title">
                歌曲名称 -来自测试歌曲-
            </div>
            <div class="clearer_hide" ></div>
        </div>
        
        <div class="clearer" ></div>
        <div id="play_title_end"></div>
    </div>
    
    <div id="play_lyric_container">
        <div id="play_lyric_background" class="tc">
           	从前有座山<br>
            山里有座庙<br>
            庙里有个hazx<br>
            在撸管！<br>
            1=233333<br>
            2=233333<br>
            3=233333<br>
            4=233333<br>
            5=233333<br>
            6=233333<br>
            7=233333<br>
            8=233333<br>
            9=233333<br>
            0=233333<br>
        </div>
    </div>
    
    <div id="play_down_container" >
    	
        
    	<div id="play_control_container">
        
            
        	<div id="play_bar_container">
            	<div id="play_bar">
                	<div id="play_bar_played">
                    	<div id="play_bar_slider_container">
                        	<div id="play_bar_slider"></div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div id="play_timer_container">
            	<div id="play_time_now" >00:00</div>
                <div id="play_time_all" >00:00</div>
            </div>
            
            <div class="clearer_hide" ></div>
            
        	<div id="play_button_container" class="tc">
            	<img id="play_prev_button" src="./images/play/HMoe_Music_UI_Mobile_Play_640x1136_front.png">
                <img id="play_pause_button" src="./images/play/HMoe_Music_UI_Mobile_Play_640x1136_pause.png">
                <img id="play_next_button" src="./images/play/HMoe_Music_UI_Mobile_Play_640x1136_next.png">
            </div>
        </div>
    	
        <div class="clearer" ></div>
    	<div id="play_footer">
			<p class="fl">Copyright&copy;2013-2014 H萌</p>
			<p class="fr">Ver 0.1 Beta</p>
		</div><!-- end of #footer-->    
    </div>

</div>
</div>


<div>
<div id="list_page" class="screen">
</div>
</div>

<div>
<div id="offline_page" class="screen">
</div>
</div>



</body>
</html>