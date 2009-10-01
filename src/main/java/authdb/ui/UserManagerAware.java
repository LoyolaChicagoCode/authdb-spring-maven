package authdb.ui;

import authdb.db.UserManager;

/**
 * This interface is implemented by classes whose instances depend on a user
 * manager.
 */
public interface UserManagerAware {

	/**
	 * Injects this object's dependency on the shared data.
	 * 
	 * @param sharedData
	 *            the shared data
	 */
	void setUserManager(UserManager userManager);
}
