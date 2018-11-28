package org.adobecommunity.site.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;

import org.apache.jackrabbit.value.ValueFactoryImpl;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ResourceUtil.BatchResourceRemover;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Runnable.class, property = "scheduler.expression=0 0 * * * ?")
public class CleanupScanScheduler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CleanupScanScheduler.class);

    @Reference
    private ResourceResolverFactory rrFactory;

    @Override
    public void run() {
        log.info("run");

        ResourceResolver resolver = null;
        try {
            resolver = rrFactory.getServiceResourceResolver(new HashMap<String, Object>() {
                private static final long serialVersionUID = 1L;
                {
                    put(ResourceResolverFactory.SUBSERVICE, "ugc");
                }
            });

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -30);
            String calStr = ValueFactoryImpl.getInstance().createValue(cal).getString();

            Iterator<Resource> checks = resolver.findResources(
                    "SELECT * FROM [sling:UGC] WHERE ISDESCENDANTNODE([/etc/usergenerated/agc/adobecommunity-org/check]) AND [jcr:content/requested] <= CAST('"
                            + calStr + "' AS DATE)",
                    Query.JCR_SQL2);

            while (checks.hasNext()) {
                Resource oldCheck = checks.next();
                log.debug("Removing old check {}", oldCheck);

                BatchResourceRemover remover = ResourceUtil.getBatchResourceRemover(100);
                remover.delete(oldCheck);
                resolver.commit();
            }

        } catch (LoginException e) {
            log.error("Failed to login", e);
        } catch (RepositoryException e) {
            log.error("Failed to generate date for query", e);
        } catch (PersistenceException e) {
            log.error("Failed to delete old checks", e);
        } finally {
            if (resolver != null) {
                resolver.close();
            }
        }

    }

}
