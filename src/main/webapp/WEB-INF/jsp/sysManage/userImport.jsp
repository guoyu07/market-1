<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>应用市场管理系统</title>
    <jsp:include page="../common/commonFrame.jsp">
        <jsp:param name="needPage" value="N"/>
        <jsp:param name="needDate" value="N"/>
        <jsp:param name="needValidate" value="Y"/>
    </jsp:include>
    <script type="text/javascript" src="${ctx}/resources/js/uploadifive/jquery.uploadifive.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/js/uploadifive/uploadifive.css" />
</head>
<body class="${webSkin}">

<form id="frmEdit" name="frmEdit" role="form" class="form-horizontal" action="${ctx}/user/fileImport" method="post">
    <input type="hidden" name="_method" id="_method" value="post"/>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">批量导入用户</h4>
    </div>
    <div class="modal-body">
        <div class="form-group">
            <input type="hidden" id="userFile" name="userFile" value=""/>
            <label class="col-sm-4 control-label">用户文件：</label>
            <div class="col-sm-8">
                <input type="file" id="uploadFile" name="uploadFile" />
                <div id="fileQueue"></div>
                <span id="uploadErrorMsg"
                      style="padding-left: 16px;padding-bottom: 2px;font-weight: bold;color: #EA5200;"></span>
                <br><a href="<c:url value="/resources/demo/user.xls"/>">下载用户文件示例</a>
            </div>
        </div>
        <div class="form-group"></div>
    </div>

    <div class="modal-footer">
        <%--<shiro:hasPermission name="sysManage:userManage::CREATE">
            <button id="saveBtn" class="btn btn-primary popover-hide" data-trigger="manual" data-toggle="popover" data-placement="top" data-content="">
                <i class="fa fa-save"></i> 上传并保存
            </button>
        </shiro:hasPermission>--%>
        <button id="saveBtn" class="btn btn-primary popover-hide" data-trigger="manual" data-toggle="popover" data-placement="top" data-content="">
            <i class="fa fa-save"></i> 上传并保存
        </button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</form>
</body>
<script type="text/javascript">
    $(document).ready(function () {
        user.import.init();
    });

    /*$("#saveBtn").click(function () {
        ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(), function (result) {
            if (result.status == 'OK') {
                user.list.getRecords();
                $('[data-dismiss]').click();
                $.pnotify({title: result.message, type: result.status});
            } else {
                $("#saveBtn").attr("data-content", result.message);
                $("#saveBtn").popover('toggle');
                setTimeout(function () {
                    $("#saveBtn").popover('toggle');
                }, 2000);
            }
        });

    });*/


</script>
</html>
