<%-- jsp 常用编译指令 page、include、taglib --%>
<%-- page：针对当前页面，定义页面属性，contentType：设定生成网页的文件格式以及编码字符集，同时设置content-type响应头 language：脚本语言种类，默认为java
errorPage：设置错误处理页面 isErrorPage：设置本页面是否为错误处理程序 --%>
<%-- include：将外部jsp文件嵌入到当前的jsp文件中（静态导入），编译指令起作用 --%>
<%-- taglib：定义和访问自定义标签 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="bg-blue" xmlns="http://www.w3.org/1999/xhtml">
<head>   <title>应用市场管理系统</title>
    <%-- jsp常用动作指令 jsp:include jsp:forward jsp:param  --%>
    <%-- jsp:include 动态引入一个jsp页面（动态导入），可增加额外参数，但编译指令不起作用，插入目标页面 --%>
    <%-- jsp:forward 执行页面跳转，将请求处理发到下一个页面，目标页面替换原页面 --%>
    <%-- jsp:param 用户传递参数，必须与其他支持参数的标签一起使用 --%>
    <%@ include file="common/common.jsp" %>
    <jsp:include page="common/commonFrame.jsp">
        <jsp:param name="needPage" value="N"/>
        <jsp:param name="needDate" value="N"/>
        <jsp:param name="needValidate" value="Y"/>
    </jsp:include>
    <script type="text/javascript">
        $(document).ready(function () {

            $('#kaptchaImage').click(function () {
                $('#kaptchaImage').hide().attr('src', ctx + '/resources/kaptcha.jpg?' + Math.floor(Math.random() * 100)).fadeIn();
            });

            $("#frmLogin").validate({
                rules: {
                    account: {
                        required: true
                    },
                    password: {
                        required: true
                    },
                    verifycode: {
                        required: true,
                        minlength: 4
                    }
                },
                messages: {
                    verifycode: {
                        remote: "验证码不正确"
                    }
                }
            });
        });

    </script>

    <style type="text/css">
        .loginmain{ background:url(/resources/images/hear.jpg)}
    </style>
</head>

<body class="bg-blue ${webSkin}" >
<div class="form-box" id="login-box" >
    <div class="header">
        <c:choose>
            <c:when test="${empty applicationScope.loginLogoPic}">应用市场管理系统</c:when>
            <c:otherwise><img src="${ctx}/resources/images/${applicationScope.loginLogoPic}" width="150"/></c:otherwise>
        </c:choose>
    </div>
    <form id="frmLogin" name="frmLogin" class="form-horizontal" action="${ctx}/sys/login" method="post">
        <div class="body bg-gray">
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="text" name="name" value="" class="form-control" placeholder="请输入用户名"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="password" name="password" value="" class="form-control" placeholder="请输入密码"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-6"><input name="verifycode" type="text"  maxlength="4" class="form-control" placeholder="请输入验证码" autocomplete="off"/></div>
                <span style="float: left"><a href="javascript:void(0);" class="verifiImg"><img src="${ctx}/resources/kaptcha.jpg" id="kaptchaImage" /></a></span>
            </div>
            <c:if test="${not empty error}">
                <div class="form-group" style="  margin-left: 0px;">
                        ${error}
                </div>
            </c:if>
        </div>
        <div class="footer">
            <button type="submit" class="btn ${applicationScope.loginBtColor} btn-block">登&nbsp;&nbsp;&nbsp;&nbsp;录</button>
        </div>
    </form>

</div>
</body>

</html>
