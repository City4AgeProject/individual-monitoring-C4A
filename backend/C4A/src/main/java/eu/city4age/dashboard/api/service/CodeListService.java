package eu.city4age.dashboard.api.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.hibernate3.Hibernate3Module;

import eu.city4age.dashboard.api.dao.RiskStatusDAO;
import eu.city4age.dashboard.api.dao.StakeholderDao;
import eu.city4age.dashboard.api.json.GetAllRolesWrapper;
import eu.city4age.dashboard.api.model.CdRiskStatus;
import eu.city4age.dashboard.api.model.CdRole;
import eu.city4age.dashboard.api.model.Stakeholder;

@Transactional("transactionManager")
@Path("/codeList")
public class CodeListService {

    static protected Logger logger = Logger.getLogger(CodeListService.class);
	
	@Autowired
	private RiskStatusDAO riskStatusDAO;
	
	@Autowired
	private StakeholderDao stakeholderDAO;
	
	private static final CustomObjectMapper objectMapper = new CustomObjectMapper();
	
	@GET
    @Path("/getAllRiskStatus")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllRiskStatus() throws Exception {

		List<CdRiskStatus> riskStatus = riskStatusDAO.getAllRiskStatus();
		
		String dtoAsString = objectMapper.writeValueAsString(riskStatus);
        
        return dtoAsString;
	}
	
	@GET
    @Path("/getAllStockholders")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllStockholders() throws Exception {

		List<Stakeholder> stakeholders = stakeholderDAO.getAllStockholders();
		
		String dtoAsString = objectMapper.writeValueAsString(stakeholders);
        
        return dtoAsString;		
	}
	
	@POST
    @Path("/getAllRolesForStakeholderAbbr")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllRolesForStakeholderAbbr(String json) throws Exception {
		
		objectMapper.registerModule(new Hibernate3Module());
		
    	ObjectReader objectReader = objectMapper.reader(GetAllRolesWrapper.class);
    	
    	GetAllRolesWrapper data = objectReader.readValue(json);
		
		List<CdRole> roles = stakeholderDAO.getAllRolesForStakeholderAbbr(data.getStakeholderAbbr());
		
		String dtoAsString = objectMapper.writeValueAsString(roles);
        
        return dtoAsString;		
	}


}
