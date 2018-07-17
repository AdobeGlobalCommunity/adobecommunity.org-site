package org.adobecommunity.site.impl.jobs;

import java.util.HashMap;
import java.util.Map;

import org.adobecommunity.site.impl.jobs.EmailQueueConsumer.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class, property = { JobConsumer.PROPERTY_TOPICS + "=" + EmailQueueConsumer.TOPIC })
@Designate(ocd = Config.class)
public class EmailQueueConsumer implements JobConsumer {

	@ObjectClassDefinition(name = "Email Queue Consumer")
	public @interface Config {

		String hostName();

		int smtpPort();

		boolean tlsEnabled();

		String username();

		@AttributeDefinition(type = AttributeType.PASSWORD)
		String password();

		String defaultSender();

	}

	@Reference
	private ResourceResolverFactory factory;
	private Config config;

	@Activate
	public void activate(Config config) {
		this.config = config;
	}

	private static final Logger log = LoggerFactory.getLogger(EmailQueueConsumer.class);

	public static final String SUBJECT = "subject";
	public static final String MESSAGE = "message";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String TOPIC = "adobecommunity-org/email/sendsimple";

	public static void queueMessage(JobManager jobMgr, String from, String to, String subject, String messageTemplate,
			Map<String, Object> parameters) {
		StrSubstitutor sub = new StrSubstitutor(parameters);

		String message = sub.replace(messageTemplate);

		log.debug("Queueing contact message from {} to {}", from, to);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmailQueueConsumer.SUBJECT, subject);
		data.put(EmailQueueConsumer.FROM, from);
		data.put(EmailQueueConsumer.MESSAGE, message);
		data.put(EmailQueueConsumer.TO, to);
		jobMgr.addJob(TOPIC, data);
		log.debug("Job queued successfully!");
	}

	public static void queueMessage(JobManager jobMgr, String to, String subject, String messageTemplate,
			Map<String, Object> parameters) {
		StrSubstitutor sub = new StrSubstitutor(parameters);

		String message = sub.replace(messageTemplate);

		log.debug("Queueing contact message to {}", to);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmailQueueConsumer.SUBJECT, subject);
		data.put(EmailQueueConsumer.MESSAGE, message);
		data.put(EmailQueueConsumer.TO, to);
		jobMgr.addJob(TOPIC, data);
		log.debug("Job queued successfully!");
	}

	public JobResult process(final Job job) {

		try {
			Email email = new SimpleEmail();
			email.setHostName(config.hostName());
			email.setSmtpPort(config.smtpPort());
			email.setAuthenticator(new DefaultAuthenticator(config.username(), config.password()));
			email.setStartTLSEnabled(config.tlsEnabled());
			log.debug("Configuring connection to {}:{} with username {}", config.hostName(), config.smtpPort(),
					config.username());

			String from = job.getProperty(FROM, String.class);
			if (StringUtils.isBlank(from)) {
				from = config.defaultSender();
			}
			String to = job.getProperty(TO, String.class);
			String subject = job.getProperty(SUBJECT, String.class);
			String message = job.getProperty(MESSAGE, String.class);

			email.setFrom(from);
			email.setSubject(job.getProperty(SUBJECT, String.class));
			email.setMsg(message);
			email.addTo(to);
			log.debug("Sending email from {} to {} with subject {}", from, to, subject);

			email.send();
		} catch (EmailException e) {
			log.warn("Exception sending email for job " + job.getTargetInstance(), e);
			return JobResult.FAILED;
		}

		// process the job and return the result
		return JobResult.OK;
	}
}
