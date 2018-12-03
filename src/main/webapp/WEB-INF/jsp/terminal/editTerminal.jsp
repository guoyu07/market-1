<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  terminal: Admin
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
</head>
<body class="${webSkin}">

<form id="frmterminal" name="frmterminal" role="form" class="form-horizontal" action="${ctx}/terminal" method="post">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="myModalLabel">${empty terminal.imei?'添加':'编辑'}</h4>
  </div>
  <div class="modal-body">
    <input type="hidden" name="_method" value="${empty terminal.imei?'post':'put'}"/>
      <input type="hidden" name="id" value="${terminal.imei}"/>

      <div class="form-group">
          <label class="col-sm-2 control-label">IMEI号</label>
          <div class="col-sm-10">
            <input name="imei" type="text" <c:if test="${!empty terminal.imei}"> readonly </c:if> class="form-control" value="${terminal.imei}" placeholder="IMEI号">
          </div>
      </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">机身号</label>
      <div class="col-sm-10">
        <%--<c:if test="${!empty terminal.imei}"> readonly </c:if>--%>
        <input name="sn" type="text"  class="form-control" value="${terminal.sn}" placeholder="机身号">
      </div>
    </div>

    <c:if test="${loginUser.type == 1}">
      <div class="form-group">
        <label class="col-sm-2 control-label">机构号</label>
        <div class="col-sm-10">
          <select name="uid" class="form-control"  value="${terminal.uid}" >
            <option value="" <c:if test="${terminal.uid == null }"> selected="selected"</c:if>>--请选择--</option>
            <c:forEach items="${users}" var="user">
              <option value="${user.id}" <c:if test="${terminal.uid != null && terminal.uid eq user.id}"> selected="selected"</c:if>>${user.id}-${user.account}</option>
            </c:forEach>
          </select>
          <%--<input name="uid" type="text" class="form-control" value="${terminal.uid}" placeholder="机构号">--%>
        </div>
      </div>
    </c:if>

      <div class="form-group">
          <label class="col-sm-2 control-label">区域</label>
          <div class="col-sm-10">
              <select name="area" class="form-control"  value="${terminal.area}" >
                  <option value="" <c:if test="${terminal.uid == null }"> selected="selected"</c:if>>--请选择--</option>
                  <c:forEach items="${areas}" var="area">
                      <option value="${area.name}" <c:if test="${terminal.area != null && terminal.area eq area.name}"> selected="selected"</c:if>>${area.name}</option>
                  </c:forEach>
              </select>
              <%--<input name="uid" type="text" class="form-control" value="${terminal.uid}" placeholder="机构号">--%>
          </div>
      </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">商铺名称</label>
      <div class="col-sm-10">
        <input name="shopName" type="text" class="form-control" value="${terminal.shopName}" placeholder="商铺名称">
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">联系人</label>
      <div class="col-sm-10">
        <input name="shopContacts" type="text" class="form-control" value="${terminal.shopContacts}" placeholder="商铺联系人">
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">联系方式</label>
      <div class="col-sm-10">
        <input name="shopPhone" type="text" class="form-control" value="${terminal.shopPhone}" placeholder="商铺联系方式">
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">经度</label>
      <div class="col-sm-10">
        <input name="shopLongitude" type="text" class="form-control" value="${terminal.shopLongitude}" placeholder="经度">
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">纬度</label>
      <div class="col-sm-10">
        <input name="shopLatitude" type="text" class="form-control" value="${terminal.shopLatitude}" placeholder="纬度">
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label">备注</label>
      <div class="col-sm-10">
        <input name="remark" type="text" class="form-control" value="${terminal.remark}" placeholder="备注">
      </div>
    </div>

  </div>
  <div class="modal-footer">
<shiro:hasPermission name="terminalManage:terminalControl:${empty terminal.imei?'CREATE':'UPDATE'}">
  <button id="saveBtn" class="btn btn-primary popover-hide" data-trigger="manual"
          data-toggle="popover" data-placement="top" data-content=""><i class="fa fa-save"></i> 保存</button>
</shiro:hasPermission>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</body>


<script type="text/javascript">
  var updateType = '${empty terminal.imei?'create':'update'}';
  var _loginterminalLevel='${sessionScope.terminalInfo.type}';
  $(document).ready(function () {
    terminal.info.init();
  });
</script>
</html>
