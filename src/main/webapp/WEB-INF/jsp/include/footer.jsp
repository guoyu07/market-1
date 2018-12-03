<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglibs.jsp" %>
<script type="text/javascript">
  $(document).ready(function () {
    var _titleKey = " - "+$(".content-header h1").text();
    $("title").text($("title").text()+_titleKey);
    ucf.common.urlAddRandom();
  })
</script>
<footer class="footer">
  <hr>
  <p class="pull-right">&copy; 2017应用市场管理系统
  </p>
</footer>