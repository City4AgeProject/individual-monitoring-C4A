package eu.city4age.dashboard.api.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import io.swagger.annotations.ApiParam;

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(ViewEndpoint.PATH)
public class ViewEndpoint {

   public static final String PATH = "view";

   static protected Logger logger = LogManager.getLogger(ViewEndpoint.class);

   @Context
   private ContextResolver<ObjectMapper> mapperResolver;

   private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
   

   	@Autowired
   	private NUIRepository nuiRepository;

	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;
   	
   	@GET
   	@Produces(MediaType.APPLICATION_JSON)
   	@Path("getNuiValues/userInRoleId/{userInRoleId}/meaId/{meaId}")
   	public Response getNuiValuesMea(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
   			@ApiParam(hidden = true) @PathParam("meaId") Long meaId) throws JsonProcessingException {
   		
   		List<NumericIndicatorValue> nuis = nuiRepository.getNuisForSelectedMea(userInRoleId, meaId);
   		return JerseyResponse.build(objectMapper.writerWithView(View.NUIView.class).writeValueAsString(nuis));
   	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getDailyMeasures/userInRoleId/{userInRoleId}/meaId/{meaId}")
	public Response getDailyMeasuresMea(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @PathParam("meaId") Long meaId) throws JsonProcessingException {

		List<VariationMeasureValue> measures = new ArrayList<VariationMeasureValue>();
			measures = variationMeasureValueRepository.findByUserAndMea(userInRoleId, meaId);
		return JerseyResponse.build(objectMapper.writerWithView(View.VariationMeasureValueView.class).writeValueAsString(measures));

	}

}