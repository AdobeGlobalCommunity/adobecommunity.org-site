<%@include file="/apps/adobecommunity-org/global.jsp"%>
<c:choose>
	<c:when test="${not empty slingRequest.requestPathInfo.suffixResource.valueMap['jcr:title']}">
		<c:set var="title" value="${slingRequest.requestPathInfo.suffixResource.valueMap['jcr:title']} | ${properties['jcr:title']}" />
	</c:when>
	<c:otherwise>
		<c:set var="title" value="${properties['jcr:title']}" />
	</c:otherwise>
</c:choose>
<c:set var="page" value="${sling:adaptTo(resource,'org.apache.sling.cms.PageManager').page}" />
<title><sling:encode value="${title}" mode="HTML" /> | Adobe Global Community</title>
<meta content="${fn:join(page.keywords,',')}" name="keywords" />
<meta content="Pages on the Adobe Global Community tagged with '${sling:encode(slingRequest.requestPathInfo.suffixResource.valueMap['jcr:title'],'HTML_ATTR')}'" name="description" />
<meta name="twitter:description" content="Pages on the Adobe Global Community tagged with '${sling:encode(slingRequest.requestPathInfo.suffixResource.valueMap['jcr:title'],'HTML_ATTR')}'" />
<meta property="og:site_name" content="Adobe Global Community"/>
<meta property="og:type" content="blog"/>
<meta property="og:title" content="${sling:encode(title,'HTML_ATTR')} | Adobe Global Community"/>
<meta name="twitter:card" content="summary" />
<meta name="twitter:title" content="${sling:encode(title,'HTML_ATTR')} | Adobe Global Community" />
<c:choose>
    <c:when test="${not empty path.thumbnail}">
        <meta property="og:image" content="${page.thumbnail}"/>
        <meta name="twitter:image" content="${page.thumbnail}"/>
    </c:when>
    <c:otherwise>
        <meta property="og:image" content="/static/clientlibs/adobecommunity-org/images/logo.png"/>
        <meta name="twitter:image" content="/static/clientlibs/adobecommunity-org/images/logo.png"/>
    </c:otherwise>
</c:choose>
<meta property="og:url" content="https://adobecommunity.org${page.publishedPath}${slingRequest.requestPathInfo.suffix}"/>
<link rel="canonical" href="https://adobecommunity.org${page.publishedPath}${slingRequest.requestPathInfo.suffix}" />
<meta name="twitter:url" content="https://adobecommunity.org${page.publishedPath}${slingRequest.requestPathInfo.suffix}" />