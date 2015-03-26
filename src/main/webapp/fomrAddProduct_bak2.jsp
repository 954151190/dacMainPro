<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib uri="/struts-tags" prefix="s"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>ajaxupload上传</title>
		<meta charset="utf-8"/>
		<style type="text/css">
			.divMain{
				position:absolute;
				width:140px;
				height:100px;
				padding-left:60px;
				padding-top:40px;
			}
			#upload{
				width:150px;
				height:30px;
			}
			.content{
				width:300px;
				height:200px;
			}
		</style>
		<script type="text/javascript" src="jquery/jquery.1.8.js"></script>
		<script type="text/javascript" src="jquery/ajaxupload.js"></script>
	</head>
	<body>
		<div class="divMain">
			<button id="upload">文件上传</button>
			<div class="content"></div>
		</div>
	</body>
	<script type="text/javascript">
		/*
				ajaxupload上传
			*/
		   $(document).ready(function(){
			    var button = $('#upload'), interval;
			    var fileType = "all",fileNum = "one"; 
			    new AjaxUpload(button,{
			        action: 'productAdd',
			        name: 'file',
			        onSubmit : function(file, ext){
			            if(fileType == "pic")
			            {
			                if (ext && /^(jpg|png|jpeg|gif)$/.test(ext)){
			                    this.setData({
			                        'info': '文件类型为图片'
			                    });
			                } else {
			                    $('<li></li>').appendTo('.files').text('非图片类型文件，请重传');
			                    return false;               
			                }
			            }
			            button.text('文件上传中');
			            if(fileNum == 'one')
			                this.disable();
			            interval = window.setInterval(function(){
			                var text = button.text();
			                if (text.length < 14){
			                    button.text(text + '.');                    
			                } else {
			                    button.text('文件上传中');             
			                }
			            }, 200);
			        },
			        onComplete: function(file, response){//上传成功的函数；response代表服务器返回的数据
							//清楚按钮的状态
							button.text('文件上传');
				            window.clearInterval(interval);
				            this.enable();
							//修改下方div的显示文字
						if('success'==response){
							$(".content").text("上传成功");
						}else{
							$(".content").text("上传失败");
						}
			        }
					});
			});
	</script>
</html>