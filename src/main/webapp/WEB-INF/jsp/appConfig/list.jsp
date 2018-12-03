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
  <script type="text/javascript" src="${ctx}/resources/js/busi/appConfig/appConfig.js?v=${deployVersion}"></script>

</head>
<body class="${webSkin}">

<%@ include file="../include/navbar.jsp" %>
<div class="wrapper row-offcanvas row-offcanvas-left">
  <!-- Left side column. contains the logo and sidebar -->
  <jsp:include page="../include/left.jsp">
    <jsp:param name="enName" value="appConfig" />
  </jsp:include>

  <!-- Right side column. Contains the navbar and content of the page -->
  <aside class="right-side">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>应用配置</h1>
      <ucfPay:ContentHeaderTag enName="appConfig" />
    </section>

    <!-- Main content -->
    <section class="content">
      <form id="frmQry" name="frmQry" role="form" action="" METHOD="post" class="form-horizontal">
        <input type="hidden" name="page" value="0"/>
        <input type="hidden" name="size" value="50"/>
        <input type="hidden" name="sort" value="create_time,desc"/>
      <div class="box box-primary">
        <div class="box-body">
           <%-- <div class="row form-group">
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
            <div class="row form-group" <%--style="margin-left: -147px;"--%>>
              <label class="col-sm-1 control-label">IMEI号：</label>
              <div class="col-sm-2"><input type="text" name="nameLike" class="form-control" /></div>
              <label class="col-sm-1 control-label">机构：</label>
              <div class="col-sm-2">
                <select name="uidName" class="form-control" >
                  <option value="">--请选择--</option>
                  <c:forEach items="${users}" var="user">
                    <option value="${user.id}">${user.id}-${user.account}</option>
                  </c:forEach>
                </select>
              </div>
              <label class="col-sm-1 control-label">区域：</label>
              <div class="col-sm-2">
                <select name="areaName" class="form-control" >
                  <option value="">--请选择--</option>
                  <c:forEach items="${areas}" var="area">
                    <option value="${area.name}"> ${area.name}</option>
                  </c:forEach>
                </select>
              </div>
            </div>
        </div>
        <div class="box-footer">
          <button id="btnQry" type="button" class="btn"><i class="fa fa-search" ></i>查询</button>
          <shiro:hasPermission name="appManage:appConfig:CREATE">
           <%--  <button  onclick="configApp()" type="button" class="btn" data-toggle="modal" data-target="#myModal">配置应用</button>--%>
             <a href="" onclick="return configAppHref($(this));"  id="configAppHref" type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal"><i class="fa fa-plus"></i>配置应用</a>

          </shiro:hasPermission>
        </div>
        </div>
      </form>
      <div class="box">
        <div class="box-body table-responsive">
          <table id="tblData" class="table table-bordered table-hover dataTable">
            <thead>
            <tr>
              <th>IMEI号</th>
              <th>机身号</th>
              <th>机构号</th>
              <th>商铺名称</th>
              <th><input type="checkbox" id="checkedAll"> 全选</th>
            </tr>
            </thead>
            <tbody><tr><td colspan="20" style="text-align: center"> 请选择搜索条件后点击“查询”键。</td></tr></tbody>
          </table>
          <div id="Pagination" style="float: left;"></div>

          <div id="Pagination2" class="pagination">
            <span id="terminalCount2">共0条记录，每页50条</span>
          </div>
          
        </div>
      </div>


    </section><!-- /.content -->
  </aside><!-- /.right-side -->
</div><!-- ./wrapper -->


<%@ include file="../include/footer.jsp" %>
</body>
</html>

<script type="text/javascript">
    $(document).ready(function () {
        appconfig.list.init();
    });


    function imeiCheck(o) {
        if(o[0].checked){
            o.parent().parent().attr("data-check",true);
        }else{
            o.parent().parent().attr("data-check",false);
        }
    }

    function configApp() {
        $('#myModal').modal({
            keyboard: false
        });
        var params = [];
        var tblData = $('#tblData');
        var count = 0;
        for(var i=1;i < tblData[0].rows.length;i++){
            if(tblData[0].rows[i].dataset.check=="true"){
                count++;
                params.push(tblData[0].rows[i].dataset["imeiid"]);
            }
        }
        console.info(JSON.stringify(params));
        if(count == 0){
            alert("请选择需要配置的imei。");
        }
        else {
            console.log(JSON.stringify(params));
            document.write("<form action='" + ctx+"/app/config/showApp'"+ " method='post' name='form1' style='display:none'>");
            document.write("<input type='hidden' name='imeiJsonList' value='"+JSON.stringify(params)+"'>");
            document.write("</form>");
            document.form1.submit();
        }
    }

    $("#checkedAll").click(function(){
        if($("#checkedAll")[0].checked){ //check all
            $("input[name='imeiCheck']").each(function(){
                //Jquery 版本兼容性 attr("checked") 返回undefined或失效
                //$(this).attr("checked",true);
                $(this).prop("checked",true);
                //$(this)[0].checked = true;
                $(this).parent().parent().attr("data-check",true);
            });

        }else{
            $("input[name='imeiCheck']").each(function(){
                $(this).prop("checked",false);
                //$(this)[0].checked = false;
                $(this).parent().parent().attr("data-check",false);
            });
        }
    });

    function configAppHref(o) {
        var params = [];
        var tblData = $('#tblData');
        var count = 0;
        for(var i=1;i < tblData[0].rows.length;i++){
            if(tblData[0].rows[i].dataset.check=="true"){
                count++;
                params.push(tblData[0].rows[i].dataset["imeiid"]);
            }
        }
        console.info(JSON.stringify(params));
        if(count == 0){
            alert("请选择需要配置的imei。");
            window.location.href="/app/config";
        }
        else {
            o[0].href = "/app/config/showApp?size=100&imeiJsonList="+JSON.stringify(params);
            console.log(o[0].href)
            return true;
        }
    }


</script>
