package authdb.db;

import java.util.Collection;
import java.util.Set;

/**
 * This interface represents a data access object (DAO) for for managing a
 * collection of user entities. This interface is similar to the home interface
 * of an EJB.
 */
public interface UserManager {
	/**
	 * Reinitializes the database tables.
	 */
	void init();

	/**
	 * Creates a user object with the given fields.
	 */
	User create(String id, String password, Set<String> roles);

	/**
	 * Finds a user object with the given unique id.
	 */
	User find(String id);

	/**
	 * Returns all user objects.
	 */
	Collection<? extends User> findAll();

	/**
	 * Removes the user object with the given unique id.
	 */
	void remove(String id);

	/**
	 * Updates the user object with the given unique key.
	 */
	void update(String id, String password, Set<String> roles);
}