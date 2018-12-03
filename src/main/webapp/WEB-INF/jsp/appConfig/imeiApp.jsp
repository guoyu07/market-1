<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  app: Admin
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

<form id="frmEdit" name="frmEdit" role="form" class="form-horizontal" action="${ctx}/app" method="post">
  <input type="hidden" id="imeiJsonList" value="${imeiJsonList}"/>
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="myModalLabel">添加应用</h4>
  </div>
  <div class="modal-body">

    <div class="box">
      <div class="box-body table-responsive">
        <table id="tblData" class="table table-bordered table-hover dataTable">
          <thead>
          <tr>
            <th>应用id</th>
            <th>应用名称</th>
            <th>应用版本</th>
            <th>应用大小</th>
            <th>全选<input type="checkbox" id="selectAllApp"></th>
          </tr>
          </thead>
          <tbody>
          <c:choose>
            <c:when test="${not empty tbAppStoreList}">
              <c:forEach items="${tbAppStoreList}" var="item">
                <tr data-appid="${item.id}" data-check="${item.isCheck}">
                  <td>${item.id}</td>
                  <td>${item.appName}</td>
                  <td>${item.appVersionName}</td>
                  <td><fmt:formatNumber type="number" value="${(item.fileSize)/1024/1024}" pattern="0.00" maxFractionDigits="2"/>M</td>
                  <c:choose>
                    <c:when test="${item.isCheck == 'true'}">
                      <td><input  checked type="checkbox" name="imeiCheck" onclick="appCheck($(this))" value="${item.id}" id=""></td>
                    </c:when>
                    <c:otherwise>
                      <td><input type="checkbox" name="imeiCheck" onclick="appCheck($(this))" value="${item.id}" id=""></td>
                    </c:otherwise>
                  </c:choose>

                </tr>
              </c:forEach>

            </c:when>
            <c:otherwise>
              <tr>
                <td colspan="20" style="text-align: center"> 该账户还没有配置相关应用。</td>
              </tr>
            </c:otherwise>
          </c:choose>

          </tbody>
        </table>
        <div id="Pagination"></div>
      </div>
    </div>

  </div>

  <div class="modal-footer">
<shiro:hasPermission name="appManage:appControl:${empty app.appName?'CREATE':'UPDATE'}">
  <c:if test="${not empty tbAppStoreList}">
    <button id="saveBtn" onclick="return saveApp();" class="btn btn-primary popover-hide" data-trigger="manual"
            data-toggle="popover" data-placement="top" data-content=""><i class="fa fa-save"></i> 保存</button>
  </c:if>

</shiro:hasPermission>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</body>


<script type="text/javascript">
    function appCheck(o) {
        if(o[0].checked){
            o.parent().parent().attr("data-check",true);
        }else{
            o.parent().parent().attr("data-check",false);
        }
    }

    function saveApp() {
        var params = [];
        var tblData = $('#tblData');
        var count = 0;
        for(var i=1;i < tblData[0].rows.length;i++){
            if(tblData[0].rows[i].dataset.check=="true"){
                count++;
                params.push(tblData[0].rows[i].dataset["appid"]);
            }
        }
        console.info(JSON.stringify(params));
        if(count == 0){
            alert("请选择需要配置的应用。");
            return false;
        }
        else {
            console.log(JSON.stringify(params));
            debugger
            var imeiJsonList = $("#imeiJsonList").attr("value");
            document.write("<form action='" + ctx+"/app/config/saveApp'"+ " method='post' name='form1' style='display:none'>");
            document.write("<input type='hidden' name='appJsonList' value='"+JSON.stringify(params)+"'>");
            document.write("<input type='hidden' name='imeiJsonList' value='"+imeiJsonList+"'>");
            document.write("</form>");
            document.form1.submit();
            return true;
        }

    }
    $("#selectAllApp").click(function(){
        $("#frmEdit").find("input[name='imeiCheck']").each(function() {
            if($("#selectAllApp")[0].checked)
                $(this).prop("checked",true);
            else
                $(this).prop("checked",false);
        });
    });
</script>


</html>
