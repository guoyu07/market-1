<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  app: Admin
  Date: 2015/3/2
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
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
  <script type="text/javascript" src="${ctx}/resources/js/busi/app/whiteList.js?v=${deployVersion}"></script>
</head>
<body class="${webSkin}">

<form id="frmEdit" name="frmEdit" role="form" class="form-horizontal" action="${ctx}/app/whiteList" method="post">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="myModalLabel">${empty app.packetName?'添加':'编辑'}</h4>
  </div>
  <div class="modal-body">
    <input type="hidden" name="_method" value="${empty app.packetName?'post':'put'}"/>
      <input type="hidden" name="id" value="${app.id}"/>

      <div class="form-group">
          <label class="col-sm-2 control-label">应用包名</label>
          <div class="col-sm-10">
              <input name="packetName" type="text" class="form-control" value="${app.packetName}" placeholder="应用包名">
          </div>
      </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">应用说明</label>
      <div class="col-sm-10">
        <input name="descript" type="text" class="form-control" value="${app.descript}" placeholder="应用说明">
      </div>
    </div>


    </div>

  </div>
  <div class="modal-footer">
<shiro:hasPermission name="appManage:appWhitelist:${empty app.packetName ?'CREATE':'UPDATE'}">
  <button id="saveBtn" class="btn btn-primary popover-hide" data-trigger="manual"
          data-toggle="popover" data-placement="top" data-content=""><i class="fa fa-save"></i> 保存</button>
</shiro:hasPermission>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</body>


<script type="text/javascript">
  var updateType = '${empty app.packetName ? 'create':'update'}';
  var _loginappLevel='${sessionScope.appInfo.type}';
  $(document).ready(function () {
      whiteList.info.init();
  });
</script>


</html>
