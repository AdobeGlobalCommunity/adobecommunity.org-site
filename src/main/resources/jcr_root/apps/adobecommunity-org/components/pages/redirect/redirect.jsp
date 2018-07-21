<%@include file="/apps/adobecommunity-org/global.jsp"%><c:choose><c:when test="${cmsEditEnabled != 'true'}"><c:redirect url="${properties.target}" /></c:when>
	<c:otherwise>
		<!DOCTYPE html>
		<html lang="en">
		    <head>
		    	<title>${properties['jcr:title']}</title>
		    </head>
			<body>
				<p>This page will redirect to ${properties.target}
			</body>
		</html>
	</c:otherwise>
</c:choose>