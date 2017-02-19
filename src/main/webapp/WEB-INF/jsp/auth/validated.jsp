<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="authAggregator" type="com.github.trentonadams.eve.api.auth.AuthAggregator"--%>

<c:set var="character" value="${authAggregator.OAuthCharacter}"/>
<div>
<c:choose>
  <c:when test="${model.authStatus == 'ESTABLISHED'}">
    <div>Your eve session is established.
      Welcome ${character.characterName}
    </div>

    <div>
      <img title="${character.characterName}"
           src="https://imageserver.eveonline.com/Character/${character.characterID}_256.jpg"/>
    </div>
    <div>
      ${character.}
    </div>
  </c:when>
  <c:otherwise>
    You are not authenticated with Eve, you'll need to login.
  </c:otherwise>
</c:choose>
</div>

<p>character: ${authAggregator.OAuthCharacter}</p>
<p>tokens: ${authAggregator.tokens}</p>