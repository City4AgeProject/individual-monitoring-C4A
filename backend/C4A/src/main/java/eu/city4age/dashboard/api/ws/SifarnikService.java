package eu.city4age.dashboard.api.ws;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
import eu.city4age.dashboard.api.domain.DataValidityStatus;
import eu.city4age.dashboard.api.domain.OrderBy;
import eu.city4age.dashboard.api.json.GetAllRolesWrapper;
import eu.city4age.dashboard.api.model.CdRiskStatus;
import eu.city4age.dashboard.api.model.CdRole;
import eu.city4age.dashboard.api.model.Stakeholder;

@Transactional("transactionManager")
@Path("/codeList")
public class SifarnikService {

    static protected Logger logger = Logger.getLogger(SifarnikService.class);
	
	@Autowired
	private RiskStatusDAO riskStatusDAO;
	
	@Autowired
	private StakeholderDao stakeholderDAO;
	
	@GET
    @Path("/getAllRiskStatus")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllRiskStatus() throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		List<CdRiskStatus> riskStatus = riskStatusDAO.getAllRiskStatus();
		
		String dtoAsString = objectMapper.writeValueAsString(riskStatus);
        
        return dtoAsString;
	}
	
	@GET
    @Path("/getAllDataValitidityStatus")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllDataValitidityStatus() throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Map<DataValidityStatus, String> statuses = new EnumMap<>(DataValidityStatus.class);

		statuses.put(DataValidityStatus.QUESTIONABLE_DATA,	"Questionable data");
		statuses.put(DataValidityStatus.FAULTY_DATA,  		"Faulty data");
		statuses.put(DataValidityStatus.VALID_DATA,      		"Valid data");

		String dtoAsString = objectMapper.writeValueAsString(statuses);
        
        return dtoAsString;
	}
	
	@GET
    @Path("/getAllOrderBy")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllOrderBy() throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Map<OrderBy, String> orders = new EnumMap<>(OrderBy.class);

		orders.put(OrderBy.DATE_ASC,				"Date Asc.");
		orders.put(OrderBy.DATE_DESC,  				"Date Desc.");
		orders.put(OrderBy.AUTHOR_NAME_ASC, 		"Author Name Asc.");
		orders.put(OrderBy.AUTHOR_NAME_DESC,		"Author Name Desc.");
		orders.put(OrderBy.AUTHOR_ROLE_ASC,  		"Author Name Asc.");
		orders.put(OrderBy.AUTHOR_ROLE_DESC, 		"Author Name Desc.");

		String dtoAsString = objectMapper.writeValueAsString(orders);
        
        return dtoAsString;
	}
	
	@GET
    @Path("/getAllStockholders")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllStockholders() throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		List<Stakeholder> stakeholders = stakeholderDAO.getAllStockholders();
		
		String dtoAsString = objectMapper.writeValueAsString(stakeholders);
        
        return dtoAsString;		
	}
	
	@POST
    @Path("/getAllRolesForStakeholderAbbr")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllRolesForStakeholderAbbr(String json) throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new Hibernate3Module());
		
    	ObjectReader objectReader = objectMapper.reader(GetAllRolesWrapper.class);
    	
    	GetAllRolesWrapper data = objectReader.readValue(json);
		
		List<CdRole> roles = stakeholderDAO.getAllRolesForStakeholderAbbr(data.getStakeholderAbbr());
		
		String dtoAsString = objectMapper.writeValueAsString(roles);
        
        return dtoAsString;		
	}


}
