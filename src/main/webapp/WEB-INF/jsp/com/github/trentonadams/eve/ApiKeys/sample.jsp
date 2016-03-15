<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="towns" type="java.util.List<ca.tnt.athome.data.Town>"--%>
<%--@elvariable id="pageFields" type="java.util.Map<String,Object>"--%>
<div class="panel panel-info">
  <div class="panel-heading">
    <h2 class="panel-title">List Rental</h2>
  </div>
  <div class="panel-body">
    <div>
      <div>
        <p>This page is for entering information for rental
          units such as houses, apartments, etc. If you wish
          to modify one of the existing entries, please
          click the "Contact Us" link, and let us know what
          you want changed.
        </p>

        <p>This is a free service but remember if you list an
          available rental, the website administrator must
          approve the information before it will show on the
          list (usually within 1-2 business days) and the listing will
          automatically delete after two weeks.
        </p>

        <p>Remember, <span style="color: #ff0000; "><strong>never
          give personal information</strong></span> about you,
          your bank accounts or other financial information to
          anyone, especially over the internet unless you are
          sure that it is safe.
        </p>
      </div>
    </div>
    <form role="form" method="post">
      <c:if test="${id != null}">
        <%-- listing id passed in, it's a request to modify--%>
        <input type="hidden" name="id" value="${id}"/>
      </c:if>
      <tags:bootstrap-field name="town" errorMessage="Town is a required field">
        <select name="town" id="town" class="form-control"
                aria-label="Select the Town">
          <option value="0">--Town--</option>
          <c:forEach items="${towns}" var="townVar">
            <option
              value="${townVar.id}" ${town == townVar.id?'selected':''}>${townVar.town}</option>
          </c:forEach>
        </select>

        <div class="required-icon">
          <div class="text">*</div>
        </div>
      </tags:bootstrap-field>

      <tags:bootstrap-field name="description"
                            errorMessage="Description is a required field">
        <textarea name="description"
                  class="form-control"
                  placeholder="Description e.g. house, apartment, number of bedrooms, baths, garage, etc."
                  id="description">${description}</textarea>
      </tags:bootstrap-field>

      <tags:bootstrap-field name="available"
                            errorMessage="Date Available is a required field">
        <%--jQuery UI date picker initalized at bottom of sample.jsp --%>
        <input type="text" name="available" id="available"
               placeholder="Date Available (e.g. 2015-May-01) - Click field to select date"
               class="form-control" value="${available}"/>

        <div class="required-icon">
          <div class="text">*</div>
        </div>
      </tags:bootstrap-field>

      <tags:bootstrap-field name="amount"
                            errorMessage="Amount is a required field">
        <input type="number" name="amount" id="amount" value="${amount}"
               placeholder="Amount" class="form-control"/>

        <div class="required-icon">
          <div class="text">*</div>
        </div>
      </tags:bootstrap-field>

      <tags:bootstrap-field name="contact"
                            errorMessage="Contact is a required field">
        <input type="text" name="contact" id="contact" value="${contact}"
               size="30" class="form-control"
               placeholder="Contact (Name, telephone, etc)"/>

        <div class="required-icon">
          <div class="text">*</div>
        </div>
      </tags:bootstrap-field>

      <tags:bootstrap-field name="email"
                            errorMessage="Email is a required field">
        <input type="email" name="email" id="email" value="${email}"
               size="30" class="form-control"
               placeholder="e-mail e.g. me@example.com"/>

        <div class="required-icon">
          <div class="text">*</div>
        </div>
      </tags:bootstrap-field>

      <tags:bootstrap-field name="address"
                            errorMessage="Address is a required field">
        <textarea name="address" id="address" placeholder="Address"
                  class="form-control">${address}</textarea>

        <div class="required-icon">
          <div class="text">*</div>
        </div>
      </tags:bootstrap-field>
      <input type="submit" name="submit" value="Submit"
             class="btn btn-primary"/>
    </form>
  </div>
</div>
