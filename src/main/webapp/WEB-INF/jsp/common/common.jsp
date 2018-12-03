<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>


<%--<link rel="shortcut icon" href="${ctx}/resources/images/favicon.ico" />--%>

<%--jquery 2.X 不支持IE6，7，8--%>
<script type="text/javascript" src="${ctx}/resources/js/common/jquery-1.9.1.js"></script>
<%--<script type="text/javascript" src="${ctx}/resources/js/common/jquery-2.1.3.min.js"></script>--%>
<script type="text/javascript" src="${ctx}/resources/js/common/ajax.util.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common/common.js"></script>
<script type="text/javascript" src="${ctx}/resources/lib/bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script type="text/javascript" src="${ctx}/resources/lib/adminLTE/js/AdminLTE/app.js"></script>
<script type="text/javascript" src="${ctx}/resources/lib/layer-v1.9.3/layer.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/select2/js/select2.full.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/select2/js/i18n/zh-CN.js"></script>


<link rel="stylesheet" type="text/css" href="${ctx}/resources/lib/bootstrap/css/bootstrap.css">
<!-- font Awesome -->
<link rel="stylesheet" type="text/css" href="${ctx}/resources/lib/adminLTE/css/font-awesome.min.css" />
<!-- Ionicons -->
<link rel="stylesheet" type="text/css" href="${ctx}/resources/lib/adminLTE/css/ionicons.min.css" />
<!-- Theme style -->
<link rel="stylesheet" type="text/css" href="${ctx}/resources/lib/adminLTE/css/AdminLTE.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/theme-orange.css" />


<link rel="stylesheet" type="text/css" href="${ctx}/resources/js/select2/css/select2.min.css">
<!--[if lt IE 9]>
<script type="text/javascript" src="${ctx}/resources/js/common/html5shiv.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common/respond.min.js"></script>
<![endif]-->
<%--EL（Expression Language）是一门表达式语言，它对应<%=…%>。我们知道在JSP中，表达式会被输出，所以EL表达式也会被输出--%>
<%--EL一共11个内置对象，无需创建即可以使用，10个是Map类型的，1个是pageContext对象--%>
<%--${applicationScope.themeColor}等同与application.getAttribute(“themeColor”)--%>

<c:set var="webSkin" value="${applicationScope.themeColor}" />
<script type="text/javascript">
    var ctx = "${ctx}";
    var varSearchKeyMinLength=${sessionScope.userInfo.type eq 3?0:2};
</script>
<style type="text/css">
    .cover {
        position:fixed; top: 0px; right:0px; bottom:0px;filter: alpha(opacity=60); background-color: #777;
        z-index: 9999; left: 0px; display:none;
        opacity:0.5; -moz-opacity:0.5;
    }
</style>
<div id="cover" class="cover"></div>

