<%@include file="/libs/sling-cms/global.jsp"%>
<div id="event-carousel" class="carousel slide" data-ride="carousel">
	<div class="ajax-load carousel-inner" data-src="${resource.path}.model.json" data-tpl="event-carousel" data-filter="[{'key': 'eventDate', 'method': 'future'},{'key': 'featured', 'value': true}]" data-limit="4">
	    <div class="ajax-load__loader text-center">
	        <i class="my-4 fa fa-refresh fa-spin fa-3x fa-fw"></i>
	    </div>
	</div>
	<a class="carousel-control-prev" href="#event-carousel" role="button" data-slide="prev">
		  <span class="carousel-control-prev-icon" aria-hidden="true"></span>
		  <span class="sr-only">Previous</span>
	</a>
	<a class="carousel-control-next" href="#event-carousel" role="button" data-slide="next">
		<span class="carousel-control-next-icon" aria-hidden="true"></span>
		<span class="sr-only">Next</span>
	</a>
</div>