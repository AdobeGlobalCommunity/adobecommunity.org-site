<%@include file="/libs/sling-cms/global.jsp"%>
<h3 class="my-4">
        <sling:encode value="${properties.header}" mode="HTML" />
        <a href="/events/add.html" class="btn btn-primary pull-right">+ Event</a>
    </h3>

<table class="table table-hover table-responsive agenda w-100 d-block d-md-table">
    <thead>
        <tr>
            <th>Date</th>
            <th>Time</th>
            <th>Event</th>
        </tr>
    </thead>
    <tbody class="ajax-load carousel-inner" data-src="${resource.path}.model.json" data-tpl="event-list" data-filter="[{'key': 'eventDate', 'method': 'future'}]" data-filter-param="type" data-limit="20">
        <tr>
            <td colspan="3" class="ajax-load__loader text-center">  
                <i class="my-4 fa fa-refresh fa-spin fa-3x fa-fw"></i>
            </td>
        </tr>
    </tbody>
</table>