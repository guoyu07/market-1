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

  <%@ include file="../common/common.jsp" %>
  <jsp:include page="../common/commonFrame.jsp">
    <jsp:param name="needPage" value="Y"/>
    <jsp:param name="needDate" value="Y"/>
    <jsp:param name="needValidate" value="Y"/>
  </jsp:include>
  <script type="text/javascript" src="${ctx}/resources/js/busi/sysManage/userList.js?v=${deployVersion}"></script>


  <script type="text/javascript">
    $(document).ready(function () {
      user.list.init();
    });

  </script>


</head>
<body class="${webSkin}">

<%@ include file="../include/navbar.jsp" %>
<div class="wrapper row-offcanvas row-offcanvas-left">
  <!-- Left side column. contains the logo and sidebar -->
  <jsp:include page="../include/left.jsp">
    <jsp:param name="enName" value="userManage" />
  </jsp:include>

  <!-- Right side column. Contains the navbar and content of the page -->
  <aside class="right-side">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>机构管理</h1>
      <ucfPay:ContentHeaderTag enName="userManage" />
    </section>

    <!-- Main content -->
    <section class="content">
      <form id="frmQry" name="frmQry" role="form" action="" METHOD="post" class="form-horizontal">
        <input type="hidden" name="page" value="0"/>
        <input type="hidden" name="size" value="20"/>
        <input type="hidden" name="sort" value="create_time,desc"/>
      <div class="box box-primary">
        <div class="box-body">
            <%--<div class="row form-group">
              <label class="col-sm-2 control-label">创建时间：</label>
              <div class="col-xs-2">
                <input type="text" id="beginDate" name="beginDate"  class="form-control dateSelector" />
              </div>
              <div class="col-xs-2">
                <input type="text" id="endDate" name="endDate"  class="form-control dateSelector" />
              </div>
              <div class="dateTagList">
                <a href="javascript:void(0);" dateTagIndex="0">昨天</a>
                <a href="javascript:void(0);" dateTagIndex="1">最近七天</a>
                <a href="javascript:void(0);" dateTagIndex="2">最近1个月</a>
                <a href="javascript:void(0);" dateTagIndex="3">3个月</a>
                <a href="javascript:void(0);" dateTagIndex="4">1年</a>
              </div>
            </div>--%>
            <div class="row form-group">
              <label class="col-sm-2 control-label">姓名：</label>
              <div class="col-xs-3"><input type="text" name="nameLike" class="form-control" /></div>
              <c:if test="${userInfo.type eq 1}">
                <label class="col-sm-2 control-label">状态：</label>
                <div class="col-xs-3">
                  <select name="status">
                      <option value="ON">ON</option>
                      <option value="OFF">OFF</option>
                  </select>
                </div>
              </c:if>
            </div>

              <div class="row form-group">
                <label class="col-sm-2 control-label">机构号：</label>
                <div class="col-xs-3"><input type="text" name="id" class="form-control" /></div>
              </div>
        </div>
        <div class="box-footer">
          <button id="btnQry" type="button" class="btn"><i class="fa fa-search"></i>查询</button>
          <shiro:hasPermission name="sysManage:userManage:CREATE">
              <%--<button id="btnExport" type="button" class="btn">导出用户</button>--%>
            <a href="user/0" type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal"><i class="fa fa-plus"></i>添加机构</a>
              <%--<a href="user/preImport" type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">
                  <i class="fa fa-plus"></i> 导入用户
              </a>--%>
          </shiro:hasPermission>
        </div>
        </div>
      </form>
      <div class="box">
        <div class="box-body table-responsive">
          <table id="tblData" class="table table-bordered table-hover dataTable">
            <thead>
            <tr>
              <th>登陆账号</th>
              <th>机构号</th>
              <th>机构名称</th>
              <th>状态</th>
              <th>创建时间</th>
              <th>最后更新时间</th>
            </tr>
            </thead>
            <tbody><tr><td colspan="20" style="text-align: center"> 请选择搜索条件后点击“查询”键。</td></tr></tbody>
          </table>
          <div id="Pagination"></div>
        </div>
      </div>

    </section><!-- /.content -->
  </aside><!-- /.right-side -->
</div><!-- ./wrapper -->
<%@ include file="../include/footer.jsp" %>
</body>
</html>
