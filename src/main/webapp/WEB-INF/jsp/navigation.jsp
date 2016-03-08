<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-xs-6 col-sm-3 sidebar-offcanvas"
     role="navigation">
  <div class="list-group">
    <a href="<c:url value="/"/>"
       class="list-group-item internal" data-toggle="offcanvas">
      Home
    </a>
    <a href="<c:url value="/search.html"/>"
       class="list-group-item internal"
       data-toggle="offcanvas">
      Search
    </a>
    <c:choose>
      <c:when test="<%=request.isUserInRole((String)session.getAttribute(\"SECURITY_modifyRole\"))%>">
        <a class="list-group-item internal"
           href="<c:url value="/addbusiness_private.html"/>">Add Business</a>
      </c:when>
      <c:otherwise>
        <a class="list-group-item internal"
           href="<c:url value="/addbusiness.html"/>">Add Business</a>
      </c:otherwise>
    </c:choose>
  </div>
  <div class="list-group">
    <span class="list-group-item header">Landlord & Tenant Listings</span>
    <a href="<c:url value="/rentals.html"/>"
       class="list-group-item internal" data-toggle="offcanvas">
      Rentals
    </a>
    <a href="<c:url value="/tenants.html"/>"
       class="list-group-item internal"
       data-toggle="offcanvas">
      Prospective Tenants
    </a>
    <a href="<c:url value="/add-rental.html"/>"
       class="list-group-item internal"
       data-toggle="offcanvas">
      Add Rental
    </a>
    <a href="<c:url value="/add-tenant.html"/>"
       class="list-group-item internal"
       data-toggle="offcanvas">
      Add Tenant
    </a>
<%--    <a href="#" class="list-group-item"
       data-toggle="offcanvas">
      Apartment Managers
    </a>--%>
  </div>
  <div class="list-group">
    <span class="list-group-item header">Resources</span>
    <a href="<c:url value="/community-resources.html"/>"
       class="list-group-item internal"
       data-toggle="offcanvas">Community Resources
    </a>
    <a href="<c:url value="/contracts-forms.html"/>"
       class="list-group-item internal"
       data-toggle="offcanvas">Contracts and Forms
    </a>
    <a href="<c:url value="/rental-resources.html"/>"
       class="list-group-item internal">
      Landlords &amp; Tenants
    </a>
  </div>
  <div class="list-group">
    <span class="list-group-item header">Our Community</span>
    <a href="http://www.athabasca.ca/"
       class="list-group-item" data-toggle="offcanvas">
      Town of Athabasca
    </a>
    <a href="http://www.athabascacounty.com/"
       class="list-group-item"
       data-toggle="offcanvas">County of Athabasca
    </a>
    <a href="http://www.boylealberta.com/" class="list-group-item"
       data-toggle="offcanvas">Town of Boyle
    </a>
  </div>
</div>