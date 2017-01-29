<jsp:useBean id="model" scope="request"
             type="com.github.trentonadams.eve.features.auth.Authentication"/>
<div class="error">
  We apologize for the inconvenience, we were unable to authenticate you at this
  time. You may try again.  If you continue to receive this error, please
  notify us by contacting Burn Monroe in game.
</div>

<p>character: ${eveAuthenticator.character}</p>