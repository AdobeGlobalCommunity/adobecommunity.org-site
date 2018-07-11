package org.adobecommunity.site.internal;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

@Component(service = Filter.class, property = { "sling.filter.scope=REQUEST",
		"sling.filter.pattern=.+/j_security_check", "service.ranking=-1" })
public class LoginFailureFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(response.isCommitted() && ((HttpServletResponse) response).getStatus() == 403) {
			response.getWriter().write("<script>window.location='/login.html?j_reason=INVALID_CREDENTIALS</script>");
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
