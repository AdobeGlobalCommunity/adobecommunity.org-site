<%@include file="/libs/sling-cms/global.jsp"%>
<div class="container main">
    <div class="padded-content h-100">
		<div class="row">
		    <div class="col-md-8">
		        <sling:include path="container" resourceType="sling-cms/components/general/container" />
		    </div>
		    <div class="col-md-4">
		        <div class="alert alert-dark">
		            <sling:include path="rightcol" resourceType="sling-cms/components/general/container" />
		        </div>
		    </div>
		</div>
	</div>
</div>