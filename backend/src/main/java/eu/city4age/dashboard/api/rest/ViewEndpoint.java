package eu.city4age.dashboard.api.rest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.dto.Item;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.DataSet;
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
	
	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;

	@Autowired
	private NativeQueryRepository nativeQueryRepository;

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
   	
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

		List<VariationMeasureValue> measures = variationMeasureValueRepository.findByUserAndMea(userInRoleId, meaId);
		return JerseyResponse.build(objectMapper.writerWithView(View.VariationMeasureValueView.class).writeValueAsString(measures));

	}
	
	@GET
	@Path("getDiagramData/careRecipientId/{careRecipientId}")
	@Produces({MediaType.APPLICATION_JSON, "application/javascript"})
	public Response getDiagramData(@PathParam("careRecipientId") Long careRecipientId,
			@QueryParam("parentFactorId") Long parentFactorId) throws JsonProcessingException {

		DataSet response = new DataSet();

		List<ViewGefCalculatedInterpolatedPredictedValues> list;
		if(parentFactorId != null) {
			list = viewGefCalculatedInterpolatedPredictedValuesRepository.findByCareRecipientIdAndParentFactorIds(careRecipientId, Arrays.asList(parentFactorId));
		} else {
			list = viewGefCalculatedInterpolatedPredictedValuesRepository.findByCareRecipientId(careRecipientId);
		}

		if (list != null && !list.isEmpty()) {

			Set<DataIdValue> monthLabels = createMonthLabels(list);

			if(parentFactorId == null) {
				OJDiagramFrailtyStatus frailtyStatus = transformToDto(list, monthLabels);
				response.setFrailtyStatus(frailtyStatus);
			}

			response.getGroups().addAll(monthLabels);

			Set<DetectionVariable> dvs = new HashSet<DetectionVariable>();

			for (ViewGefCalculatedInterpolatedPredictedValues gef : list) {
				DetectionVariable dv = new DetectionVariable();
				dv.setId(gef.getId().getDetectionVariableId());
				dv.setDetectionVariableName(gef.getDetectionVariableName());
				dvs.add(dv);
			}

			for (DetectionVariable dv : dvs) {

				String detectionVariableName = dv.getDetectionVariableName();

				eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie series = new eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie(
						detectionVariableName);

				for (ViewGefCalculatedInterpolatedPredictedValues gef : list) {
					Boolean gefAdded = false;

					if (gefAdded != true && dv.getId().equals(gef.getId().getDetectionVariableId())) {
						series.getItems().add(new Item(gef.getId().getId(), gef.getGefValue(), gef.getId().getDataType(), gef.getId().getDetectionVariableId(), gef.getId().getTimeIntervalId()));
						gefAdded = true;
					}
				}
				((DataSet)response).getSeries().add(series);
			} 

		}

		return JerseyResponse.build(objectMapper.writeValueAsString(response));

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getDerivedMeasures/userInRoleId/{userInRoleId}/parentFactorId/{parentFactorId}")
	public Response getDerivedMeasures(@PathParam("userInRoleId") Long userInRoleId, @PathParam("parentFactorId") Long parentFactorId) throws JsonProcessingException {

		DataSet response = new DataSet();

		List<Object[]> derivedMeasures = nativeQueryRepository.computeDerivedMeasures(userInRoleId, parentFactorId);

		if (derivedMeasures != null && !derivedMeasures.isEmpty()) {

			for (Object[] derivedMeasure : derivedMeasures) {
				
				List<Item> items = new ArrayList<>();

				DataIdValue monthLabel = createMonthLabel(Long.valueOf((Integer) derivedMeasure[4]));

				response.getGroups().add(monthLabel);

				eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie serie = new eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie(detectionVariableRepository.findOne(Long.valueOf((Integer) derivedMeasure[1])).getDetectionVariableName());
				items.add(new Item((BigDecimal) derivedMeasure[5], Long.valueOf((Integer) derivedMeasure[1]), Long.valueOf((Integer) derivedMeasure[4])));
				serie.setItems(items);
				response.getSeries().add(serie);
			}

		}
		return JerseyResponse.build(objectMapper.writeValueAsString(response));
	}

	private DataIdValue createMonthLabel(Long timeIntervalId) {
		DataIdValue monthLabel = new DataIdValue();

		SimpleDateFormat formatWithTz = new SimpleDateFormat("yyyy/MM");

		formatWithTz.setTimeZone(TimeZone.getTimeZone("UTC"));

		monthLabel.setId(timeIntervalId);
		monthLabel.setName(formatWithTz.format(timeIntervalRepository.findOne(timeIntervalId).getIntervalStart()));

		return monthLabel;
	}

	private Set<DataIdValue> createMonthLabels(List<ViewGefCalculatedInterpolatedPredictedValues> gefs) {

		Set<DataIdValue> monthLabels = new HashSet<DataIdValue>();

		SimpleDateFormat formatWithTz = new SimpleDateFormat("yyyy/MM");

		formatWithTz.setTimeZone(TimeZone.getTimeZone("UTC"));

		for (ViewGefCalculatedInterpolatedPredictedValues gef: gefs) {
			monthLabels.add(new DataIdValue(gef.getId().getTimeIntervalId(), gef.getIntervalStartLabel()));
		}

		return monthLabels;
	}

	private OJDiagramFrailtyStatus transformToDto(List<ViewGefCalculatedInterpolatedPredictedValues> gefs, Set<DataIdValue> months) {
		OJDiagramFrailtyStatus dto = new OJDiagramFrailtyStatus();
		dto.setMonths(months);

		gefs.sort(null);

		eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie preFrail = new eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie("Pre-Frail", new ArrayList<BigDecimal>());
		eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie frail = new eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie("Frail", new ArrayList<BigDecimal>());
		eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie fit = new eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie("Fit", new ArrayList<BigDecimal>());

		String previous = "";
		for (ViewGefCalculatedInterpolatedPredictedValues gef : gefs) {

			boolean found = false;

			for (DataIdValue month : months) {

				if (gef.getFrailtyStatus() != null && !gef.getId().getDataType().equals("p") && month.getId().equals(gef.getId().getTimeIntervalId())) {

					found = true;

					switch (gef.getFrailtyStatus()) {

					case "pre_frail":
						previous = "pre_frail";
						preFrail.getItems().add(BigDecimal.valueOf(0.2));
						frail.getItems().add(null);
						fit.getItems().add(null);
						break;
					case "frail":
						previous = "frail";
						frail.getItems().add(BigDecimal.valueOf(0.2));
						preFrail.getItems().add(null);
						fit.getItems().add(null);
						break;
					case "fit":
						previous = "fit";
						fit.getItems().add(BigDecimal.valueOf(0.2));
						preFrail.getItems().add(null);
						frail.getItems().add(null);
						break;
					default:
						switch (previous) {
						case "pre_frail":
							previous = "pre_frail";
							preFrail.getItems().add(BigDecimal.valueOf(0.2));
							frail.getItems().add(null);
							fit.getItems().add(null);
							break;
						case "frail":
							previous = "frail";
							frail.getItems().add(BigDecimal.valueOf(0.2));
							preFrail.getItems().add(null);
							fit.getItems().add(null);
							break;
						case "fit":
							previous = "fit";
							fit.getItems().add(BigDecimal.valueOf(0.2));
							preFrail.getItems().add(null);
							frail.getItems().add(null);
							break;
						case "":
							previous = "fit";
							fit.getItems().add(BigDecimal.valueOf(0.2));
							preFrail.getItems().add(null);
							frail.getItems().add(null);
							break;
						}
					}					
				}

				if (!found && gef.getId().getDetectionVariableId().equals(501L) && !gef.getId().getDataType().equals("p") && month.getId().equals(gef.getId().getTimeIntervalId())) {

					switch (previous) {
					case "pre_frail":
						previous = "pre_frail";
						preFrail.getItems().add(BigDecimal.valueOf(0.2));
						frail.getItems().add(null);
						fit.getItems().add(null);
						break;
					case "frail":
						previous = "frail";
						frail.getItems().add(BigDecimal.valueOf(0.2));
						preFrail.getItems().add(null);
						fit.getItems().add(null);
						break;
					case "fit":
						previous = "fit";
						fit.getItems().add(BigDecimal.valueOf(0.2));
						preFrail.getItems().add(null);
						frail.getItems().add(null);
						break;
					case "":
						previous = "fit";
						fit.getItems().add(BigDecimal.valueOf(0.2));
						preFrail.getItems().add(null);
						frail.getItems().add(null);
						break;
					}
				}

			}

		}

		dto.getSeries().add(preFrail);
		dto.getSeries().add(frail);
		dto.getSeries().add(fit);

		return dto;		
	}

}