<%@ attribute name="message" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" %>
<%@ attribute name="errorMessage" %>
<c:if test="${pageFields[name] != null}">
  <p>${errorMessage}</p>
</c:if>