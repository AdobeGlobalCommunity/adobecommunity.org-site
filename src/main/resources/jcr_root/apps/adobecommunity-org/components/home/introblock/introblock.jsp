<%@include file="/apps/adobecommunity-org/global.jsp"%>
<div class="col-12 p-4 {{cb.background}} supportlist">
    <div class="row">
        <div class="col-md-8 supportlist__lead lead">
        	${properties.text}
        </div>
        <div class="col-md-4 supportlist__list">
            <ul>
                <c:forEach var="item" items="${properties.items}">
                    <li>${item}</li>
                </c:forEach>
            </ul>
            <sling:include path="cta" resourceType="adobecommunity-org/components/general/cta" />
        </div>
    </div>
</div>