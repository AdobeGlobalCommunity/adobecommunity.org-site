<%@include file="/libs/sling-cms/global.jsp"%>
<c:set var="domain" value="${fn:split(fn:replace(fn:replace(properties.original,'https://',''),'http://',''),'/')[0]}" />
<div class="container">
	<sling:call script="breadcrumb.jsp" />
	<div class="row background__white">
		<div class="col-md-12">
			<article class="post" typeof="BlogPosting">
				<header class="row">
					<div class="col-md-12">
						<c:choose>
							<c:when test="${not empty properties['sling:thumbnail']}">
								<c:set var="image" value="${fn:replace(properties['sling:thumbnail'],'/content/agc/adobecommunity-org','')}" />
							</c:when>
							<c:otherwise>
								<c:set var="image" value="//placehold.it/300x300?text=${sling:encode(properties['jcr:title'],'HTML_ATTR')}" />
							</c:otherwise>
						</c:choose>
						<header class="background--img" style="background-image: url(${image})">
							<div class="cover p-lg-5 p-4">
								<h1 class="mt-0" property="headline">
									<a href="${properties.original}" target="_blank" rel="noopener">
										<sling:encode value="${properties['jcr:title']}" mode="HTML" />
									</a>
								</h1>
								<em>
									<fmt:parseDate value="${properties.publishDate}" var="publishDate" pattern="yyyy-MM-dd" />
									Published on <time datetime="${properties.publishDate}6">
									<span property="datePublished">
										<fmt:formatDate value="${publishDate}" pattern="MMM d, yyyy" /> 
									</span></time> by 
									<span property="publisher" typeof="Person">
									<span property="name">${properties.author}</span> <c:if test="${not empty domain}">on ${domain}</c:if></span>
								</em>
							</div>
						</header>
					</div>
				</header>
				<div class="share background--grey py-2 pl-1">
					<sling:include path="share" resourceType="adobecommunity-org/components/general/share" />
				</div>
				<div class="padded-content">
					<c:choose>
						<c:when test="${empty properties.original}">
							<div class="post-body">
								<sling:include path="container" resourceType="sling-cms/components/general/container" />
							</div>
							<sling:call script="tags.jsp" />
							<sling:call script="comments.jsp" />
						</c:when>
						<c:otherwise>
							<blockquote class="blockquote">
								<div class="post-summary">
									${properties.snippet}
								</div>
								<footer class="blockquote-footer">
									<a href="${properties.original}" target="_blank" rel="noopener">
										Read the full post &quot;<sling:encode value="${properties['jcr:title']}" mode="HTML" />&quot; on ${domain}</a>
									</footer>
							</blockquote> 
							<sling:call script="tags.jsp" />
							<hr class="large" />
							<sling:call script="comments.jsp" />
						</c:otherwise>
					</c:choose>
				</div>
			</article>
		</div>
	</div>
</div>