<%@include file="/libs/sling-cms/global.jsp"%>
<c:choose>
	<c:when test="${param.err == 'req'}">
		<div class="alert alert-danger" role="alert">
			Please complete all required fields.
		</div>
	</c:when>
	<c:when test="${param.err == 'err'}">
		<div class="alert alert-danger" role="alert">
			An unexpected error has occurred. Please try again.
		</div>
	</c:when>
	<c:when test="${param.res == 'shared'}">
		<div class="alert alert-success" role="alert">
			Your event has been shared successfully!
		</div>
	</c:when>
</c:choose>
<form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Share Event">
    <fieldset>
        <legend>Event Information</legend>
        <div class="form-group">
            <label for="title">Title <span class="text-danger">*</span></label>
            <input class="form-control" required="required" name="title" type="text">
        </div>
        <div class="form-group">
            <label for="summary">Summary <span class="text-danger">*</span></label>
            <textarea class="form-control" name="summary"></textarea>
        </div>
        <div class="form-group">
            <label for="eventtype">Type <span class="text-danger">*</span></label>
            <select class="form-control" required="required" name="eventtype">
                <option value="webinar">Webinar</option>
                <option value="conference">Conference</option>
                <option value="meetup">Meetup</option>
            </select>
        </div>
        <div class="form-group">
            <label for="event_date">Event Date <span class="text-danger">*</span></label>
            <input class="form-control" required="required" name="eventdate" placeholder="MM-dd-YYYY" type="date" />
        </div>
        <div class="form-group">
            <label for="event_date">Event Time <span class="text-danger">*</span></label>
            <input class="form-control" required="required" name="eventtime" />
        </div>
        <div class="form-group">
            <label for="link">Link <span class="text-danger">*</span></label>
            <input class="form-control" required="required" name="link" placeholder="https://" type="url" />
        </div>
        <div class="form-group">
            <label for="presenter">Presenter</label>
            <input class="form-control" name="presenter" type="text">
        </div>
        <div class="form-group">
            <label for="image">Thumbnail</label>
            <input class="form-control" name="image" placeholder="https://" type="url">
        </div>
        <div class="form-group repeating-parent" data-name="tags">
            <fieldset class="d-none" disabled="disabled">
                <div class="repeating-item repeating-template">
                    <div class="input-group my-1">
                        <input required="required" class="form-control" name="tags" list="tag-options" type="text">
                        <span class="input-group-btn">
                            <button class="btn btn-secondary repeating-remove">-</button>
                        </span>
                    </div>
                </div>
            </fieldset>
            <label for="tags">Tags</label>
            <div class="repeating-container">
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <a href="#" class="btn btn-secondary repeating-add">+</a> 
                </div>
            </div>
            <datalist id="tag-options">
            	<c:forEach var="tag" items="${sling:findResources(resourceResolver,'SELECT * FROM [sling:Taxonomy]','JCR-SQL2')}">
            		<option value="${tag.path}">${sling:encode(tag.valueMap['jcr:title'],'HTML')}</option>
            	</c:forEach>
            </datalist>
        </div>
    </fieldset>
    <fieldset>
        <legend>Event Location</legend>
        <div class="form-group">
            <label for="location_name">Location <span class="text-danger">*</span></label>
            <input class="form-control" required="required" name="location" type="text" />
        </div>
        <div class="form-group">
            <label for="location_link">Location Link</label>
            <input class="form-control" name="locationlink" placeholder="https://" type="url" />
        </div>
    </fieldset>
    <fieldset>
        <div class="form-group">
            <input name="created" type="hidden">
            <input name="layout" value="event" type="hidden">
            <button class="btn btn-success"><span class="fa fa-plus-circle"></span> Share Event</button>
            <a href="/content/agc/adobecommunity-org/events.html" class="btn btn-default">Cancel</a>
        </div>
    </fieldset>
</form>