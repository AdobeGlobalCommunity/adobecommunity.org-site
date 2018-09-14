package org.adobecommunity.site.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

@Model(adaptables = Resource.class)
public class DiscussionMarkdownModel {

	@Self
	private Resource resource;

	public String getHtml() {
		MutableDataSet options = new MutableDataSet();

		Parser parser = Parser.builder(options).build();
		HtmlRenderer renderer = HtmlRenderer.builder(options).build();
		Node node = parser.parse(resource.getValueMap().get("jcr:content/jcr:description", String.class));

		return renderer.render(node);
	}
}
