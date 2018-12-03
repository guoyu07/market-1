<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
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
  <script type="text/javascript" src="${ctx}/resources/js/validate/jquery.validate.min.js"></script>
</head>
<body class="${webSkin}">

<form id="frmUser" name="frmUser" role="form" class="form-horizontal" action="${ctx}/user" method="post">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="myModalLabel">${empty user.name?'添加':'编辑'}</h4>
  </div>
  <div class="modal-body">
    <input type="hidden" name="_method" value="${empty user.name?'post':'put'}"/>
      <input type="hidden" name="id" value="${user.id}"/>

      <div class="form-group">
          <label class="col-sm-2 control-label">登陆账号</label>
          <div class="col-sm-10">
              <input name="name" type="text"  id="name" class="form-control" onblur="checkName()" value="${user.name}" placeholder="只能输入英文和数字">
          </div>
      </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">机构名称</label>
      <div class="col-sm-10">
        <input name="account" type="text" class="form-control" value="${user.account}" placeholder="机构名称">
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">登陆密码</label>
      <div class="col-sm-10">
        <input name="password" type="password" class="form-control" placeholder="${empty user.name?'密码':'不填写表示不修改'}">
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">用户状态</label>
      <div class="col-sm-6">
        <select name="status" id="status" class="form-control">
          <option value="ON" ${user.status eq 'ON'?'selected':''}>有效</option>
          <option value="OFF" ${user.status eq 'OFF'?'selected':''}>无效</option>
        </select>
      </div>
    </div>

    <div id="divRole" class="form-group" style="display: ${roleShowStat};">
      <label class="col-sm-2 control-label">用户角色</label>
      <div class="col-sm-10">
        <c:forEach var="vo" items="${roles}" varStatus="vs">
          <input id="role_${vo.id}" type="checkbox" name="roleIds" value="${vo.id} " ${vo.owner?'checked':''} />
          <label for="role_${vo.id}">${vo.name}</label>&nbsp;
        </c:forEach>
      </div>
    </div>

  </div>
  <div class="modal-footer">
<shiro:hasPermission name="sysManage:userManage:${empty user.name?'CREATE':'UPDATE'}">
  <button id="saveBtn" class="btn btn-primary popover-hide" data-trigger="manual"
          data-toggle="popover" data-placement="top" data-content=""><i class="fa fa-save"></i> 保存</button>
</shiro:hasPermission>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</body>


<script type="text/javascript">
  var updateType = '${empty user.name?'create':'update'}';
  var _loginUserLevel='${sessionScope.userInfo.type}';
  $(document).ready(function () {
    user.info.init();
  });

  function checkName() {
      if(/^[0-9a-zA-Z]+$/ig.test($("#name")[0].value)){
          $("#saveBtn").removeAttr("disabled");
      }
      else {
          $("#name")[0].placeholder = "只能输入英文和数字";

          $("#saveBtn").attr("disabled","disabled");
      }

  }
</script>
</html>
