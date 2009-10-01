package authdb.ui;

import static authdb.ui.Constants.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;

import authdb.db.User;
import authdb.db.UserManager;

/**
 * This action adds a user to the model whose information is provided by the
 * incoming userForm bean.
 */
public class UserManagerAction extends MappingDispatchAction implements
		UserManagerAware {

	private static Logger log = Logger.getLogger(UserManagerAction.class);

	private UserManager userManager;

	public UserManagerAction() {
		Logger.getRootLogger().debug("new instance " + this);
	}

	public void setUserManager(UserManager userManager) {
		log.debug("set user manager to " + userManager);
		this.userManager = userManager;
	}

	protected UserManager getUserManager() {
		return userManager;
	}

	public ActionForward add(ActionMapping mapping, ActionForm userForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("add");
		// obtain arguments from form bean
		String id = BeanUtils.getProperty(userForm, PROPERTY_ID);
		String password = BeanUtils.getProperty(userForm, PROPERTY_PASSWORD);
		String roles = BeanUtils.getProperty(userForm, PROPERTY_ROLES);
		// interact with model
		User entry = getUserManager().create(id, password,
				Util.stringAsSet(roles));
		// set message and return result
		if (entry != null) {
			request.setAttribute(ATTRIBUTE_MESSAGE_KEY, "add.success");
		} else {
			request.setAttribute(ATTRIBUTE_MESSAGE_KEY, "add.failure");
		}
		return mapping.findForward(FORWARD_SUCCESS);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm userForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// obtain arguments from request
		String id = request.getParameter(PARAMETER_ID);
		log.debug("delete id = " + id);
		// interact with model
		getUserManager().remove(id);
		// set message and return result
		request.setAttribute(ATTRIBUTE_MESSAGE_KEY, "delete.message");
		return mapping.findForward(FORWARD_SUCCESS);
	}

	public ActionForward edit(ActionMapping mapping, ActionForm userForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("edit");
		// obtain arguments from form bean
		String id = BeanUtils.getProperty(userForm, PROPERTY_ID);
		String password = BeanUtils.getProperty(userForm, PROPERTY_PASSWORD);
		String roles = BeanUtils.getProperty(userForm, PROPERTY_ROLES);
		// interact with model
		getUserManager().update(id, password, Util.stringAsSet(roles));
		// set message and return result
		request.setAttribute(ATTRIBUTE_MESSAGE_KEY, "edit.message");
		return mapping.findForward(FORWARD_SUCCESS);
	}

	public ActionForward find(ActionMapping mapping, ActionForm userForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// obtain arguments from request
		String id = request.getParameter(PARAMETER_ID);
		log.debug("find id = " + id);
		// interact with model and populate form bean
		User entry = getUserManager().find(id);
		BeanUtils.setProperty(userForm, PROPERTY_ID, id);
		BeanUtils.setProperty(userForm, PROPERTY_PASSWORD, entry.getPassword());
		BeanUtils.setProperty(userForm, PROPERTY_ROLES, Util.setAsString(entry
				.getRoles()));
		// return result
		return mapping.findForward(FORWARD_SUCCESS);
	}

	public ActionForward init(ActionMapping mapping, ActionForm userForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("init");
		// interact with model
		getUserManager().init();
		// return result
		return mapping.findForward(FORWARD_SUCCESS);
	}

	public ActionForward list(ActionMapping mapping, ActionForm listForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("list");
		// interact with model
		List<User> users = new ArrayList<User>(getUserManager().findAll());
		// populate form bean
		BeanUtils.setProperty(listForm, PROPERTY_USERS, users);
		// return result
		return mapping.findForward(FORWARD_SUCCESS);
	}
}