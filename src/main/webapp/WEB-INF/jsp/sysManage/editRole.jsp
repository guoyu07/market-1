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
  <script type="text/javascript" src="${ctx}/resources/js/busi/sysManage/roleList.js?v=${deployVersion}"></script>
  <script type="text/javascript" src="${ctx}/resources/lib/adminLTE/js/plugins/slimScroll/jquery.slimscroll.min.js"></script>

  <script type="text/javascript" src="${ctx}/resources/js/jstree/jstree.min.js"></script>
  <link rel="stylesheet" type="text/css" href="${ctx}/resources/js/jstree/themes/default/style.min.css" />

  <jsp:include page="../common/commonFrame.jsp">
    <jsp:param name="needPage" value="N"/>
    <jsp:param name="needDate" value="N"/>
    <jsp:param name="needValidate" value="Y"/>
  </jsp:include>

</head>
<body class="${webSkin}">

<form id="frmEdit" name="frmEdit" role="form" class="form-horizontal" action="role" method="post">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="myModalLabel">${empty role.id?'添加':'编辑'}</h4>
  </div>
  <div class="modal-body">
    <input type="hidden" name="_method" value="${empty role.id?'post':'put'}"/>
    <input type="hidden" name="id" value="${role.id}"/>
    <input type="hidden" name="permissions" value=""/>
    <div class="form-group">
      <label class="col-sm-2 control-label">名 称：</label>
      <div class="col-sm-10">
        <input name="name" type="text" class="form-control" value="${role.name}" placeholder="字母、数字、下划线组成，字母开头">
      </div>
    </div>
   <%-- <c:if test="${sessionScope.userInfo.type eq 1}">
      <div class="row form-group">
        <label class="col-sm-2 control-label">级别：</label>
        <div class="col-sm-10">
          <select name="sysLevel">
            <option value="1" ${role.sysLevel eq '1'?'selected':''}>平台级</option>
            <option value="2" ${role.sysLevel eq '2'?'selected':''}>机构级</option>
            &lt;%&ndash;<option value="3" ${role.sysLevel eq '3'?'selected':''}>商户级</option>&ndash;%&gt;
          </select>
        </div>

      </div>
    </c:if>--%>
    <div class="form-group">
      <label class="col-sm-2 control-label">备 注：</label>
      <div class="col-sm-10">
        <input name="memo" type="text" class="form-control" value="${role.memo}" placeholder="备注">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-2 control-label">权 限：</label>
      <div class="col-sm-10">
        <div id="role-box" class="box-body">
          <div id="menuTree"></div>
        </div>
      </div>
    </div>

  </div>
  <div class="modal-footer">
    <c:if test="${sessionScope.userInfo.type eq '1' or (sessionScope.userInfo.type ne '1' and role.createUser ne 'sys')}">
      <shiro:hasPermission name="sysManage:roleManage:${empty role.id?'CREATE':'UPDATE'}">
        <button id="saveBtn" class="btn btn-primary popover-hide" data-trigger="manual"
                data-toggle="popover" data-placement="top" data-content=""><i class="fa fa-save"></i> 保存</button>
      </shiro:hasPermission>
      <c:if test="${not empty role.id}">
        <shiro:hasPermission name="sysManage:roleManage:DELETE">
          <button id="delBtn" type="button" class="btn btn-primary popover-hide" data-trigger="manual"
                  data-toggle="popover" data-placement="top" data-content=""><i class="fa fa-undo"></i> 删除</button>
        </shiro:hasPermission>
      </c:if>
    </c:if>


    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</body>
<script type="text/javascript">
  var updateType = '${empty role.id?'create':'update'}';
  var menuData =${menuData};
  $(document).ready(function () {
    role.info.init();
  });
</script>
</html>
