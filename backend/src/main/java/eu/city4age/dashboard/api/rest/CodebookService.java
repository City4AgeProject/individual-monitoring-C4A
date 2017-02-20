package eu.city4age.dashboard.api.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.RiskStatusRepository;
import eu.city4age.dashboard.api.persist.RoleRepository;
import eu.city4age.dashboard.api.persist.StakeholderRepository;
import eu.city4age.dashboard.api.pojo.domain.RiskStatus;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.Stakeholder;

@Transactional("transactionManager")
@Path(CodebookService.PATH)
public class CodebookService {

	public static final String PATH = "codebook";

	static protected Logger logger = LogManager.getLogger(CodebookService.class);

	@Autowired
	private RiskStatusRepository riskStatusRepository;

	@Autowired
	private StakeholderRepository stakeholderRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SessionFactory sessionFactory;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@GET
	@Path("getAllRiskStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllRiskStatus() throws Exception {

		List<RiskStatus> riskStatus = riskStatusRepository.findAll();

		return Response.ok(objectMapper.writeValueAsString(riskStatus)).build();
	}

	@GET
	@Path("getAllStockholders")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllStockholders() throws Exception {

		List<Stakeholder> stakeholders = stakeholderRepository.findAll();

		return Response.ok(objectMapper.writeValueAsString(stakeholders)).build();
	}

	@GET
	@Path("getAllRolesForStakeholderAbbr/{stakeholderAbbr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllRolesForStakeholderAbbr(@PathParam(value = "stakeholderAbbr") String stakeholderAbbr)
			throws JsonProcessingException {
		List<Role> roles = roleRepository.findByStakeholderAbbreviation(stakeholderAbbr);

		return Response.ok(objectMapper.writeValueAsString(roles)).build();
	}

	@GET
	@Path("selectTable/{tableName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectTable(@PathParam(value = "tableName") String tableName) throws JsonProcessingException {
		Session session = sessionFactory.openSession();
		Query q = session.createSQLQuery("SELECT * from " + tableName);
		return Response.ok(objectMapper.writeValueAsString(q.list())).build();
	}

}