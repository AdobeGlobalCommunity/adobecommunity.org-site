<%@include file="/libs/sling-cms/global.jsp"%>
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