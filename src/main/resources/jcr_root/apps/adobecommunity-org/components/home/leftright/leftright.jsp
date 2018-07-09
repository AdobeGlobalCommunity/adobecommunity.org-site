<%@include file="/apps/adobecommunity-org/global.jsp"%>
<div class="background--pad mr-sm-0 mr-md-2 my-4 background--white">
	<div class="row">
		<div class="col-md-6 background--img" style="background-image: url(${fn:replace(properties.backgroundimage,'/content/agc/adobecommunity-org','')})"></div>
		<div class="col-md-6 p-3">
			<h4 class="communityupdate__header">
				<sling:encode value="${properties.header}" mode="HTML" />
			</h4>
			${properties.text}
			<sling:include path="cta" resourceType="adobecommunity-org/components/general/cta" />
		</div>
	</div>
</div>