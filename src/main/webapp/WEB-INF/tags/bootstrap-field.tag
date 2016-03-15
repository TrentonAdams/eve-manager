<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="name" required="true" %>
<%@attribute name="errorMessage" required="true" %>
<div class="required-field-block ${pageFields[name] != null ? 'alert alert-danger':''}">
  <tags:field-error>
    <jsp:attribute name="name">${name}</jsp:attribute>
    <jsp:attribute name="errorMessage">${errorMessage}</jsp:attribute>
  </tags:field-error>
  <jsp:doBody/>
  <div class="required-icon">
    <div class="text">*</div>
  </div>
</div>