<%@include file="/libs/sling-cms/global.jsp"%>
<c:set var="path" value="${sling:adaptTo(resource,'org.apache.sling.cms.PageManager').page.path}" />
<form class="form-inline" method="get" action="${path}.html">
    <label class="sr-only" for="search">Search</label>
    <input type="search" class="form-control mb-2 mr-sm-2" id="search" placeholder="AEM" name="query" value="${sling:encode(param.query,'HTML_ATTR')}" />
    
    <label class="sr-only" for="type">Type</label>
    <select name="type" class="form-control mb-2 mr-sm-2" id="type" required="required">
        <option value="individual" ${param.type == 'individual' ? 'selected="selected"' : ''}>Individuals</option>
        <option value="company" ${param.type == 'individual' ? '' : 'selected="selected"'}>Companies</option>
    </select>
    
    <div class="dropdown mb-2 mr-sm-2">
        <button class="btn btn-secondary dropdown-toggle" type="button" id="experienceselect" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            Expertise
        </button>
        <div class="dropdown-menu" aria-labelledby="experienceselect">
            <div class="px-4 py-3">
                <c:forEach var="experience" items="${sling:listChildren(sling:getResource(resourceResolver,'/etc/taxonomy/adobe-experience-cloud'))}">
                    <div class="form-check">
                        <c:set var="checked" value="${false}" />
                        <c:forEach var="v" items="${paramValues.experience}">
                            <c:if test="${v == experience.name}">
                                <c:set var="checked" value="${true}" />
                            </c:if>
                        </c:forEach>
                        <input type="checkbox" class="form-check-input" id="experience-${experience.name}" value="${experience.name}" name="experience" ${checked ? 'checked="checked"' : ''} />
                        <label class="form-check-label" for="experience-${experience.name}">
                            ${fn:replace(experience.valueMap['jcr:title'],' ','&nbsp;')}
                        </label>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="dropdown mb-2 mr-sm-2">
        <button class="btn btn-secondary dropdown-toggle" type="button" id="rolesselect" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            Role
        </button>
        <div class="dropdown-menu" aria-labelledby="rolesselect">
            <div class="px-4 py-3">
                <c:forEach var="role" items="${sling:listChildren(sling:getResource(resourceResolver,'/etc/taxonomy/project-roles'))}">
                    <div class="form-check">
                        <c:set var="checked" value="${false}" />
                        <c:catch var="e">
                            <c:forEach var="v" items="${paramValues.roles}">
                                <c:if test="${v == role.name}">
                                    <c:set var="checked" value="${true}" />
                                </c:if>
                            </c:forEach>
                        </c:catch>
                        <input type="checkbox" class="form-check-input" id="roles-${role.name}" value="${role.name}" name="roles"  ${checked ? 'checked="checked"' : ''} />
                        <label class="form-check-label" for="roles-${role.name}">
                            ${fn:replace(role.valueMap['jcr:title'],' ','&nbsp;')}
                        </label>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <button type="submit" class="btn btn-primary mb-2 mr-sm-2">Search</button>
</form>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.adobecommunity.site.models.ProfileSearch" var="profileSearch" />
<c:set var="results" value="${profileSearch.profiles}" />
<div class="card-columns">
    <c:forEach var="profile" items="${results}">
        <div class="card ${profile.featured ? 'border-info' : ''}">
            <div class="card-body">
                <h5 class="card-title">
                    <a href="${path}/${profile.company ? 'company' : 'profile'}.html${profile.id}">
                        <sling:encode value="${profile.name}" mode="HTML" />
                    </a>
                </h5>
                <p class="card-text">
                    <sling:encode value="${fn:substring(profile.about,0,200)}" mode="HTML" />...
                </p>
            </div>
            <div class="card-footer">
                <c:forEach var="logo" items="${profile.productLogos}">
                    <img src="${logo}" />
                </c:forEach>
            </div>
        </div>
    </c:forEach>
</div>
<nav aria-label="Pagination">
    <ul class="pagination">
        <li class="page-item">
            <c:choose>
                <c:when test="${not empty profileSearch.previous}">
                    <a class="page-link" href="${profileSearch.previous}">Previous</a>
                </c:when>
                <c:otherwise>
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1">Previous</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </li>
        <li class="page-item">
            <c:choose>
                <c:when test="${not empty profileSearch.next}">
                    <a class="page-link" href="${profileSearch.next}">Next</a>
                </c:when>
                <c:otherwise>
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1">Next</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </li>
  </ul>
</nav>
<c:set var="closed" value="${profileSearch.close}" />