<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="model" scope="request"
             type="com.github.trentonadams.eve.features.ApiKeys.MyModel"/>

<%--@elvariable id="towns" type="java.util.List<ca.tnt.athome.data.Town>"--%>
<%--@elvariable id="pageFields" type="java.util.Map<String,Object>"--%>
<div class="panel panel-info">
  <div class="panel-heading">
    <h2 class="panel-title">API Key Management</h2>
  </div>
  <div class="panel-body">
    <form role="form" action="<c:url value="/api-keys/post"/>" method="post"
          ng-app="apiKeys" ng-controller="MainCtrl as ctrl">
      <tags:bootstrap-field name="keyId"
                            errorMessage="Key ID is a required field">
        <input type="number" ng-model="ctrl.keyId" name="keyId" id="keyId"
               placeholder="keyId" class="form-control"/>

        <div class="required-icon">
          <div class="text">*</div>
        </div>
      </tags:bootstrap-field>

      <tags:bootstrap-field name="verificationCode"
                            errorMessage="Verification Code is a required field">
        <input type="text" ng-model="ctrl.verificationCode"
               name="verificationCode" id="verificationCode"
               placeholder="verificationCode" class="form-control"/>

        <div class="required-icon">
          <div class="text">*</div>
        </div>
      </tags:bootstrap-field>
      <div>
        keyId = {{ctrl.keyId}}
      </div>
      <div>
        verificationCode = {{ctrl.verificationCode}}
      </div>
      <input type="submit" name="submit" value="Submit"
             class="btn btn-primary"/>
    </form>
  </div>

  <script type="text/javascript">
    angular.module('apiKeys', [])
      .controller('MainCtrl', [
        function ()
        {
          this.keyId = ${model.keyId == null? 0:model.keyId};
          this.verificationCode = '${model.verificationCode}';
        }]);
  </script>
</div>
