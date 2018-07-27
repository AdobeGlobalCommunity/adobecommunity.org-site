<%@include file="/apps/adobecommunity-org/global.jsp"%>
<body>
    <!-- Google Tag Manager (noscript) -->
    <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-5SQ5QW6" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
    <!-- End Google Tag Manager (noscript) -->
	<sling:call script="/libs/sling-cms/components/editor/scripts/init.jsp" />
	<sling:call script="header.jsp" />
	<sling:call script="content.jsp" />
	<sling:call script="footer.jsp" />
	<sling:call script="scripts.jsp" />
	<sling:call script="/libs/sling-cms/components/editor/scripts/finalize.jsp" />
</body>