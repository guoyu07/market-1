<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>   <title>应用市场管理系统</title>
  <jsp:include page="../common/commonFrame.jsp">
    <jsp:param name="needPage" value="N"/>
    <jsp:param name="needDate" value="N"/>
    <jsp:param name="needValidate" value="Y"/>
  </jsp:include>
  <script type="text/javascript">
    function changeCode() {  //刷新
      $('#kaptchaImage').hide().attr('src', ctx +'/resources/kaptcha.jpg?' + Math.floor(Math.random()*100) ).fadeIn();
    }

    $(document).ready(function () {

      $("#frmEdit").validate({
        rules: {
          oldPwd:{
            required: true
          },
          newPwd1:{
            required: true,
            rangelength: [6, 20]
          },
          newPwd2:{
            required: true,
            rangelength: [6, 20]
          },
          verifyCode:{
            required: true
          }
        },
        submitHandler:function(form){
          ucf.ajax.commonFn($('#frmEdit').attr('action'), $("#frmEdit").serialize(),function(result){
            $("#saveBtn").attr("data-content",result.message);
            $("#saveBtn").popover('toggle');

            if(result.status =='OK'){
              setTimeout(function(){$("#saveBtn").popover('toggle');$('[data-dismiss]').click();}, 2000);
            }else{
              setTimeout(function(){$("#saveBtn").popover('toggle');}, 2000);
            }
          });
        }
      });
    });

  </script>
</head>
<body class="${webSkin}">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="myModalLabel">修改密码</h4>
  </div>
  <form id="frmEdit" name="frmEdit" role="form" class="form-horizontal" action="${ctx}/myInfo/pwd" method="post">


  <div class="modal-body">
    <div class="form-group">
      <label class="col-sm-3 control-label">旧密码:</label>
      <div class="col-sm-6">
        <input type="password" name="oldPwd" class="form-control" placeholder=""/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label">新密码:</label>
      <div class="col-sm-6">
        <input type="password" name="newPwd1" class="form-control" placeholder=""/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label">再次输入:</label>
      <div class="col-sm-6">
        <input type="password" name="newPwd2" class="form-control" placeholder=""/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label">验证码:</label>
      <div class="col-sm-3">
        <input type="text" name="verifyCode" class="form-control" placeholder=""/>
      </div>
      <div class="col-sm-3">
        <img src="${ctx}/resources/kaptcha.jpg" id="kaptchaImage" onclick="javascript:changeCode();" style="cursor:pointer;" />
      </div>
    </div>
  </div>

  <div class="modal-footer">
    <button id="saveBtn" class="btn btn-primary popover-hide" data-trigger="manual"
            data-toggle="popover" data-placement="top" data-content="">
      <i class="fa fa-save"></i> 保存</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
  </form>
</body>
</html>
