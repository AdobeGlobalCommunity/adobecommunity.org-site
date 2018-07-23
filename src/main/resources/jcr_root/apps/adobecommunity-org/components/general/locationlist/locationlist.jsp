<%@include file="/apps/adobecommunity-org/global.jsp"%>
<h3 class="my-4">
	<sling:encode value="${properties.header}" mode="HTML" />
</h3>
<form class="group--location-search">
    <div class="form-row align-items-center">
        <div class="col-auto">
            <label for="address" class="sr-only">Address</label>
            <input type="text" class="form-control" name="address" placeholder="Enter location..." />
        </div>
        <div class="col-auto">
            <button type="submit" class="btn btn-primary">Find Groups</button>
        </div>
    </div>
</form>
<br/><hr/><br/>
<div class="group--container">
	<sling:getResource path="${properties.path}" var="groupContainer" />
	<c:forEach var="group" items="${sling:listChildren(groupContainer)}">
		<c:if test="${group.name != 'jcr:content'}">
			<c:set var="groupProps" value="${sling:getRelativeResource(group,'jcr:content').valueMap}" />
		    <div class="py-3 group--item" data-lat="${groupProps.lat}" data-lng="${groupProps.lng}">
			    <h4>
			    	<a href="${group.path}.html">
			    		<sling:encode value="${groupProps['jcr:title']}"  mode="HTML" />
			    	</a>
			    </h4>
			    <div class="group--distance-container d-none">
			        <span class="fa fa-road"></span> <span class="group--distance"></span>KM
			    </div>
			    <div>
			        <span class="fa fa-map-marker"></span> <sling:encode value="${groupProps.location}"  mode="HTML" />
			    </div>
			    <p>
			        <sling:encode value="${groupProps['jcr:description']}"  mode="HTML" />
			    </p>
			    <div>
			        <a href="${group.path}.html" class="btn btn-sm btn-secondary">
			            More Info <span class="fa fa-caret-right"></span>
			        </a>
			    </div>
			</div>
		</c:if>
	</c:forEach>
</div>