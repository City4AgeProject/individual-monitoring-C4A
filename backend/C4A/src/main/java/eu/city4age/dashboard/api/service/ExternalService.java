package eu.city4age.dashboard.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.dao.ExternalDAO;
import eu.city4age.dashboard.api.external.C4AGroupsResponse;
import eu.city4age.dashboard.api.external.C4ServiceGetOverallScoreListResponse;
import eu.city4age.dashboard.api.model.CdDetectionVariable;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;

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


}
