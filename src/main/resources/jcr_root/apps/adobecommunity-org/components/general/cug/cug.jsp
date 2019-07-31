<%@include file="/apps/adobecommunity-org/global.jsp"%>
<div class="${cmsEditEnabled ? '' : 'agc__cug' }" data-auth="true" style="${cmsEditEnabled ? '' : 'display:none' }">
    ${cmsEditEnabled ? 'Authenticated Content' : '' }
    <sling:include path="auth" resourceType="sling-cms/components/general/container" />
</div>
<div class="${cmsEditEnabled ? '' : 'agc__cug' }" data-auth="false">
    ${cmsEditEnabled ? 'Unauthenticated Content' : '' }
    <sling:include path="anon" resourceType="sling-cms/components/general/container" />
</div>