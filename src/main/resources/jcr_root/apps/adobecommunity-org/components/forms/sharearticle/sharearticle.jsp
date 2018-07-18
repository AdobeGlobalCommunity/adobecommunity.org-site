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
			Your article has been shared successfully!
		</div>
	</c:when>
</c:choose>
<form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Share Article">
    <fieldset>
        <legend>Article Information</legend>
        <div class="form-group">
            <label for="title">Title <span class="text-danger">*</span></label>
            <input class="form-control" required="required" name="title" />
        </div>
        <div class="form-group">
            <label for="summary">Summary <span class="text-danger">*</span></label>
            <textarea class="form-control" name="summary" required="required"></textarea>
        </div>
        <div class="form-group">
            <label for="link">Link <span class="text-danger">*</span></label>
            <input class="form-control" required="required" name="link" placeholder="https://" type="url">
        </div>
        <div class="form-group">
            <label for="thumbnail">Thumbnail <span class="text-danger">*</span></label>
            <input class="form-control" required="required" name="thumbnail" placeholder="https://" pattern="https:\/\/.+(\.jpg|\.jpeg|\.png)" type="url">
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
        <div class="form-group">
            <input name="created" type="hidden">
            <input name="layout" value="article" type="hidden">
            <button class="btn btn-success"><span class="fa fa-plus-circle"></span> Share Article</button>
            <a href="/content/agc/adobecommunity-org/learn/articles.html" class="btn btn-default">Cancel</a>
        </div>
    </fieldset>
</form>