<html>
<head>
	<meta charset="utf-8">
	<title>HMoe_Music_UI_Mobile_Main.html</title>
	<style type="text/css">
	@font-face
	{
		font-family: FZLTCH;
		src: url('file:///android_asset/FZLTCH.ttf');
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
		left:-100%;
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
		max-height:91px;
		max-width:91px;
	}
	#play_title_title{
		float:right;
		width:72%;
		font-size:22px;
		font-weight:bolder;
	}
	#play_footer{
		bottom:0px;
		position:absolute;
		width:100%;
		border-bottom-left-radius:5px;
		border-bottom-right-radius:5px;
		background-color:rgba(72,84,90,.2);
	}
	
	#list_page
	{
		z-index:2;
		left:100%;
	}
	
	#offline_page
	{
		z-index:3;
		left:100%;
	}
	</style>
    <script language="javascript" src="./js/jquery-2.1.0.min.js" ></script>
    <script language="javascript" >
	
	
	
	$(document).ready(function(e) {
		pageX=document.body.offsetWidth;
		pageY=document.body.offsetHeight ;
		$('#main_listen').click(function(e) {
			$('#play_page').animate({left:'0px'});
    	});
		$('#play_header_btnBack').click(function(e) {
			$('#play_page').animate({left:'-100%'});
        });
    });
	
	
	
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
		<a href="./index.html">
			<img src="./images/main/hmoe_logo.png" />
		</a>
		<!-- end of #header-->
	
		<a id="main_listen" class="wrap_list_item">
			<div class="yc"></div>
			<div class="cc"><h1>　 聆听音乐</h1></div>
		</a><!-- end of #listen-->
	
		<a id="main_list" class="wrap_list_item" href="list.html">
			<div class="yc"></div>
			<div class="cc"><h1>　 播放列表</h1></div>
		</a><!-- end of #list-->
	
		<a id="main_offline" class="wrap_list_item" href="offline.html">
			<div class="yc"></div>
			<div class="cc"><h1>　 离线管理</h1></div>
		</a><!-- end of #offline-->
	
	<div id="main_footer">
		<p class="fl">Copyright&copy;2013-2014 H萌</p>
		<p class="fr">Ver 0.1 Beta</p>
	</div><!-- end of #footer-->
</div>
</div>




<div>
<div id="play_page" class="screen">
	<div class="background" ><img id="play_bg_img" class="background_img  glass" src="./images/play/HMoe_Music_UI_Mobile_Play_640x1136_bg.png" onLoad="resizeBgImg('#play_bg_img')"/></div>

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
    
    <div id="play_footer">
		<p class="fl">Copyright&copy;2013-2014 H萌</p>
		<p class="fr">Ver 0.1 Beta</p>
	</div><!-- end of #footer-->
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