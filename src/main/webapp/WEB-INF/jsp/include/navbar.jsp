<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglibs.jsp" %>
<%@ include file="../common/dailog.jsp" %>
<header class="header">

  <a href="${ctx}/index" class="logo">
    <c:choose>
      <c:when test="${userInfo.type eq 3}">
        ${applicationScope.navBarTitleNameForAgent}
      </c:when>
      <c:otherwise>
        ${applicationScope.navBarTitleName}
      </c:otherwise>
    </c:choose>
  </a>
  <!-- Header Navbar: style can be found in header.less -->
  <nav class="navbar navbar-static-top" role="navigation">
    <!-- Sidebar toggle button-->
    <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </a>
    <div class="navbar-right">
      <ul class="nav navbar-nav">

        <li class="dropdown user user-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <i class="glyphicon glyphicon-user"></i>
            <span>${userInfo.name} <i class="caret"></i></span>
          </a>
          <ul class="dropdown-menu">

            <!-- Menu Body -->
            <li class="user-body">
              <div class="col-xs-6 text-center">
                <a href="${ctx}/myInfo" data-toggle="modal" data-target="#myModal">账户资料</a>
              </div>
              <div class="col-xs-6 text-center">
                <a href="${ctx}/myInfo/pwd" data-toggle="modal" data-target="#myModal">修改密码</a>
              </div>
            </li>
            <!-- Menu Footer-->
            <li class="user-footer">
              <%--<div class="pull-left">
                <a href="#" class="btn btn-default btn-flat">Profile</a>
              </div>--%>
              <div class="pull-right">
                <a href="${ctx}/sys/logout" class="btn btn-default btn-flat">退出</a>
              </div>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </nav>
</header>

