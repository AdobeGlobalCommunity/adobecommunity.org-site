package org.adobecommunity.site.models;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { SlingHttpServletRequest.class })
public class UserProfile {

	private static final Logger log = LoggerFactory.getLogger(UserProfile.class);

	public static final String SUBPATH = "profile/";

	@Required
	private String name;

	@Required
	private String phone;

	@Required
	private String company;

	private String role;

	private String[] products;

	private String topics;

	private String presentation;

	private String organizing;

	public UserProfile(SlingHttpServletRequest request) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : this.getClass().getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				log.debug("Loading field {}", field.getName());
				field.setAccessible(true);
				if (field.getType().isArray()) {
					field.set(this, request.getParameterValues(field.getName()));
				} else {
					String value = request.getParameter(field.getName());
					if (StringUtils.isNotBlank(value) || field.getDeclaredAnnotation(Required.class) == null) {
						field.set(this, value);
					} else {
						log.debug("No value provided for reqired parameter {}", field.getName());
						throw new java.lang.IllegalArgumentException(
								"No value provided for required parameter " + field.getName());
					}
				}
			}
		}
	}

	public String getCompany() {
		return company;
	}

	public String getName() {
		return name;
	}

	public String getOrganizing() {
		return organizing;
	}

	public String getPhone() {
		return phone;
	}

	public String getPresentation() {
		return presentation;
	}

	public String[] getProducts() {
		return products;
	}

	public String getRole() {
		return role;
	}

	public String getTopics() {
		return topics;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrganizing(String organizing) {
		this.organizing = organizing;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}

	public void setProducts(String[] products) {
		this.products = products;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

	public void updateUser(User user, Session session) throws IllegalArgumentException, IllegalAccessException,
			UnsupportedRepositoryOperationException, RepositoryException {
		ValueFactory vf = session.getValueFactory();
		for (Field field : this.getClass().getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				field.setAccessible(true);
				if (!ArrayUtils.contains(new String[] { "email", "password" }, field.getName())) {
					log.debug("Setting field {}", field.getName());
					if (field.getType().isArray()) {
						String[] v = (String[]) field.get(this);
						int len = v != null ? v.length : 0;
						Value[] values = new Value[len];
						for (int i = 0; i < len; i++) {
							values[i] = vf.createValue(v[i]);
						}
						user.setProperty(SUBPATH + field.getName(), values);
					} else {
						user.setProperty(SUBPATH + field.getName(), vf.createValue((String) field.get(this)));
					}
				} else {
					log.debug("Skipping field {}", field.getName());
				}
			}
		}
	}

	public Map<String, Object> toMap()
			throws IllegalArgumentException, IllegalAccessException, UnsupportedRepositoryOperationException {
		Map<String, Object> data = new HashMap<String, Object>();
		for (Field field : this.getClass().getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				field.setAccessible(true);
				if (!"password".equals(field.getName())) {
					data.put(field.getName(), field.get(this));
				}
			}
		}
		return data;
	}

}