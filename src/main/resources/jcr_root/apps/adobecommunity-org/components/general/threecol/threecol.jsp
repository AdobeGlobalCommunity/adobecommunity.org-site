<%@include file="/apps/adobecommunity-org/global.jsp"%>
<div class="row text-center justify-content-center">
	<div class="col-md-4 px-0 my-2">
		<div class="p-4 mr-sm-0 mr-md-2 background--white h-100">
			<img src="${properties.image1}" class="mx-auto my-4 d-block img-fluid" alt="${sling:encode(properties.header1,'HTML_ATTR')}">
			<h4>
				<sling:encode value="${properties.header1}" mode="HTML" />
			</h4>
			<sling:include path="container1" resourceType="sling-cms/components/general/container" />
		</div>
	</div>
	<c:if test="${not empty properties.header2}">
		<div class="col-md-4 px-0 my-2">
			<div class="p-4 mr-sm-0 mr-md-2 background--white h-100">
				<img src="${properties.image2}" class="mx-auto my-4 d-block img-fluid" alt="${sling:encode(properties.header2,'HTML_ATTR')}">
				<h4>
					<sling:encode value="${properties.header2}" mode="HTML" />
				</h4>
				<sling:include path="container2" resourceType="sling-cms/components/general/container" />
			</div>
		</div>
	</c:if>
	<c:if test="${not empty properties.header3}">
		<div class="col-md-4 px-0 my-2">
			<div class="p-4 background--white h-100">
				<img src="${properties.image3}" class="mx-auto my-4 d-block img-fluid" alt="${sling:encode(properties.header3,'HTML_ATTR')}">
				<h4>
					<sling:encode value="${properties.header3}" mode="HTML" />
				</h4>
				<sling:include path="container3" resourceType="sling-cms/components/general/container" />
			</div>
		</div>
	</c:if>
</div>