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
  <script type="text/javascript" src="${ctx}/resources/js/busi/terminal/terminalSet.js?v=${deployVersion}"></script>


</head>
<body class="${webSkin}">

<%@ include file="../include/navbar.jsp" %>
<div class="wrapper row-offcanvas row-offcanvas-left">
  <!-- Left side column. contains the logo and sidebar -->
  <jsp:include page="../include/left.jsp">
    <jsp:param name="enName" value="terminalSet" />
  </jsp:include>

  <!-- Right side column. Contains the navbar and content of the page -->
  <aside class="right-side">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>厂商应用</h1>
      <ucfPay:ContentHeaderTag enName="terminalSet" />
    </section>

    <!-- Main content -->
    <section class="content">
      <form id="frmQry" name="frmQry" role="form" action="" METHOD="post" class="form-horizontal">
        <input type="hidden" name="page" value="0"/>
        <input type="hidden" name="size" value="20"/>
        <input type="hidden" name="sort" value="create_time,desc"/>

        <div class="box-body">
          <div class="row form-group">
            <label class="col-sm-2 control-label">IMEI号：</label>
            <div class="col-xs-3"><input type="text" name="nameLike" class="form-control" /></div>

            <label class="col-sm-2 control-label">机身号：</label>
            <div class="col-xs-3"><input type="text"  id="snInput" name="sn" class="form-control" /></div>
          </div>
        </div>

        <div class="box box-primary">
            <div class="box-footer">
              <button id="btnQry" type="button" class="btn"><i class="fa fa-search"></i>查询</button>
              <shiro:hasPermission name="terminalManage:terminalSet:EXPORT">
                <button id="btnExport" type="button" class="btn">导出数据</button>
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
              <th> <input type="checkbox" id="checkedAll"> 全选</th>
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

<script type="text/javascript">
    $(document).ready(function () {
        terminalSet.list.init();
    });

    $("#checkedAll").click(function(){
        if($("#checkedAll")[0].checked){ //check all
            $("input[name='snCheck']").each(function(){
                //Jquery 版本兼容性 attr("checked") 返回undefined或失效
                //$(this).attr("checked",true);
                $(this).prop("checked",true);
                //$(this)[0].checked = true;
                $(this).parent().parent().attr("data-check",true);
            });

        }else{
            $("input[name='snCheck']").each(function(){
                $(this).prop("checked",false);
                //$(this)[0].checked = false;
                $(this).parent().parent().attr("data-check",false);
            });
        }
    });

    function snCheck(o) {
        if(o[0].checked){
            o.parent().parent().attr("data-check",true);
        }else{
            o.parent().parent().attr("data-check",false);
        }
    }

    $("#btnExport").on("click", function () {
        var params = [];
        var tblData = $('#tblData');
        var count = 0;
        for(var i=1;i < tblData[0].rows.length;i++){
            if(tblData[0].rows[i].dataset.check=="true"){
                count++;
                params.push(tblData[0].rows[i].dataset["snid"]);
            }
        }
        console.info(JSON.stringify(params));
        var snList = "";
        for(var i = 0;i < params.length;i++){
            snList = snList + params[i] + ",";
        }

        if(count == 0){
            alert("请选择需要导出密匙的终端。");
        }
        else {
            ucf.common.download(ctx + "/terminal/set/export", "snList="+snList,"post");
        }

    });

</script>