<%@include file="/libs/sling-cms/global.jsp"%>
<div class="container main">
	<sling:call script="breadcrumb.jsp" />
    <div class="padded-content h-100">
		<div class="row">
		    <div class="col-md-12">
		        <sling:include path="container" resourceType="sling-cms/components/general/container" />
		        <c:if test="${fn:startsWith(pageContext.request.requestURI,'/content/agc/adobecommunity-org/members')}">
		        	<div class="row">
		        		<div class="col-md-6 offset-md-3">
				        	<h2>Login</h2>
				        	<p>Login to see member content or <a href="/content/agc/adobecommunity-org/join.html">Join AGC</a> if you are not already a member.</p>
				        	<sling:include path="login" resourceType="adobecommunity-org/components/forms/login" />
		        		</div>
		        	</div>
		        	
		        </c:if>
		    </div>
		</div>
	</div>
</div>