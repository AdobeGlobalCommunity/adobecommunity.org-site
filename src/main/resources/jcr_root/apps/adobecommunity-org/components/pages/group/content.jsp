<%@include file="/apps/adobecommunity-org/global.jsp"%>
<div class="container main">
    <div class="padded-content h-100">
		<div class="row">
			<div class="col-sm-3">
				<a href="${properties.link}" target="_blank">
					<img src="${properties.image}" title="${sling:encode(properties['jcr:title'],'HTML_ATTR')}" class="img-flex" />
				</a>
				<div class="my-2">
					<span class="fa fa-map-marker"></span>
					<a href="https://www.google.com/maps/place/${sling:encode(properties.location,'HTML_ATTR')}/" target="_blank">
						${sling:encode(properties['jcr:title'],'HTML')}
					</a>
				</div>
				<ul class="nav flex-column my-2">
					<li class="nav-item">
						<a class="nav-link" href="${properties.link}/quick_join/" target="_blank">Join</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${properties.link}/members/" target="_blank">Members</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${properties.link}/messages/boards/" target="_blank">Message Board</a>
					</li>
				</ul>
			</div>
			<div class="col-sm-9">
				<h1 class="display-5">${sling:encode(properties['jcr:title'],'HTML')}</h1>
				<p>
					<sling:encode value="${properties['jcr:description']}" mode="HTML" />
				</p>
				<a href="${properties.link}" class="btn btn-secondary" target="_blank">
					Group Page
				</a>
				<h3 class="mt-4">Upcoming Events</h3>
				<div class="meetup-event--container my-2" data-meetup-id="${fn:replace(properties.link, 'https://www.meetup.com', '')}">
					<div class="my-5 meetup-event--oauth text-center">
						<a href="https://secure.meetup.com/oauth2/authorize?client_id=ehllabo2ftktd6g3cfj5b48gsr&response_type=token&redirect_uri=https://adobecommunity.org/meetup-auth.html" class="btn btn-secondary meetup-event--button">
							Connect to Meetup for Events
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>