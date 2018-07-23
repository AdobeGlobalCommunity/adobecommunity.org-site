<%@include file="/libs/sling-cms/global.jsp"%>
<div class="my-2">
	<label class="d-block" for="${resource.name}">
		<sling:encode value="${properties.title}" mode="HTML" />
	</label>
	<audio controls="controls" id="${resource.name}">
		<source src="${properties.media}" type="audio/mpeg" />
	</audio>
</div>