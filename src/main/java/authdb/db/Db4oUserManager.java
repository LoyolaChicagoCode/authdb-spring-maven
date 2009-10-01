package authdb.db;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

/**
 * This class implements a data access object (DAO) that stores User objects
 * from the database using Db4o.
 */
public class Db4oUserManager implements UserManager {

	private ObjectContainer container;

	/**
	 * Returns the database container.
	 *
	 * @return the database container.
	 */
	protected ObjectContainer getContainer() {
		return container;
	}

	/**
	 * Injects the database container.
	 *
	 * @param container
	 *            the database container.
	 */
	public void setContainer(ObjectContainer container) {
		this.container = container;
	}

	/**
	 * Initializes the DAO. This method is invoked automatically by Spring.
	 */
	public void initDao() {
		// create the schema according to the Hibernate mapping
		// but only if it does not exist yet
		// otherwise an exception occurs and nothing happens
		create("nobody", "password", new String[] {});
		create("guest", "password", new String[] { "guests" });
		create("laufer", "password", new String[] { "guests", "users" });
		create("user", "password", new String[] { "guests", "users" });
		create("admin", "password", new String[] { "guests", "users",
				"administrators" });
	}

	/**
	 * Reinitializes the database. This method is invoked by the user.
	 */
	@Override
	public void init() {
		// delete all existing users
		for (final User u : findAll())
			getContainer().delete(u);
		// then recreate and populate the schema
		initDao();
	}

	protected void create(String id, String password, String[] roles) {
		create(id, password, new HashSet<String>(Arrays.asList(roles)));
	}

	@Override
	public User create(final String id, final String password,
			final Set<String> roles) {
		final User user = new Principal(id, password, roles);
		getContainer().set(user);
		return user;
	}

	@Override
	public User find(final String id) {
		User result = null;
		// Create the query predicate
		final Predicate<User> predicate = new Predicate<User>() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean match(final User candidate) {
				return id.equals(candidate.getId());
			}
		};
		// Query the database and get the first result
		final List<User> users = getContainer().query(predicate);
		if ((users != null) && (users.size() > 0)) {
			result = users.get(0);
		}
		return result;
	}

	@Override
	public Collection<? extends User> findAll() {
		return getContainer().query(User.class);
	}

	@Override
	public void remove(final String id) {
		getContainer().delete(find(id));
	}

	@Override
	public void update(final String id, final String password, final Set<String> roles) {
		final Principal user = (Principal) find(id);
		user.setPassword(password);
		user.setRoles(roles);
		getContainer().set(user);
	}
}