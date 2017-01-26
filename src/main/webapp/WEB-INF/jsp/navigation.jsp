<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-xs-6 col-sm-3 sidebar-offcanvas"
     role="navigation">
  <div class="list-group">
    <span class="list-group-item header">Account Management</span>
    <a href="<c:url value="/auth"/>"
       class="list-group-item internal"
       data-toggle="offcanvas">
      Login
    </a>
    <a href="<c:url value="/"/>"
       class="list-group-item internal" data-toggle="offcanvas">
      Home
    </a>
    <a href="<c:url value="/api-keys"/>"
       class="list-group-item internal"
       data-toggle="offcanvas">
      Manage API Keys
    </a>
    <a class="list-group-item internal"
       href="<c:url value="/api-keys/sample"/>">Sample</a>
  </div>
</div>