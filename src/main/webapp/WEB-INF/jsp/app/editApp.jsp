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
  <script type="text/javascript" src="${ctx}/resources/js/uploadifive/jquery.uploadifive.min.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/busi/app/appEdit.js?v=${deployVersion}"></script>
  <link rel="stylesheet" type="text/css" href="${ctx}/resources/js/uploadifive/uploadifive.css" />
</head>
<body class="${webSkin}">

<form id="frmEdit" name="frmEdit" role="form" class="form-horizontal" action="${ctx}/app" method="post">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="myModalLabel">${empty app.appName?'提交':'升级'}</h4>
  </div>
  <div class="modal-body">
    <input type="hidden" name="_method" value="${empty app.appName?'post':'put'}"/>
      <input type="hidden" name="id" value="${app.id}"/>

      <div class="form-group">
          <label class="col-sm-2 control-label">应用名称</label>
          <div class="col-sm-10">
              <input name="appName" type="text" class="form-control" value="${app.appName}" placeholder="应用名称">
          </div>
      </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">应用说明</label>
      <div class="col-sm-10">
        <input name="description" type="text" class="form-control" value="${app.description}" placeholder="应用说明">
      </div>
    </div>

    <c:if test="${not empty app.appName}">
      <div class="form-group">
        <label class="col-sm-2 control-label">应用版本</label>
        <div class="col-sm-10">
          <input name="appVersionName" type="text" class="form-control" value="${app.appVersionName}" placeholder="应用版本" readonly>
        </div>
      </div>

      <div class="form-group">
        <label class="col-sm-2 control-label">应用大小</label>
        <div class="col-sm-10">
          <input name="fileSize" type="text" class="form-control" value="${app.fileSize}" placeholder="应用大小" readonly>
        </div>
      </div>
    </c:if>

    <div class="form-group">
      <label class="col-sm-2 control-label">应用位置</label>
      <div class="col-sm-10">
        <input name="appLocation" id="appLocation" type="text" class="form-control" value="${app.appLocation}" placeholder="应用位置" >
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">类别</label>
      <div class="col-sm-10">
        <select name="appType" class="form-control"  value="${app.appType}" >
          <option value="" <c:if test="${app.appType == null }"> selected="selected"</c:if>>--请选择--</option>
          <c:forEach items="${appTypes}" var="appType">
            <option value="${appType.name}" <c:if test="${app.appType != null && app.appType eq appType.name}"> selected="selected"</c:if>>${appType.name}</option>
          </c:forEach>
        </select>
        <%--<input name="uid" type="text" class="form-control" value="${terminal.uid}" placeholder="机构号">--%>
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">${empty app.appName?'选择文件':'更新文件'}</label>
      <div class="col-sm-10">
        <input type="file" id="uploadFile" name="uploadFile" />
        <div id="fileQueue"></div>
        <span id="uploadErrorMsg"
              style="padding-left: 16px;padding-bottom: 2px;font-weight: bold;color: #EA5200;">
                </span>
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">图标位置</label>
      <div class="col-sm-10">
        <input name="iconLocation" id="iconLocation" type="text" class="form-control" value="${app.iconLocation}" placeholder="图标位置" >
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">${empty app.appName?'选择文件':'更新文件'}</label>
      <div class="col-sm-10">
        <input type="file" id="uploadFileImg" name="uploadFileImg" />
        <div id="fileQueueImg"></div>
        <span id="uploadErrorMsgImg"
              style="padding-left: 16px;padding-bottom: 2px;font-weight: bold;color: #EA5200;">
                </span>
      </div>
    </div>

    </div>

  </div>
  <div class="modal-footer">
<shiro:hasPermission name="appManage:appControl:${empty app.appName?'CREATE':'UPDATE'}">
  <button id="saveBtn" class="btn btn-primary popover-hide" data-trigger="manual"
          data-toggle="popover" data-placement="top" data-content=""><i class="fa fa-save"></i> 保存</button>
</shiro:hasPermission>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</body>


<script type="text/javascript">
  var updateType = '${empty app.appName?'create':'update'}';
  var _loginappLevel='${sessionScope.appInfo.type}';
  $(document).ready(function () {
    app.import.init();
  });
</script>


</html>
