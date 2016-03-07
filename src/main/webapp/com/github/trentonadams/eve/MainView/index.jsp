<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: trenta
  Date: 19/02/16
  Time: 4:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Jersey Examples</title>
</head>
<body>

<c:choose>
  <c:when test="${model.page == '/test.jsp'}">
    <jsp:include page="/test.jsp"/>
  </c:when>
  <c:when test="${model.page == '/testpath.jsp'}">
    <jsp:include page="/testpath.jsp"/>
  </c:when>
  <c:otherwise>
    <jsp:include page="/default-view.jsp"/>
  </c:otherwise>
</c:choose>

</body>
</html>
