<jsp:useBean id="model" scope="request" type="com.github.trentonadams.eve.features.auth.Authentication"/>
<p>
  Your eve session is established.
  </p>

<p>code: ${model.eveAuthenticator.code}</p>
<p>tokens: ${model.eveAuthenticator.tokens}</p>
<p>character: ${model.eveAuthenticator.character}</p>