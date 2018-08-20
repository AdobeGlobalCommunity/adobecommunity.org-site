<%@include file="/apps/adobecommunity-org/global.jsp"%>
<c:set var="page" value="${sling:adaptTo(resource,'org.apache.sling.cms.core.models.PageManager').page}" />
<title><sling:encode value="${properties['jcr:title']}" mode="HTML" /> | Adobe Global Community</title>
<meta content="${fn:join(page.keywords,',')}" name="keywords" />
<meta content="${sling:encode(properties['jcr:description'],'HTML_ATTR')}" name="description" />
<meta name="twitter:description" content="${sling:encode(properties['jcr:description'],'HTML_ATTR')}" />
<meta property="og:site_name" content="Adobe Global Community"/>
<meta property="og:type" content="blog"/>
<meta property="og:title" content="${properties['jcr:title']} | Adobe Global Community"/>
<meta name="twitter:card" content="summary" />
<meta name="twitter:title" content="${properties['jcr:title']} | Adobe Global Community" />
<c:choose>
    <c:when test="${not empty path.thumbnail}">
        <meta property="og:image" content="${page.thumbnail}"/>
        <meta name="twitter:image" content="${page.thumbnail}"/>
    </c:when>
    <c:otherwise>
        <meta property="og:image" content="/static/clientlibs/adobecommunity-org/images/logo.jpg"/>
        <meta name="twitter:image" content="/static/clientlibs/adobecommunity-org/images/logo.jpg"/>
    </c:otherwise>
</c:choose>
<meta property="og:url" content="https://adobecommunity.org${page.publishedPath}"/>
<link rel="canonical" href="https://adobecommunity.org${fn:replace(page.publishedPath,'index.html','')}" />
<meta name="twitter:url" content="https://adobecommunity.org${page.publishedPath}" />