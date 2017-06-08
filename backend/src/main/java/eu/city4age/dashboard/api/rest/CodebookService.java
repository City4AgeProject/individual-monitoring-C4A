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
import eu.city4age.dashboard.api.pojo.domain.AbstractBaseEntity;
import eu.city4age.dashboard.api.pojo.domain.RiskStatus;
import eu.city4age.dashboard.api.pojo.domain.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author milos.holclajtner
 *
 */
@Transactional("transactionManager")
@Path(CodebookService.PATH)
@Api(value = "codebook", produces = "application/json")
public class CodebookService {

	public static final String PATH = "codebook";

	static protected Logger logger = LogManager.getLogger(CodebookService.class);

	@Autowired
	private RiskStatusRepository riskStatusRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SessionFactory sessionFactory;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@GET
	@ApiOperation("Get all risk status.")
	@Path("getAllRiskStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = RiskStatus.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response getAllRiskStatus() throws Exception {

		List<RiskStatus> riskStatus = riskStatusRepository.findAll();

		return Response.ok(objectMapper.writeValueAsString(riskStatus)).build();
	}

	@GET
	@ApiOperation("Get all rolles for specific stakeholder.")
	@Path("getAllRolesForStakeholderAbbr/{stakeholderAbbr}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "stakeholderAbbr", value = "stakeholders abbreviation", required = false, dataType = "string", paramType = "path", defaultValue = "GES")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Role.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response getAllRolesForStakeholderAbbr(@ApiParam(hidden = true) @PathParam(value = "stakeholderAbbr") String stakeholderAbbr)
			throws JsonProcessingException {
		List<Role> roles = roleRepository.findByStakeholderAbbreviation(stakeholderAbbr);

		return Response.ok(objectMapper.writeValueAsString(roles)).build();
	}

	@GET
	@ApiOperation("Get all data from specified table.")
	@Path("selectTable/{tableName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tableName", value = "table name", required = false, dataType = "string", paramType = "path", defaultValue = "assessment")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = AbstractBaseEntity.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response selectTable(@ApiParam(hidden = true) @PathParam(value = "tableName") String tableName) throws JsonProcessingException {
		Session session = sessionFactory.openSession();
		Query q = session.createSQLQuery("SELECT * from " + tableName);
		return Response.ok(objectMapper.writeValueAsString(q.list())).build();
	}

}
