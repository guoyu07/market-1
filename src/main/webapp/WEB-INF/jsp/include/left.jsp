<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglibs.jsp" %>
<aside class="left-side sidebar-offcanvas">
  <!-- sidebar: style can be found in sidebar.less -->
  <section class="sidebar">
    <!-- Sidebar user panel -->
    <div class="user-panel">
      <div class="pull-left image">

      </div>
      <div class="pull-left info">
        <c:choose>
          <c:when test="${userInfo.type eq 3}">
            <p>您好, ${agentMerchantInfo.merchantName}</p>
          </c:when>
          <c:otherwise>
            <p>您好, ${userInfo.account}</p>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <!-- sidebar menu: : style can be found in sidebar.less -->
      <%--enName会传递给MenuTags类的enName属性--%>
    <ucfPay:menuTags enName="${param.enName}" />
  </section>
  <!-- /.sidebar -->
</aside>
