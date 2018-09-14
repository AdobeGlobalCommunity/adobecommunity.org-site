<%-- /*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */ --%><rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:content="http://purl.org/rss/1.0/modules/content/">
<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/libs/sling-cms/global.jsp"%>
	<c:set var="site" value="${sling:adaptTo(resource,'org.apache.sling.cms.SiteManager').site}" />
	<channel>
		<title>${site.title}</title>
		<description>${site.description}</description>
		<link>${site.url}</link>
		<atom:link href="${site.url}/feed.xml" rel="self" type="application/rss+xml" />
		<c:set var="query" value="SELECT * FROM [sling:Page] WHERE ISDESCENDANTNODE([${site.path}/learn/articles]) AND [jcr:content/published]=true ORDER BY [jcr:content/publishDate] DESC" />
		<c:forEach var="postRsrc" items="${sling:findResources(resourceResolver,query,'JCR-SQL2')}" end="9">
			<item>
				<c:set var="post" value="${sling:adaptTo(postRsrc,'org.apache.sling.cms.PageManager').page}" />
				<c:choose>
					<c:when test="${fn:startsWith(post.properties['sling:thumbnail'], 'http')}">
						<c:set var="thumbnail" value="${post.properties['sling:thumbnail']}" />
					</c:when>
					<c:otherwise>
						<c:set var="thumbnail" value="${fn:replace(site.url,'https','http')}${fn:replace(post.properties['sling:thumbnail'],site.path,'')}" />
					</c:otherwise>
				</c:choose>
				<title><sling:encode value="${post.title}" mode="XML" /></title>
				<description><sling:encode value="${post.properties['jcr:description']}" mode="XML" /></description>
				<content:encoded>
					<![CDATA[
						<img src="${thumbnail}" title="${sling:encode(post.properties.summary,'XML_ATTR')}" />
						<sling:encode value="${post.properties.snippet}" mode="XML" />
					]]>
				</content:encoded>
				<c:if test="${not empty thumbnail}">
					<c:choose>
						<c:when test="${fn:indexOf(thumbnail,'.png') != -1}">
							<enclosure length="0" type="image/png" url="${thumbnail}" />
						</c:when>
						<c:otherwise>
							<enclosure length="0" type="image/jpeg" url="${thumbnail}" />
						</c:otherwise>
					</c:choose>
				</c:if>
				<fmt:parseDate value="${post.properties.publishDate}" var="publishDate" pattern="yyyy-MM-dd" />
				<pubDate><fmt:formatDate value="${publishDate}" pattern="EEE, dd MMM yyyy HH:mm:ss Z" /></pubDate>
				<link>${site.url}${post.publishedPath}</link>
				<guid isPermaLink="true">${site.url}${post.publishedPath}</guid>
			</item>
		</c:forEach>
	</channel>
</rss>