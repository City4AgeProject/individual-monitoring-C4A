package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.CrProfileRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.CrProfile;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.enu.AllPilotRoles;
import eu.city4age.dashboard.api.pojo.enu.SamePilotRoles;
import eu.city4age.dashboard.api.pojo.ws.C4ACareRecipientListResponse;
import eu.city4age.dashboard.api.pojo.ws.C4ACareRecipientsResponse;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.security.JwtIssuer;

/**
 *
 * @author EMantziou
 */
@Component(value = "wsService")
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(CareRecipientEndpoint.PATH)
public class CareRecipientEndpoint {

	public static final String PATH = "careRecipient";

	static protected Logger logger = LogManager.getLogger(CareRecipientEndpoint.class);

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private CrProfileRepository crProfileRepository;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

    @GET
    @Path("getCareRecipients")
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    public Response getCareRecipients(@HeaderParam("Authorization") String jwt) throws IOException {
        C4ACareRecipientsResponse response = new C4ACareRecipientsResponse();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<UserInRole> userinroleparamsList = null;
        Short defaultRoleId = Short.valueOf("1");

        // Verify and decode JWT token
        DecodedJWT token;
        try {
            token = JwtIssuer.INSTANCE.verify(jwt);
        } catch (JWTVerificationException e) {
			e.printStackTrace();
			response.getStatus().setResponseCode("402 - JWT NOT VALID.");
			String status = "No valid Jwt used for service authorisation.";
			response.getStatus().setConsole(status);
			response.getStatus().setCause(e.getMessage());
			response.getStatus().setStackTrace(Arrays.toString(e.getStackTrace()));
            return JerseyResponse.buildTextPlain("402");
        }
        Map<String, Claim> claims = token.getClaims();
        Integer role = claims.get("rol").asInt();
        String stirngPilotCode = claims.get("plt").asString();
        Pilot.PilotCode pilotCode = Pilot.PilotCode.valueOf(stirngPilotCode);

        // 
        if (SamePilotRoles.getEnumAsSet().contains(role)) {
            userinroleparamsList = userInRoleRepository.findByRoleIdAndPilotCode(defaultRoleId, pilotCode);
        } else if (AllPilotRoles.getEnumAsSet().contains(role)) {

            userinroleparamsList = userInRoleRepository.findByRoleId(defaultRoleId);
        }
		

        if (userinroleparamsList.isEmpty()) {
            response.setMessage("No users found");
            response.setResponseCode(0);
            return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response));
        } else {
        	userinroleparamsList.sort(Comparator.comparing(UserInRole::getId));
            List<C4ACareRecipientListResponse> itemList = new ArrayList<C4ACareRecipientListResponse>();
            for (UserInRole user : userinroleparamsList) {
                response.setMessage("success");
                response.setResponseCode(10);

                Integer age = 0;
                String gender="";

                if (user.getCrProfile() != null) {

                    LocalDate birthDate = user.getCrProfile().getBirthDate().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
                    gender = user.getCrProfile().isGender()?"male":"female";

                }

                // **************************************
                String frailtyStatus = "N/A";
                String frailtyNotice = "N/A";
                Character attention = '\0';
                String textline = "N/A";
                Character interventionstatus = '\0';
                String interventionDate = "N/A";
                String detectionStatus = "N/A";
                String detectionDate = "N/A";
                Integer interventions = 0;

                if (user.getCareProfile() != null) {
                    attention = user.getCareProfile().getAttentionStatus();
                    textline = user.getCareProfile().getIndividualSummary();
                    interventionstatus = user.getCareProfile().getInterventionStatus();
                    interventions = user.getCareProfile().getInterventions();
                    if(user.getCareProfile().getLastInterventionDate() != null)
                    	interventionDate = sdf.format(user.getCareProfile().getLastInterventionDate());
                }

                List<FrailtyStatusTimeline> frailtyparamsList = new ArrayList<FrailtyStatusTimeline>(user.getFrailtyStatusTimeline());

                if (frailtyparamsList != null && frailtyparamsList.size() > 0) {
					FrailtyStatusTimeline max = frailtyparamsList.stream().max(Comparator.comparing(p-> p.getTimeInterval().getIntervalStart(), Comparator.naturalOrder())).get();
					frailtyStatus = max.getCdFrailtyStatus().getFrailtyStatus();
					frailtyNotice = max.getFrailtyNotice();
				}

                itemList.add(new C4ACareRecipientListResponse(user.getId(), age, frailtyStatus, frailtyNotice,
                        attention, textline, interventionstatus, interventionDate, detectionStatus, detectionDate, user.getPilotCode(), gender, interventions));
            } // detectionVariables loop
            response.setItemList(itemList);

		} // end detectionVariables is empty

		return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response));

	}// end method
    
	@GET
	@Path("getCareRecipientPilotLocalData/{careRecipientId}")
	@Produces({MediaType.APPLICATION_JSON, "application/javascript"})
	public String getRemoteCareRecipientData(@PathParam("careRecipientId") Long careRecipientId) {
		
		// TODO: Get the pilot's baseurl from the Pilot Configuration Json
		//final String uri = "http://c4amobile.atc.gr/backend/cityForAgeServices2/platform/routes/getCareRecipientData/" + careRecipientId.toString();
		
		try {
			String uri = userInRoleRepository.findOne(careRecipientId).getPilot().getPersonalProfileDataUrl() + careRecipientId.toString();

			RestTemplate restTemplate;
			
			if(uri.startsWith("https")){
			    
			  TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
		        public boolean isTrusted(X509Certificate[] x509Certificates, String s)
		                        throws CertificateException {
		            return true;
		        }
		      };
			  
		      SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
		          .loadTrustMaterial(null, acceptingTrustStrategy)
		          .build();

    		  SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
    
    		  CloseableHttpClient httpClient = HttpClients.custom()
    		          .setSSLSocketFactory(csf)
    		          .build();
    
    		  HttpComponentsClientHttpRequestFactory requestFactory =
    		          new HttpComponentsClientHttpRequestFactory();

    		  requestFactory.setHttpClient(httpClient);
			  
			  restTemplate = new RestTemplate(requestFactory);
			    
			}else{
			  restTemplate = new RestTemplate();
			}
			
		    String result = restTemplate.getForObject(uri, String.class);
		    return result;
		    
		} catch (Exception e) {
		    logger.error("Error receiving local pilot data!",e);
			return "";
		}	    
	}

	@GET
	@Path("findOne/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOne(@PathParam("id") Long id) throws JsonProcessingException {
		CrProfile crP = crProfileRepository.findOne(id);
		return JerseyResponse.build(objectMapper.writeValueAsString(crP));
	}

}// end class
