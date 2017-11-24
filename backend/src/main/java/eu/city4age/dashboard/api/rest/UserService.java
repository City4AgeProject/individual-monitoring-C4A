package eu.city4age.dashboard.api.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.PilotRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.ws.C4ALoginResponse;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.security.JwtIssuer;

/**
 * @author EMantziou
 *
 */
@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(UserService.PATH)
public class UserService {
    
    public static final String PATH = "users";
    
    static protected Logger logger = LogManager.getLogger(UserService.class);
    
    @Autowired
    private UserInRoleRepository userInRoleRepository;
    
    @Autowired
    private PilotRepository pilotRepository;

    @GET
    @Path("login/username/{username}/password/{password}")
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    public Response login(@PathParam("username") String username, @PathParam("password") String password)
            throws IOException {
        C4ALoginResponse response = new C4ALoginResponse();
        Pilot userPilot;
        String displayName, pilotName, pilotCode;
        
        UserInRole user = userInRoleRepository.findBySystemUsernameAndPassword(username, password);

        // Wrong credentials
        if (user == null) {

            // build response
            response.setResponseCode(401);
            
            return JerseyResponse.build(response, 401);
        }

        // get pilot name & code
        pilotCode = user.getPilotCode();
        userPilot = pilotRepository.findOne(pilotCode);
        pilotName = userPilot.getName();
        
        displayName = user.getUserInSystem().getDisplayName();

        // create and sign JWT 
        String token = JwtIssuer.INSTANCE.createAndSign(user.getUserInSystem().getUsername(), user.getRoleId(), pilotCode);

        // build response
        response.setResponseCode(200);
        response.setJwToken(token);
        response.setDisplayName(displayName);
        response.setPilotName(pilotName);
        response.setUirId(user.getId());
        
        return JerseyResponse.build(response);

    }
    
}

