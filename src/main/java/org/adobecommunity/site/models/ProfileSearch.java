package org.adobecommunity.site.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.jcr.query.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class ProfileSearch {

    private static final Logger log = LoggerFactory.getLogger(ProfileSearch.class);

    @OSGiService
    private ResourceResolverFactory resolverFactory;

    private ResourceResolver serviceResolver;

    private SlingHttpServletRequest request;

    private boolean hasMore;

    public ProfileSearch(SlingHttpServletRequest request) {
        this.request = request;
    }

    private String generateQuery() {
        log.trace("generateQuery");
        String query = "SELECT * FROM [rep:User] AS s";

        String prefix = "individual".equals(request.getParameter("type")) ? "jobprofile/" : "companyprofile/";

        List<String> predicates = new ArrayList<>();

        predicates.add("[" + prefix + "public]='yes'");

        if (request.getParameterValues("roles") != null) {
            log.trace("Adding roles: {}", Arrays.toString(request.getParameterValues("roles")));
            predicates.add(Arrays.stream(request.getParameterValues("roles"))
                    .map(r -> "s.[" + prefix + "roles]='" + r.replaceAll("'", "''") + "'")
                    .collect(Collectors.joining(" OR ")));
        }
        if (request.getParameterValues("experience") != null) {
            log.trace("Adding experience: {}", Arrays.toString(request.getParameterValues("experience")));
            predicates.add(Arrays.stream(request.getParameterValues("experience"))
                    .map(r -> "s.[" + prefix + "experience]='" + r.replaceAll("'", "''") + "'")
                    .collect(Collectors.joining(" OR ")));
        }
        if (StringUtils.isNotEmpty(request.getParameter("query"))) {
            log.trace("Adding query: {}", query);
            predicates.add("[" + prefix + "*] LIKE '%" + request.getParameter("query").replace("'", "''") + "%'");
        }

        if (predicates.size() > 0) {
            query += " WHERE (" + predicates.stream().collect(Collectors.joining(") AND (")) + ")";
        }
        query += " ORDER BY [jcr:score]";

        log.debug("Generated query: {}", query);

        return query;
    }

    public String getNext() {
        if (hasMore) {
            if (request.getQueryString().contains("page=")) {
                return request.getQueryString().replace("page=" + request.getParameter("page"),
                        "page=" + String.valueOf(getStart() + 12));
            } else if (StringUtils.isNotEmpty(request.getQueryString())) {
                return request.getQueryString() + "&page=" + String.valueOf(getStart() + 12);
            } else {
                return "?page=" + String.valueOf(getStart() + 12);
            }
        } else {
            return null;
        }
    }

    public String getPrevious() {
        if (getStart() != 0) {
            if (request.getQueryString().contains("page=")) {
                return request.getQueryString().replace("page=" + request.getParameter("page"),
                        "page=" + String.valueOf(getStart() - 12));
            } else if (StringUtils.isNotEmpty(request.getQueryString())) {
                return request.getQueryString() + "&page=" + String.valueOf(getStart() - 12);
            } else {
                return "?page=" + String.valueOf(getStart() - 12);
            }
        } else {
            return null;
        }
    }

    public List<Profile> getProfiles() throws LoginException {
        log.trace("getProfiles");
        List<Profile> result = new ArrayList<>();
        String subpath = "individual".equals(request.getParameter("type")) ? "jobprofile" : "companyprofile";

        serviceResolver = resolverFactory
                .getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "usersearch"));
        Iterator<Resource> resources = serviceResolver.findResources(generateQuery(), Query.JCR_SQL2);
        int start = getStart();
        for (int i = 0; resources.hasNext(); i++) {
            if (i >= start && i < start + 12) {
                result.add(resources.next().getChild(subpath).adaptTo(Profile.class));
            } else if (i < start) {
                resources.next();
            } else {
                hasMore = true;
                break;
            }
        }
        log.debug("Found {} results", result.size());

        return result;
    }

    private int getStart() {
        int start = 0;
        if (request.getParameterMap().containsKey("page")) {
            try {
                start = 12 * (Integer.parseInt(request.getParameter("page"), 10) - 1);
            } catch (NumberFormatException nfe) {
                log.warn("Failed to load page", nfe);
            }
        }
        return start;
    }

    public boolean isClose() {
        if (serviceResolver != null) {
            serviceResolver.close();
        }
        return true;
    }

}
