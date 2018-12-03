<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="${ctx}/resources/lib/pnotify/1.2.2/jquery.pnotify.default.css" rel="stylesheet">
<link href="${ctx}/resources/lib/pnotify/1.2.2/jquery.pnotify.default.icons.css" rel="stylesheet">
<script type="text/javascript" src="${ctx}/resources/lib/pnotify/1.2.2/jquery.pnotify.js"></script>

<div class="modal fade" id="myModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content"></div>
    </div>
</div>

<script type="text/javascript">
    <!--
    $(function(){modal();});
    /* 模态窗口JS */
    function modal() {
        $("#myModal").on("hidden.bs.modal", function(e) {
            $(this).removeData("bs.modal").find(".modal-content").empty();
        });
    }
    //-->
</script>
