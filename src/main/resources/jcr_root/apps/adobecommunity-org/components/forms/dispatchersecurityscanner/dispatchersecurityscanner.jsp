<%@include file="/libs/sling-cms/global.jsp"%>
<c:choose>
    <c:when test="${empty slingRequest.requestPathInfo.suffix}">
        <h3>Dispatcher Security Scanner</h3>
        <form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Check AEM Site">
            <fieldset>
                <p>Fill out the following information to check your AEM website's dispatcher configuration for common security issues.</p>
                <div class="form-group">
                    <label for="protocol">Protocol <span class="text-danger">*</span></label>
                    <select class="form-control" required="required" name="protocol">
                        <option>https</option>
                        <option>http</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="domain">Domain <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="domain" />
                    <p>The domain (without protocol or path) of the website to check, e.g. www.adobe.com</p>
                </div>
                <div class="form-group">
                    <label for="pagepath">Page Path <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="pagepath" />
                    <p>The URL path to a valid page without extension, e.g. /marketing/experience-manager</p>
                </div>
            </fieldset>
            <fieldset>
                <div class="form-group">
                    <button class="btn btn-success">Scan</button>
                    <a href="/members.html" class="btn btn-default">Cancel</a>
                </div>
            </fieldset>
        </form>
    </c:when>
    <c:otherwise>
        <c:set var="config" value="${sling:getRelativeResource(slingRequest.requestPathInfo.suffixResource,'jcr:content')}" />
        <h3>Dispatcher Security Scan Results for: </h3>
        <p>
            <sling:encode value="${config.valueMap.protocol}" mode="HTML" />://<sling:encode value="${config.valueMap.domain}" mode="HTML" /><sling:encode value="${config.valueMap.pagepath}" mode="HTML" />
        </p>
        <c:set var="currentPage" value="${sling:adaptTo(resource,'org.apache.sling.cms.PageManager').page}" />
        <a href="${currentPage.path}.html" class="btn btn-secondary mb-4">
            &lt; New Scan
        </a>
        <dl class="mb-4">
            <dt>Status</dt>
            <dd>${config.valueMap.status}</dd>
            <dt>Requested On</dt>
            <dd><fmt:formatDate value="${config.valueMap.requested.time}" type="BOTH" /></dd>
            <dt>Checks Succeeded</dt>
            <dd>${config.valueMap.succeeded} / ${config.valueMap.count}</dd>
        </dl>
        <h4>Failed Checks</h4>
        <div class="accordion mb-4" id="failedchecks">
            <c:forEach var="check" items="${sling:listChildren(config)}" varStatus="status">
                <c:if test="${not check.valueMap.succeeded}">
                    <c:set var="cfgPath" value="checks/${check.name}" />
                    <c:set var="cfg" value="${sling:getRelativeResource(resource,cfgPath)}" />
                    <c:choose>
                        <c:when test="${cfg.valueMap.severity == 'critical'}">
                            <c:set var="clazz" value="text-white bg-danger" />
                        </c:when>
                        <c:when test="${cfg.valueMap.severity == 'major'}">
                            <c:set var="clazz" value="text-white bg-warning" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="clazz" value="text-white bg-info" />
                        </c:otherwise>
                    </c:choose>
                    <div class="card ${clazz}">
                        <div class="card-header" id="check-${status.index}">
                            <h5 class="mb-0">
                                <button class="btn btn-link text-white" type="button" data-toggle="collapse" data-target="#collapse-${status.index}" aria-expanded="true" aria-controls="collapse-${status.index}">
                                    <sling:encode value="${check.valueMap.message}" mode="HTML" />
                                </button>
                            </h5>
                        </div>
                        <div id="collapse-${status.index}" class="collapse" aria-labelledby="check-${status.index}" data-parent="#failedchecks">
                            <div class="card-body">
                                <dl>
                                    <dt>Severity</dt>
                                    <dd>${cfg.valueMap.severity}</dd>
                                    <dt>Response Status</dt>
                                    <dd><sling:encode value="${check.valueMap.statusline}" mode="HTML" /></dd>
                                    <dt>View</dt>
                                    <dd>
                                        <c:choose>
                                            <c:when test="${cfg.valueMap.method == 'POST'}">
                                                <form method="POST" action="${check.valueMap.link}" target="_blank">
                                                    <input type="submit" value="Preview" class="btn btn-primary" />
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${check.valueMap.link}" target="_blank">
                                                    ${check.valueMap.link}
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </dd>
                                </dl>
                                ${cfg.valueMap.explaination}
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
        
        <h4>Successful Checks</h4>
        <div class="accordion" id="succeessfulchecks">
            <c:forEach var="check" items="${sling:listChildren(config)}" varStatus="status">
                <c:if test="${check.valueMap.succeeded}">
                    <div class="card">
                        <div class="card-header" id="check-${status.index}">
                            <h5 class="mb-0">
                                <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#collapse-${status.index}" aria-expanded="true" aria-controls="collapse-${status.index}">
                                    <sling:encode value="${check.valueMap.message}" mode="HTML" />
                                </button>
                            </h5>
                        </div>
                        <div id="collapse-${status.index}" class="collapse" aria-labelledby="check-${status.index}" data-parent="#succeessfulchecks">
                            <div class="card-body">
                                <c:set var="cfgPath" value="checks/${check.name}" />
                                <c:set var="cfg" value="${sling:getRelativeResource(resource,cfgPath)}" />
                                <dl>
                                    <dt>Severity</dt>
                                    <dd>${cfg.valueMap.severity}</dd>
                                    <dt>Response Status</dt>
                                    <dd><sling:encode value="${check.valueMap.statusline}" mode="HTML" /></dd>
                                    <dt>View</dt>
                                    <dd>
                                        <c:choose>
                                            <c:when test="${cfg.valueMap.method == 'POST'}">
                                                <form method="POST" action="${check.valueMap.link}" target="_blank">
                                                    <input type="submit" value="Preview" class="btn btn-primary" />
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${check.valueMap.link}" target="_blank">
                                                    ${check.valueMap.link}
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </dd>
                                </dl>
                                ${cfg.valueMap.explaination}
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
<c:if test="${cmsEditEnabled == 'true'}">
    <h2>Checks</h2>
    <c:set var="oldAvailableTypes" value="${availableTypes}" />
    <c:set var="availableTypes" value="AdobeCommunity.org Check" scope="request" />
    <sling:include path="checks" resourceType="sling-cms/components/general/container" />
    <c:set var="availableTypes" value="${oldAvailableTypes}" scope="request" />
</c:if>