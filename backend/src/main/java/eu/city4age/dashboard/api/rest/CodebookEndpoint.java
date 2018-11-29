package eu.city4age.dashboard.api.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.FilterTypeRepository;
import eu.city4age.dashboard.api.jpa.RiskStatusRepository;
import eu.city4age.dashboard.api.jpa.RoleRepository;
import eu.city4age.dashboard.api.pojo.domain.FilterType;
import eu.city4age.dashboard.api.pojo.domain.RiskStatus;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
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
@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(CodebookEndpoint.PATH)
@Api(value = "codebook", produces = "application/json")
public class CodebookEndpoint {

	public static final String PATH = "codebook";

	static protected Logger logger = LogManager.getLogger(CodebookEndpoint.class);

	@Autowired
	private RiskStatusRepository riskStatusRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private FilterTypeRepository filterTypeRepository;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@GET
	@ApiOperation("Get all risk status.")
	@Path("getAllRiskStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = RiskStatus.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response getAllRiskStatus() throws Exception {

		List<RiskStatus> riskStatus = riskStatusRepository.findAll();

		return JerseyResponse.build(objectMapper.writeValueAsString(riskStatus));
	}

	@GET
	@ApiOperation("Get all rolles for specific stakeholder.")
	@Path("getAllRolesForStakeholderAbbr/{stakeholderAbbr : .+}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "stakeholderAbbr", value = "stakeholders abbreviation", required = false, dataType = "string", paramType = "path", defaultValue = "GES")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Role.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response getAllRolesForStakeholderAbbr(@ApiParam(hidden = true) @PathParam(value = "stakeholderAbbr") List<PathSegment> stakeholderAbbr)
			throws JsonProcessingException {
		
		List<String> stakeholderAbbrs = new ArrayList<String>();
		for (PathSegment ps : stakeholderAbbr) stakeholderAbbrs.add(ps.getPath());
		List<Role> roles = roleRepository.findByStakeholderAbbreviation(stakeholderAbbrs);

		return JerseyResponse.build(objectMapper.writeValueAsString(roles));
	}
	
	@GET	
	@Path("filterTypes")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response filterTypes() throws Exception {
		
		List<FilterType> filterTypes = filterTypeRepository.findAll();

		return JerseyResponse.build(objectMapper.writeValueAsString(filterTypes));
	}


}
