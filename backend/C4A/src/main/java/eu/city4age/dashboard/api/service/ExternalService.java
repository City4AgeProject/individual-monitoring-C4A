package eu.city4age.dashboard.api.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.dao.ExternalDAO;
import eu.city4age.dashboard.api.external.C4ACareReceiverListResponse;
import eu.city4age.dashboard.api.external.C4ACareReceiversResponse;
import eu.city4age.dashboard.api.external.C4AGroupsResponse;
import eu.city4age.dashboard.api.external.C4ALoginResponse;
import eu.city4age.dashboard.api.external.C4ServiceGetOverallScoreListResponse;
import eu.city4age.dashboard.api.model.CareProfile;
import eu.city4age.dashboard.api.model.CdDetectionVariable;
import eu.city4age.dashboard.api.model.CrProfile;
import eu.city4age.dashboard.api.model.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.UserInRole;
import eu.city4age.dashboard.api.model.UserInSystem;

@Transactional("transactionManager")
@Path("careReceiversData")
public class ExternalService {

    static protected Logger logger = Logger.getLogger(ExternalService.class);
	
	@Autowired
	private ExternalDAO externalDAO;
	
    @GET
    @Path("/getGroups")
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(@QueryParam("careReceiverId") String careReceiverId, @QueryParam("parentFactorId") String parentFactorId) throws IOException {
        /**
         * ****************Variables*************
         */
        C4AGroupsResponse response = new C4AGroupsResponse();
        
        ObjectMapper objectMapper = new ObjectMapper();

        List<GeriatricFactorValue> gereatricfactparamsList = new ArrayList<GeriatricFactorValue>();
        List<CdDetectionVariable> detectionvarsparamsList = new ArrayList<CdDetectionVariable>();
        ArrayList<C4ServiceGetOverallScoreListResponse> itemList;

        /**
         * ****************Action*************
         */

        if (Integer.parseInt(parentFactorId) == -1) {
            List<String> parentFactors = Arrays.asList("OVL", "GFG");
            detectionvarsparamsList = externalDAO.getDetectionVariableForDetectionVariableType(parentFactors);

        } else {
        	String parentFactor = "GEF";
        	detectionvarsparamsList = externalDAO.getDetectionVariableForDetectionVariableType(parentFactor);

        }

        if (detectionvarsparamsList.isEmpty()) {
            response.setMessage("No detection variables found");
            response.setResponseCode(0);
            String dtoAsString = objectMapper.writeValueAsString(response);
            
            return dtoAsString;
        } else {
            itemList = new ArrayList<C4ServiceGetOverallScoreListResponse>();
            for (CdDetectionVariable types : detectionvarsparamsList) {

            	Long dvId = types.getId();
            	
            	//we use list to avoid "not found" exception
            	gereatricfactparamsList = externalDAO.getGeriatricFactorValueForDetectionVariableId(dvId, Long.valueOf(careReceiverId));

                if (gereatricfactparamsList.isEmpty()) {
                    response.setMessage("No factors for this group");
                    response.setResponseCode(0);
                    response.setCareReceiverName("");
                    response.setItemList(null);
                } else {

                    response.setMessage("success");
                    response.setResponseCode(10);

                    response.setCareReceiverName(externalDAO.getUserInSystemUsername(gereatricfactparamsList.get(0).getId()));
             
                    List<Long> timeintervalIds = new ArrayList<Long>();
                    
                    for(GeriatricFactorValue gef:gereatricfactparamsList) {
                    	timeintervalIds.add(gef.getTimeInterval().getId());
                    }
         
                    itemList.add(new C4ServiceGetOverallScoreListResponse(gereatricfactparamsList,
                    		externalDAO.getParentGroupName(gereatricfactparamsList.get(0).getId()),
                    		externalDAO.getFrailtyStatus(timeintervalIds, gereatricfactparamsList.get(0).getUserInRole().getId())));

                }

            }//detectionVariables loop        
            response.setItemList(itemList);
        }//end detectionVariables is empty
        
        String dtoAsString = objectMapper.writeValueAsString(response);
        
        return dtoAsString;

    }//end method

