package authdb.db;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * A user object backed by Hibernate.
 */
public class Principal implements User {

	// instance variables corresponding to columns in the table

	private String id;

	private String password;

	private Set<Role> roles;

	public Principal() {
	}

	/**
	 * @param id
	 *            the unique user id
	 * @param password
	 *            the password
	 * @param roles
	 *            a set of role name Strings
	 */
	public Principal(String id, String password, Set<String> roles) {
		this.id = id;
		this.password = password;
		this.roles = mapStringsToRoles(roles);
	}

	// accessors and mutators for the instance variables (also used by
	// Hibernate)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	Set<Role> getRealRoles() {
		return roles;
	}

	void setRealRoles(Set<Role> roles) {
		this.roles = roles;
	}

	// methods required by interface User (not used by Hibernate)

	public Set<String> getRoles() {
		return mapRolesToStrings(roles);
	}

	public void setRoles(Set<String> roles) {
		this.roles = mapStringsToRoles(roles);
	}

	public int compareTo(User that) {
		return getId().compareTo(that.getId());
	}

	@Override
	public String toString() {
		return "{id=" + getId() + ",password=" + getPassword() + ",roles="
				+ getRoles() + "}";
	}

	/**
	 * This method converts a set of Roles to a set of Strings.
	 */
	protected static Set<String> mapRolesToStrings(Set<Role> roles) {
		Set<String> result = new TreeSet<String>();
		for (Role r : roles) {
			result.add(r.getRole());
		}
		return result;
	}

	/**
	 * This method converts a set of Strings to a set of Roles.
	 */
	protected static Set<Role> mapStringsToRoles(Set<String> strings) {
		Set<Role> result = new HashSet<Role>();
		for (String s : strings) {
			result.add(new Role(s));
		}
		return result;
	}
}
