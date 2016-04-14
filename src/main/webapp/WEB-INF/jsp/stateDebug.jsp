<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" isELIgnored="false" pageEncoding="ISO-8859-1" %>

<div class="details">

  <div id="qunit"></div>
  <div id="qunit-fixture"></div>
  <div id="error-test"></div>
  <div id="error-test2"></div>

  <h2>Form Parameters</h2>
  <table class="border debugtable">
    <tr>
      <th>Attribute</th>
      <th>Value</th>
    </tr>
    <c:set var="even" value="false"/>
    <c:forEach var="name" items="${param}">
      <tr class="${even?'even':'odd'}">
        <td> ${name.key} </td>
        <td>${fn:escapeXml(name.value)}&nbsp;</td>
      </tr>
      <c:set var="even" value="${!even}"/>
    </c:forEach>
  </table>

  <h2>Request Attributes</h2>
  <table class="border debugtable">
    <tr>
      <th>Attribute</th>
      <th>Value</th>
    </tr>
    <c:forEach var="name" items="${requestScope}">
      <tr class="${even?'even':'odd'}">
        <td> ${name.key} </td>
        <td>${fn:escapeXml(name.value)}&nbsp;</td>
      </tr>
      <c:set var="even" value="${!even}"/>
    </c:forEach>
  </table>


  <h2>Session Attributes</h2>
  <table class="border debugtable">
    <tr>
      <th>Attribute</th>
      <th>Value</th>
    </tr>
    <c:forEach var="name" items="${sessionScope}">
      <tr class="${even?'even':'odd'}">
        <td> ${name.key} </td>
        <td>${fn:escapeXml(name.value)}&nbsp;</td>
      </tr>
      <c:set var="even" value="${!even}"/>
    </c:forEach>
  </table>

  <h2>HTTP Headers</h2>
  <table class="border debugtable">
    <tr>
      <th>Attribute</th>
      <th>Value</th>
    </tr>
    <c:forEach var="name" items="${header}">
      <tr class="${even?'even':'odd'}">
        <td> ${name.key} </td>
        <td>${fn:escapeXml(name.value)}&nbsp;</td>
      </tr>
      <c:set var="even" value="${!even}"/>
    </c:forEach>
  </table>
</div>

