package authdb.db;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class implements a data access object (DAO) that accesses User objects
 * from the database using Hibernate. This DAO has to be context-aware for
 * access to the LocalSessionFactoryBean, which provides access to the hbm2ddl
 * methods for database schema creation etc.
 */
public class HibernateUserManager extends HibernateDaoSupport implements
		UserManager, ApplicationContextAware {

	private ApplicationContext context;

	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
	}

	private String sessionFactoryName;

	/**
	 * Provides this DAO with the name of the LocalSessionFactoryBean.
	 * 
	 * @param sessionFactoryName
	 *            the name of the LSFB
	 */
	public void setSessionFactoryName(String sessionFactoryName) {
		this.sessionFactoryName = sessionFactoryName;
	}

	/**
	 * Obtains the LSFB through the Spring context
	 */
	protected LocalSessionFactoryBean getLsfb() {
		return (LocalSessionFactoryBean) context.getBean("&"
				+ sessionFactoryName);
	}

	/**
	 * Initializes the DAO. This method is invoked automatically by Spring.
	 */
	@Override
	public void initDao() {
		try {
			// create the schema according to the Hibernate mapping
			// but only if it does not exist yet
			// otherwise an exception occurs and nothing happens
			getLsfb().createDatabaseSchema();
			create("nobody", "password", new String[] {});
			create("guest", "password", new String[] { "guests" });
			create("laufer", "password", new String[] { "guests", "users" });
			create("user", "password", new String[] { "guests", "users" });
			create("admin", "password", new String[] { "guests", "users",
					"administrators" });
		} catch (DataAccessException e) {
		}
	}

	/**
	 * Reinitializes the database. This method is invoked by the user.
	 */
	public void init() {
		// drop the schema in case it exists
		try {
			getLsfb().dropDatabaseSchema();
		} catch (DataAccessException e) {
		}
		// then recreate and populate the schema
		initDao();
	}

	protected void create(String id, String password, String[] roles) {
		create(id, password, new HashSet<String>(Arrays.asList(roles)));
	}

	public User create(final String id, final String password, final Set<String> roles) {
		User user = new Principal(id, password, roles);
		getHibernateTemplate().save(user);
		return user;
	}

	public User find(final String id) {
		return (User) getHibernateTemplate().get(Principal.class, id);
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends User> findAll() {
		return getHibernateTemplate().find(
				"select item from authdb.db.Principal item order by item.id");
	}

	public void remove(final String id) {
		Object user = getHibernateTemplate().get(Principal.class, id);
		getHibernateTemplate().delete(user);
	}

	public void update(String id, String password, Set<String> roles) {
		getHibernateTemplate().update(new Principal(id, password, roles));
	}
}