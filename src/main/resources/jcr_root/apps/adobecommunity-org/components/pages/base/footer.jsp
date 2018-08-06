<%@include file="/apps/adobecommunity-org/global.jsp"%>
<footer class="background--dark primary-footer py-3">
    <div class="container">
        <ul class="nav">
            <li class="nav-item">
                <jsp:useBean id="date" class="java.util.Date" />
                <fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
                <a class="nav-link disabled" href="/content/agc/adobecommunity-org/">&copy; Copyright ${currentYear} &mdash; Adobe Global Community</a>
            </li>
        </ul>
        <div class="pull-right">
	        <ul class="nav">
	            <li class="nav-item">
	                <a class="nav-link" href="https://twitter.com/adobeglobal" target="_blank" title="Follow Adobe Global Community on Twitter"><em class="fa fa-twitter"></em></a>
	            </li>
	            <li class="nav-item">
	                <a class="nav-link" href="https://www.facebook.com/groups/Adobe.Global.Community/" target="_blank" title="Connect with the Adobe Global Community on Facebook"><em class="fa fa-facebook"></em></a>
	            </li>
	            <li class="nav-item">
	                <a class="nav-link" href="https://www.meetup.com/Worldwide-Adobe-Experience-Cloud-Developer-Community/" target="_blank" title="Meet other Adobe Global Community Members!"><em class="fa fa-meetup"></em></a>
	            </li>
	        </ul>
	        <div>
	        	<a href="https://github.com/AdobeGlobalCommunity/adobecommunity.org-site/issues" target="_blank" class="pull-right">Report Issue</a>
	        </div>
        </div>
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="/content/agc/adobecommunity-org/contact.html">Contact Us</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/content/agc/adobecommunity-org/terms.html">Terms &amp; Conditions</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/content/agc/adobecommunity-org/privacy-policy.html">Privacy Policy</a>
            </li>
        </ul>
	  <form class="form-inline" action="/content/agc/adobecommunity-org/search.html">
	    <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" name="q" />
	    <button class="btn btn-outline-dark my-2 my-sm-0" type="submit">Search</button>
	  </form>
    </div>
</footer>