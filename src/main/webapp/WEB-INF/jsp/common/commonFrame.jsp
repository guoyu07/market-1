<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglibs.jsp" %>
<c:if test="${param.needPage eq 'Y'}">
    <script type="text/javascript" src="${ctx}/resources/js/pagination/jquery.pagination.js"></script>
    <link href="${ctx}/resources/js/pagination/pagination.css" rel="stylesheet" type="text/css" />
    <style>
        #Pagination {
            min-height: 71px;
        }
    </style>
</c:if>
<c:if test="${param.needDate eq 'Y'}">
    <script type="text/javascript" src="${ctx}/resources/lib/My97DatePicker/WdatePicker.js"></script>
</c:if>
<c:if test="${param.needValidate eq 'Y'}">
    <%--<script type="text/javascript" src="${ctx}/resources/js/validate/validate.js"></script>--%>
    <link href="${ctx}/resources/js/validate/validate.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctx}/resources/js/validate/idCheck.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/validate/additional-methods.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/validate/custom-methods.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/validate/messages_zh.js"></script>
</c:if>
<c:if test="${param.listTable eq 'Y'}">
    <script type="text/javascript" src="${ctx}/resources/lib/bootstrap-table-master/bootstrap-table.min.js"></script>
    <%--<script type="text/javascript" src="${ctx}/resources/lib/bootstrap-table-master/locale/bootstrap-table-zh-CN.js"></script>--%>
    <link href="${ctx}/resources/lib/bootstrap-table-master/bootstrap-table.css" rel="stylesheet" type="text/css" />
</c:if>


