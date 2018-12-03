<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>应用市场管理系统</title>
    <%@ include file="common/common.jsp" %>
</head>
<body class="${webSkin}">
<%@ include file="include/navbar.jsp" %>
<div class="wrapper row-offcanvas row-offcanvas-left">
    <jsp:include page="include/left.jsp" />

    <aside class="right-side">
        <section class="content-header">
            <h1>首页</h1>
            <ol class="breadcrumb">
                <li><a href="${ctx}/index"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">index</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">


        </section><!-- /.content -->
    </aside><!-- /.right-side -->
</div><!-- ./wrapper -->
<%@ include file="include/footer.jsp" %>
</body>
</html>
 
 
 

