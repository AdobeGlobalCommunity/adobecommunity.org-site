<%@include file="/apps/adobecommunity-org/global.jsp"%>
<div class="col-12 background--img" style="background-image: url(${fn:replace(properties.backgroundimage,'/content/agc/adobecommunity-org','')})">
    <div class="background--opaque">
        <div class="jumbotron jumbotron--light container">    
            <h1 class="display-3">
                <sling:encode value="${properties.header}" mode="HTML" />
            </h1>
            <sling:include path="container" resourceType="sling-cms/components/general/container" />
        </div>
    </div>
</div>