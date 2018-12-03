<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>   <title>应用市场管理系统</title>
    <meta name="content-type" content="text/html; charset=UTF-8">
  <jsp:include page="../common/commonFrame.jsp">
    <jsp:param name="needPage" value="N"/>
    <jsp:param name="needDate" value="N"/>
    <jsp:param name="needValidate" value="Y"/>
  </jsp:include>
  <script type="text/javascript" src="${ctx}/resources/js/validate/jquery.validate.min.js"></script>
</head>
<body class="${webSkin}">

<form id="frmUser" name="frmUser" role="form" class="form-horizontal" action="${ctx}/app/type/add" method="post">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="myModalLabel">添加</h4>
  </div>
  <div class="modal-body">
      <input name="id" value="${type.id}" type="hidden"/>
      <div class="form-group">
          <label class="col-sm-2 control-label">类别</label>
          <div class="col-sm-10">
              <input name="name" type="text"  id="name" class="form-control" placeholder="输入类别名称" value="${type.name}">
          </div>
      </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">备注</label>
      <div class="col-sm-10">
        <input name="remark" type="text" class="form-control"  placeholder="输入备注" value="${type.remark}">
      </div>
    </div>



  </div>
  <div class="modal-footer">
<shiro:hasPermission name="sysManage:appTypeManage:CREATE">
  <%--<button id="saveBtn" class="btn btn-primary popover-hide" data-trigger="manual"--%>
          <%--data-toggle="popover" data-placement="top" data-content=""><i class="fa fa-save"></i> 保存</button>--%>
    <input type="submit" lass="btn btn-primary popover-hide"  value="保存"/>
</shiro:hasPermission>
    <%--<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
  </div>
</form>
</body>


<script type="text/javascript">
  var updateType = '${empty user.name?'create':'update'}';
  var _loginUserLevel='${sessionScope.userInfo.type}';
  $(document).ready(function () {
    user.info.init();
  });

</script>
</html>
