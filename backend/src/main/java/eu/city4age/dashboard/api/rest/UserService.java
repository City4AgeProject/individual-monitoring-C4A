package eu.city4age.dashboard.api.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.C4ALoginResponse;

/**
 * @author EMantziou
 *
 */
@Component
@Transactional("transactionManager")
@Path(UserService.PATH)
public class UserService {

	public static final String PATH = "users";

	static protected Logger logger = LogManager.getLogger(UserService.class);

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Transactional("transactionManager")
	@GET
	@Path("login/username/{username}/password/{password}")
	@Produces("application/json")
	public Response login(@PathParam("username") String username, @PathParam("password") String password)
			throws IOException {
		/**
		 * ****************Variables*************
		 */
		UserInRole user;
		C4ALoginResponse response = new C4ALoginResponse();
		/**
		 * ****************Action*************
		 */
		try {
			logger.info("Logging in...");
			user = userInRoleRepository.findBySystemUsernameAndPassword(username, password);

			if (user == null) {
				response.setMessage("wrong credentials");
				response.setResponseCode(0);
				response.setDisplayName("");
				return Response.ok(response).build();
			} else {
				if (user.getRoleId().equals(Short.valueOf("8"))) {
					response.setMessage("success");
					response.setResponseCode(10);
					if (user.getUserInSystem().getDisplayName() != null) {
						response.setDisplayName(user.getUserInSystem().getDisplayName());
					} else {
						response.setDisplayName("");
					}
					return Response.ok(response).build();
				} else {
					response.setMessage("you don't have the right permissions");
					response.setResponseCode(0);
					response.setDisplayName("");
					return Response.ok(response).build();
				}
			}

		} catch (Exception e) {
			response.setMessage("something went terrible wrong: " + e.getMessage());
			response.setResponseCode(2);
			response.setDisplayName("");
			return Response.ok(response).build();
		}
	}

}

