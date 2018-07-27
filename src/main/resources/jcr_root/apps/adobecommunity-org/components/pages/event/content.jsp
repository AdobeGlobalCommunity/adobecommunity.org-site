<%@include file="/libs/sling-cms/global.jsp"%>
<div class="container">
	<div class="row">
		<div class="col-md-12 padded-content">
			<sling:call script="breadcrumb.jsp" />
			<div class="row event">
			    <div class="col-sm-8">
			        <c:if test="${not empty properties['sling:thumbnail']}">
			            <img class="img-fluid img-thumbnail my-4 mx-auto d-block" src="${properties['sling:thumbnail']}" alt="${sling:encode(properties['jcr:title'],'HTML_ATTR')}" />
			        </c:if>
			        <h1>${sling:encode(properties['jcr:title'],'HTML')}</h1>
			        <c:if test="${not empty properties['presenter']}">
			            <strong>Presenter:</strong> ${sling:encode(properties.presenter,'HTML')}
			        </c:if>
			        <sling:include path="container" resourceType="sling-cms/components/general/container" />
			        
			        <div class="share background--grey py-2 pl-1">
			        	<sling:include path="share" resourceType="adobecommunity-org/components/general/share" />
			        </div>
			        <sling:call script="tags.jsp" />
			        <sling:call script="comments.jsp" />
			    </div>
			    <div class="col-sm-4">
			        <div class="event--time my-4">
			            <h4>When</h4>
			            <fmt:parseDate value="${properties['eventdate']}" var="eventDate" pattern="yyyy-MM-dd" />
			            <fmt:formatDate value="${eventDate}" pattern="MMM d, yyyy" /> at ${sling:encode(properties.eventtime,'HTML')}
			        </div>
			        <div class="event--location my-4">
			            <h4>Where</h4>
			            <c:choose>
			            	<c:when test="${not empty properties.locationlink}">
				                <a href="${properties.locationlink}" rel="nofollow noopen" target="_blank">
				                    ${sling:encode(properties.location,'HTML')}
				                </a>
			            	</c:when>
			            	<c:otherwise>
			            		${sling:encode(properties.location,'HTML')}
			            	</c:otherwise>
			            </c:choose>
			        </div>
			        <div class="event--type my-4">
			            <h4>Event Type</h4>
			            ${sling:encode(properties.eventtype,'HTML')}
			        </div>
			        <div class="event--cta my-4">
			        	<sling:include path="cta" resourceType="adobecommunity-org/components/general/cta" />
			        </div>
			    </div>
			</div>
		</div>
	</div>
</div>