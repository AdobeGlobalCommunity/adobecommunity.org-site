<%@include file="/apps/adobecommunity-org/global.jsp"%>
<sling:adaptTo var="pageMgr" adaptable="${resource}" adaptTo="org.apache.sling.cms.PageManager" />
<c:set var="page" value="${pageMgr.page}" />
<ul class="navbar-nav">
    <c:choose>
        <c:when test="${page.path == '/content/agc/adobecommunity-org'}">
            <li class="nav-item active">
                <a class="nav-link" href="/content/agc/adobecommunity-org/">Home <span class="sr-only">(current)</span></a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="nav-item">
                <a class="nav-link" href="/content/agc/adobecommunity-org/">Home</a>
            </li>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(page.path,'/content/agc/adobecommunity-org/members') || fn:contains(page.path,'/content/agc/adobecommunity-org/login' || fn:contains(page.path,'/content/agc/adobecommunity-org/join'))}">
            <li class="nav-item active agc__cug" data-auth="true" style="display:none">
                <a class="nav-link" href="/content/agc/adobecommunity-org/members.html">Members <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item active agc__cug" data-auth="false">
                <a class="nav-link" href="/content/agc/adobecommunity-org/login.html">Members <span class="sr-only">(current)</span></a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="nav-item agc__cug" data-auth="true" style="display:none">
                <a class="nav-link" href="/content/agc/adobecommunity-org/members.html">Members</a>
            </li>
            <li class="nav-item agc__cug" data-auth="false">
                <a class="nav-link" href="/content/agc/adobecommunity-org/login.html">Members</a>
            </li>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(page.path,'/content/agc/adobecommunity-org/learn')}">
            <li class="nav-item active">
                <a class="nav-link" href="/content/agc/adobecommunity-org/learn.html">Learn <span class="sr-only">(current)</span></a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="nav-item">
                <a class="nav-link" href="/content/agc/adobecommunity-org/learn.html">Learn</a>
            </li>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(page.path,'/content/agc/adobecommunity-org/community')}">
            <li class="nav-item active">
                <a class="nav-link" href="/content/agc/adobecommunity-org/community.html">Community <span class="sr-only">(current)</span></a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="nav-item">
                <a class="nav-link" href="/content/agc/adobecommunity-org/community.html">Community</a>
            </li>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(page.path,'/content/agc/adobecommunity-org/events')}">
            <li class="nav-item active">
                <a class="nav-link" href="/content/agc/adobecommunity-org/events.html">Events <span class="sr-only">(current)</span></a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="nav-item">
                <a class="nav-link" href="/content/agc/adobecommunity-org/events.html">Events</a>
            </li>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${fn:contains(page.path,'/content/agc/adobecommunity-org/about')}">
            <li class="nav-item active">
                <a class="nav-link" href="/content/agc/adobecommunity-org/about.html">About <span class="sr-only">(current)</span></a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="nav-item">
                <a class="nav-link" href="/content/agc/adobecommunity-org/about.html">About</a>
            </li>
        </c:otherwise>
    </c:choose>
</ul>
<div class="agc__cug flex-row ml-md-auto d-none d-md-flex" data-auth="true" data-template="user-header">
</div>
<ul class="nav navbar-nav agc__cug" data-auth="false">
    <li class="nav-item">
        <a class="btn btn btn-outline-secondary px-5" href="/content/agc/adobecommunity-org/join.html">Join</a>
    </li
</ul>