<%--core核心标签库--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--fmt标签库是用来格式化输出--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--EL函数库--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--shiro权限控制--%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%--自定义标签--%>
<%@ taglib uri="/WEB-INF/ucfWebTags.tld" prefix="ucfPay" %>
<%--内置对象，代表页面上下文，访问jsp之间的共享数据--%>
<%--pageContext：pageContext是PageContext类型！可以使用pageContext对象调用getXXX()方法，例如pageContext.getRequest()，可以${pageContext.request}。也就是读取JavaBean属性--%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
