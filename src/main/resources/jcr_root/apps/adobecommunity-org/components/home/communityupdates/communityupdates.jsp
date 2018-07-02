<%@include file="/apps/adobecommunity-org/global.jsp"%>
<div class="col-12">
    <div class="row">
        <div class="col-md-8 px-0">
            <div class="background--pad mr-sm-0 mr-md-2 background--white">
                <h3 class="mt-2 communityupdate__header">
                	<sling:encode value="${properties.eventsheader}" mode="HTML" />
                </h3>
                <hr class="my-2" />
                <h1>Placeholder for events!</h1>
            </div>
            <div class="background--pad mr-sm-0 mr-md-2 background--dark my-4 communityupdate__engage">
                <h3 class="my-2">
                	<sling:encode value="${properties.header}" mode="HTML" />
                </h3>
                ${properties.text}
            </div>
            <sling:include path="container" resourceType="sling-cms/components/general/container" />
        </div>
        <div class="col-md-4 background--white px-0">
            <div class="community-posts background--pad">
                <h3 class="mt-2 communityupdate__header">
                	<sling:encode value="${properties.postsheader}" mode="HTML" />
                </h3>
                <hr class="my-2" />
                <h1>Placeholder for articles!</h1>
            </div>
        </div>
    </div>
</div>