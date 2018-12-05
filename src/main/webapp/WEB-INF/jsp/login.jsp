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

            $('#signinBTN').click(function () {
                var index = layer.open({
                    id:1,
                    type: 1,
                    title:'用户注册',
                    skin:'layui-layer-rim',
                    area:['450px', 'auto'],
                    content:$("#regUser")
                    // btn:['保存','取消'],
                    // btn1: function (index,layero) {
                    //     layer.alert(index)
                    // },
                    // btn2:function (index,layero) {
                    //     layer.close(index);
                    // }
                });

                $("#cancelRegBTN").click(function() {
                    layer.close(index);
                })

            })

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

        // 检测注册信息完整性
        function checkRegValue() {

            var isErr = false;
            var errmsg = "参数错误";
            var username = $("#username").val();
            var email = $("#email").val();
            var pwd = $("#pwd").val();
            var secondpwd = $("#secondpwd").val();

            var reName = new RegExp("^[a-zA-Z]+$");
            var reEmil =  /\w@\w*\.\w/;

            if(!$("#mustClick").prop('checked')) {
                errmsg = "注册用户必须同意协议";
                isErr = true;
            }else if(!username || !email || !pwd || !secondpwd) {
                errmsg = "参数不能为空";
                isErr = true;
            }else if(!reName.test(username)) {
                errmsg = "用户名格式有误";
                isErr = true;
            } else if ( pwd != secondpwd ) {
                errmsg = "两次密码不一致";
                isErr = true;
            } else if(!reEmil.test(email)) {
                errmsg = "邮箱格式有误";
                isErr = true;
            }

            if(isErr)
                layer.msg(errmsg)
            else {
                $.post("${ctx}/sys/login")
            }

        }

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
            <button type="button" id="signinBTN" class="btn ${applicationScope.loginBtColor} btn-block">注&nbsp;&nbsp;&nbsp;&nbsp;册</button>
        </div>
    </form>


    <div class="row" id="regUser"  style="width: 420px; display:none; margin-left:7px; margin-top:10px;">
        <div class="col-sm-12">
            <div class="input-group"><span class="input-group-addon"> 邮   箱   :</span>
                <input id="email" type="text" class="form-control"placeholder="请输入你的邮箱">
            </div>
        </div>
        <div class="col-sm-12" style="margin-top: 10px">
            <div class="input-group"><span class="input-group-addon"> 用 户 名  :</span>
                <input id="username" type="text"class="form-control" placeholder="请输入你的用户名(大小写英文)">
            </div>
        </div>
        <div class="col-sm-12" style="margin-top: 10px">
            <div class="input-group"><span class="input-group-addon"> 密 码   :</span>
                <input id="pwd" type="password" class="form-control" placeholder="请输入你的密码"></div>
        </div>
        <div class="col-sm-12" style="margin-top: 10px">
            <div class="input-group"><span class="input-group-addon">确认密码:</span>
                <input id="secondpwd" type="sedpassword" class="form-control" placeholder="请再输入一次密码"></div>
        </div>

        <div class="col-sm-12 pull-right" style="margin-top: 10px">
            <span class="ant-checkbox ant-checkbox-checked"><input type="checkbox" id="mustClick" class="ant-checkbox-input">
                <span class="ant-checkbox-inner"></span></span>
            <span><span>同意<a href="${ctx}/resources/agreement.html" target="_blank">《九磊科技公司信息服务条款、隐私政策》</a></span></span>
            <button type="button" onclick="checkRegValue()" id="regBTN" class="btn  btn-success">注册</button>
            <button type="button" id="cancelRegBTN" class="btn btn-info">取消</button>
        </div>

    </div>

</div>
</body>

</html>
