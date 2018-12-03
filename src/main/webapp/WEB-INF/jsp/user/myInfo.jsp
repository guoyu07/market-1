<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>   <title>应用市场管理系统</title>
</head>
<body class="${webSkin}">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="myModalLabel">我的信息</h4>
  </div>

  <div class="modal-body">
    <form role="form" class="form-horizontal">

      <div class="form-group">
        <label class="col-sm-4 control-label">机构号：</label>
        <div class="col-xs-6"><input type="text" class="form-control" value="${user.id}" readonly/></div>
      </div>

      <div class="form-group">
        <label class="col-sm-4 control-label">机构名称：</label>
        <div class="col-xs-6"><input type="text" class="form-control" value="${user.account}" readonly/></div>
      </div>

      <div class="form-group">
        <label class="col-sm-4 control-label">登陆账号：</label>
        <div class="col-xs-6"><input type="text" class="form-control" value="${user.name}" readonly/></div>
      </div>

      <div class="form-group">
        <label class="col-sm-4 control-label">状态：</label>
        <div class="col-xs-6"><input type="text" class="form-control" value="${user.status}" readonly/></div>
      </div>
    </form>


  </div>
  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</body>
</html>
