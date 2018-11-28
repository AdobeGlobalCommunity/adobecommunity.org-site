<%@include file="/libs/sling-cms/global.jsp"%>
<dl class="my-2">
	<dt>
        URL
    </dt>
    <dd>
        ${sling:encode(properties.url,'HTML')}
    </dd>
    <dt>
        Method
    </dt>
    <dd>
        ${sling:encode(properties.method,'HTML')}
    </dd>
    <dt>
        Severity
    </dt>
    <dd>
        ${sling:encode(properties.severity,'HTML')}
    </dd>
    <dt>
        Explaination
    </dt>
    <dd>
        ${properties.explaination}
    </dd>
    <dt>
        Valid HTTP Response Codes
    </dt>
    <dd>
        <c:choose>
            <c:when test="${not empty properties.responsecodes}">
                ${fn:join(properties.responsecodes,',')}
            </c:when>
            <c:otherwise>
                ${fn:join(resource.parent.parent.valueMap.responsecodes,',')}
            </c:otherwise>
        </c:choose>
    </dd>
    <c:if test="${not empty properties.responsebody}">
        <dt>
            Response Body Must Not Match
        </dt>
        <dd>
            ${properties.responsebody}
        </dd>
    </c:if>
    <c:if test="${not empty properties.headers}">
        <dt>
            Headers Must Contain
        </dt>
        <dd>
            ${properties.headers}
        </dd>
    </c:if>
</dl>