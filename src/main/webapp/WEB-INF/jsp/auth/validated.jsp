<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="eveAuthenticator" type="com.github.trentonadams.eve.features.auth.EveAuthenticator"--%>

<c:set var="character" value="${eveAuthenticator.OAuthCharacter}"/>
<p>
<c:choose>
  <c:when test="${model.authStatus == 'ESTABLISHED'}">
    <div>Your eve session is established.
      Welcome ${character.characterName}
    </div>

    <div>
      <img title="${character.characterName}"
           src="https://imageserver.eveonline.com/Character/${character.characterID}_256.jpg"/>
    </div>
  </c:when>
  <c:otherwise>
    You are not authenticated with Eve, you'll need to login.
  </c:otherwise>
</c:choose>
</p>

<p>character: ${eveAuthenticator.OAuthCharacter}</p>
<p>tokens: ${eveAuthenticator.tokens}</p>