    @GET
    @Path("getCareReceivers")
    @Consumes("application/json")
    @Produces("application/json")
    public C4ACareReceiversResponse getJson() throws IOException {
        /**
         * ****************Variables*************
         */
        System.out.println("******************start*****************");
        C4ACareReceiversResponse response = new C4ACareReceiversResponse();

        List<UserInRole> userinroleparamsList;
        List<CrProfile> crprofileparamsList;
        List<CareProfile> careprofileparamsList;
        List<FrailtyStatusTimeline> frailtyparamsList;
        ArrayList<C4ACareReceiverListResponse> itemList;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        /**
         * ****************Action*************
         */

        userinroleparamsList = externalDAO.getUserInRoleByRoleId(1L);

        if (userinroleparamsList.isEmpty()) {
            response.setMessage("No users found");
            response.setResponseCode(0);
            return response;
        } else {
            itemList = new ArrayList<C4ACareReceiverListResponse>();
            for (UserInRole users : userinroleparamsList) {
                response.setMessage("success");
                response.setResponseCode(10);
                System.out.println("id " + users.getId()
                        + "name " + users.getUserInSystem().getUsername());

                //we use list to avoid "not found" exception
                crprofileparamsList = externalDAO.getProfileByUserInRoleId(users.getId());
                int age = 0;
                if (!crprofileparamsList.isEmpty()) {

                    LocalDate birthDate = new LocalDate(crprofileparamsList.get(0).getBirthDate());
                    Years age2 = Years.yearsBetween(birthDate, new LocalDate());
                    age = age2.getYears();
                }


                //we use list to avoid "not found" exception
                careprofileparamsList = externalDAO.getCareProfileByUserInRoleId(users.getId());
                //**************************************
                String frailtyStatus = null;
                String frailtyNotice = null;
                char attention = 0;
                String textline = null;
                char interventionstatus = 0;
                String interventionDate = null;
                String detectionStatus = null;
                String detectionDate = null;
                if (!careprofileparamsList.isEmpty()) {
                    attention = careprofileparamsList.get(0).getAttentionStatus();
                    textline = careprofileparamsList.get(0).getIndividualSummary();
                    interventionstatus = careprofileparamsList.get(0).getInterventionStatus();
                    interventionDate = sdf.format(careprofileparamsList.get(0).getLastInterventionDate());
                }

                frailtyparamsList = externalDAO.getFrailtyStatusByUserInRoleId(users.getId());
                if (!frailtyparamsList.isEmpty()) {
                    frailtyStatus = frailtyparamsList.get(0).getCdFrailtyStatus().getFrailtyStatus();
                    frailtyNotice = frailtyparamsList.get(0).getFrailtyNotice();
                }

                itemList.add(new C4ACareReceiverListResponse(users.getId(), age, frailtyStatus, frailtyNotice, attention, textline,
                        interventionstatus, interventionDate, detectionStatus, detectionDate));
            }//detectionVariables loop    
            response.setItemList(itemList);

        }//end detectionVariables is empty

        return response;

    }//end method

    @GET
    @Path("login")
    @Consumes("application/json")
    @Produces("application/json")
    public C4ALoginResponse login(@QueryParam("username") String username, @QueryParam("password") String password) throws IOException {
        /**
         * ****************Variables*************
         */
        UserInSystem user;
        C4ALoginResponse response = new C4ALoginResponse();
        /**
         * ****************Action*************
         */
        try {

        	user = externalDAO.getUserInSystem(username, password);

            if (user == null) {
                response.setMessage("wrong credentials");
                response.setResponseCode(0);
                response.setDisplayName("");
                return response;
            } else {
                UserInRole userInRole = externalDAO.getUserInRoleByUserInSystemId(user.getId());
                if (userInRole.getRoleId().equals(Short.valueOf("8"))) {
                    System.out.println("username " + user.getUsername() + " display name " + user.getDisplayName());
                    response.setMessage("success");
                    response.setResponseCode(10);
                    if (user.getDisplayName() != null) {
                        response.setDisplayName(user.getDisplayName());
                    } else {

                        response.setDisplayName("");
                    }
                    return response;
                } else {
                    response.setMessage("you don't have the right permissions");
                    response.setResponseCode(0);
                    response.setDisplayName("");
                    return response;
                }

            }

        } catch (Exception e) {
            response.setMessage("something went terrible wrong");
            response.setResponseCode(2);
            response.setDisplayName("");
            return response;
        }

    }
}//end class
