<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page buffer="500kb" %>
<%--
  Created by IntelliJ IDEA.
  User: trenta
  Date: 19/02/16
  Time: 4:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page errorPage="/WEB-INF/jsp/error.jsp" %>--%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Eve Manufacturing Manager</title>

  <!-- Bootstrap core CSS -->
  <!--<link href="$.mobile-1.4.2.css" rel="stylesheet">-->
  <link href="<c:url value="/public/lib/bootstrap/dist/css/bootstrap.css"/>"
        rel="stylesheet">
  <link href="<c:url value="/public/custom/bootstrap-custom/css/custom.css"/>"
        rel="stylesheet">
  <link
    href="<c:url value="/public/custom/bootstrap-custom/css/required-fields.css"/>"
    rel="stylesheet">
  <link
    href="<c:url value="/public/custom/bootstrap-custom/css/offcanvas.css"/>"
    rel="stylesheet">
  <link rel="stylesheet"
        href="<c:url value="/public/custom/jquery-custom/jquery-ui.min.css"/>">

  <!-- Custom styles for this template -->
  <!--<link href="navbar-fixed-top.css" rel="stylesheet">-->
  <link href="<c:url value="/public/custom/bootstrap-custom/css/theme.css"/>"
        rel="stylesheet">

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script
    src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>
<body>
<script src="<c:url value="/public/lib/jquery/dist/jquery.min.js"/>"></script>
<script
  src="<c:url value="/public/custom/jquery-custom/jquery-ui.min.js"/>"></script>
<script
  src="<c:url value="/public/lib/bootstrap/dist/js/bootstrap.js"/>"></script>
<script
  src="<c:url value="/public/custom/bootstrap-custom/js/offcanvas.js"/>"></script>

<!-- Fixed navbar -->
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse"
              data-target=".navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand internal" href="<c:url value="/"/>">At Home</a>
    </div>
    <div class="navbar-collapse collapse">
      <ul class="nav navbar-nav">
        <li>
          <a class="internal" href="<c:url value="/about.html"/>">About</a>
        </li>
        <li>
          <a class="internal" href="<c:url value="/contact.html"/>">
            Contact Us
          </a>
        </li>
        <%--        <li>
                  <a class="internal" href="<c:url value="/bootstrap.html"/>">
                    Examples
                  </a>
                </li>
                <li>
                  <a class="internal" href="spec.htm">Spec</a>
                </li>--%>
      </ul>
      <%--@elvariable id="eveAuthenticator" type="com.github.trentonadams.eve.api.auth.EveAuthenticatorImpl"--%>
      <c:if test="${eveAuthenticator.authValid()}">
        <div class="btn-group pull-right">
          <button type="button" class="btn btn-primary dropdown-toggle"
                  data-toggle="dropdown" aria-haspopup="true"
                  aria-expanded="false">
            <c:set var="character" value="${eveAuthenticator.OAuthCharacter}"/>
            <img title="${character.characterName}"
                 src="https://imageserver.eveonline.com/Character/${character.characterID}_32.jpg"/>
            Characters <span class="caret"></span>
          </button>
          <ul class="dropdown-menu">
            <li>
              <a href="#"><img title="Katherine Monroe"
                               src="https://imageserver.eveonline.com/Character/95366233_32.jpg"/>
                Katherine Monroe
              </a>
            </li>
            <li>
              <a href="#">Add another character</a>
            </li>
            <li>
              <a href="#">Something else here</a>
            </li>
            <li role="separator" class="divider"></li>
            <li>
              <a href="#">Separated link</a>
            </li>
          </ul>
        </div>
      </c:if>
    </div>
    <!--/.nav-collapse -->
  </div>
</div>

