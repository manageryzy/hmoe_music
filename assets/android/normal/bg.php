<?php
if(!isset($_REQUEST['id']))
{
	header('HTTP/1.0 403 Forbidden'); 
}
$id = $_REQUEST['id'];
$url="http://img1.hmacg.cn/music/bg/";
header("Location: ".$url.$id.".jpg");
?>