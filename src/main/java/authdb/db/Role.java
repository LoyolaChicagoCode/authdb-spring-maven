package authdb.db;

/**
 * A role object backed by Hibernate.
 */
class Role {

	public final String DEFAULT_ROLEGROUP = "Roles";

	private String role;

	private String roleGroup;

	public Role() {
	}

	public Role(String role) {
		this.role = role;
		this.roleGroup = DEFAULT_ROLEGROUP;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoleGroup() {
		return roleGroup;
	}

	public void setRoleGroup(String roleGroup) {
		this.roleGroup = roleGroup;
	}

	@Override
	public String toString() {
		return "{role=" + role + ",rolegroup=" + roleGroup + "}";
	}
}