<div class="container-fluid " id="container">
  <div class="row row-offcanvas row-offcanvas-right">

    <p class="pull-right visible-xs">
      <button type="button" class="btn btn-primary btn-xs"
              data-toggle="offcanvas" id="at-home-links">At Home Links >>
      </button>
    </p>

    <div class="col-xs-12 col-sm-9" id="main-content">
      <c:choose>
        <c:when
          test="${model.page == '/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/api-keys.jsp'}">
          <jsp:include
            page="/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/api-keys.jsp"/>
        </c:when>
        <c:when
          test="${model.page == '/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/sample.jsp'}">
          <jsp:include
            page="/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/sample.jsp"/>
        </c:when>
        <c:when test="${model.page == '/WEB-INF/jsp/default-view.jsp'}">
          <jsp:include page="/WEB-INF/jsp/default-view.jsp"/>
          <jsp:include page="pageinfo.jsp"/>
        </c:when>
        <c:when test="${model.page == '/WEB-INF/jsp/auth/validated.jsp'}">
          <jsp:include page="/WEB-INF/jsp/auth/validated.jsp"/>
        </c:when>
        <c:when test="${model.page == '/WEB-INF/jsp/auth/failure.jsp'}">
          <jsp:include page="/WEB-INF/jsp/auth/failure.jsp"/>
        </c:when>
        <c:when test="${model.page == '/WEB-INF/jsp/error.jsp'}">
          <jsp:include page="/WEB-INF/jsp/error.jsp"/>
        </c:when>
        <c:otherwise>
          No page matched for "${model.page}" or model.page is empty.
        </c:otherwise>
      </c:choose>
    </div>
    <!--/span-->
    <jsp:include page="/WEB-INF/jsp/navigation.jsp"/>
    <!--/span-->
  </div>
  <!--/row-->

  <jsp:include page="stateDebug.jsp"/>
  <hr>

  <footer>
    <p>
      <a href="http://www.climbingvine.ca">&copy; Climbing Vine Technology
        Corp. 2014
      </a>
    </p>
  </footer>

</div>
<!-- /container -->


<!-- Bootstrap core JavaScript
================================================== -->
<%--<%@ include file="tmp.jsp" %>--%>


<script type="text/javascript">
  (function ($)
  {
    $(document).ready(function ()
    {
      $('#available').datepicker({
        dateFormat: 'yy-M-dd'
      });

      $(function ()
      {
        $('.required-icon').tooltip({
          placement: 'left',
          title: 'Required field'
        });

        // auto select the first invalid element
        $('.alert input,.alert select, .alert textarea').first().focus();

        var mobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
          navigator.userAgent);
        if (mobile)
        {
//                    $("head").append('<link href="$.mobile-1.4.2.css" rel="stylesheet">');
          $("body").append('<script src="jquery.mobile-1.4.2.js"><\/script>');
          $(document).bind("mobileinit", function ()
          {
            $.mobile.ajaxEnabled = false;
          });
          jQuery(window).unbind('swipe.nav').bind('swipe.nav', function (event)
          {
            event.preventDefault();
            $('#at-home-links').click();
            alert('swipe left');
          });
          jQuery(window).unbind('swipe.nav').bind('swipe.nav', function (event)
          {
            event.preventDefault();
            $('#at-home-links').click();
            alert('swipe right');
          });
        }

        $('a[href="' + window.location.pathname + '"]').addClass('active');
        // remove label
        // rename title to placeholder
        // match on "required" in placeholder and then append required-icon div to main div

        /*        var oldElements = $('.required-field-block,.optional,.required').has('input[title],textarea[title]');
         oldElements.each(function (index, element)
         {
         $(this).find('label').remove();
         $(this).attr('class', 'required-field-block');
         var input = $(this).find('input,textarea');
         input.attr('placeholder', input.attr('title'));
         input.removeAttr('title');
         if (input.attr('placeholder').match(new RegExp('required')))
         {
         $(this).append('    <div class="required-icon">' +
         '<div class="text">*</div>' +
         '</div>')
         }
         input.attr('class', 'form-control');
         });*/

        var requiredInputElements = $('.required-field-block').has(
          '.required-icon').find('*[placeholder]');
        requiredInputElements.each(function (index, element)
        {
          $(this).attr('placeholder',
            $(this).attr('placeholder') + ' - is a required field');

          // let's be aria screen reader friendly.
          $(this).attr('aria-label', $(this).attr('placeholder'));
        });
      });
    });
  }(jQuery));
</script>

</body>
</html>

