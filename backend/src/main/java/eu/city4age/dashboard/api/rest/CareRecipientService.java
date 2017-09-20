package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.CrProfileRepository;
import eu.city4age.dashboard.api.persist.FrailtyStatusTimelineRepository;
import eu.city4age.dashboard.api.persist.GeriatricFactorRepository;
import eu.city4age.dashboard.api.persist.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.persist.PilotRepository;
import eu.city4age.dashboard.api.persist.TimeIntervalRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.comparator.FSTComparator;
import eu.city4age.dashboard.api.pojo.domain.CrProfile;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.C4ACareRecipientListResponse;
import eu.city4age.dashboard.api.pojo.dto.C4ACareRecipientsResponse;
import eu.city4age.dashboard.api.pojo.dto.C4AGroupsResponse;
import eu.city4age.dashboard.api.pojo.dto.C4ServiceGetOverallScoreListResponse;
import eu.city4age.dashboard.api.pojo.dto.DataSet;
import eu.city4age.dashboard.api.pojo.dto.Group;
import eu.city4age.dashboard.api.pojo.dto.Item;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie;

/**
 *
 * @author EMantziou
 */
@Component(value = "wsService")
@Path(CareRecipientService.PATH)
public class CareRecipientService {

	public static final String PATH = "careRecipient";

	static protected Logger logger = LogManager.getLogger(CareRecipientService.class);

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Autowired
	private FrailtyStatusTimelineRepository frailtyStatusTimelineRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private CrProfileRepository crProfileRepository;

	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Transactional("transactionManager")
	@GET
	@Path("getGroups/careRecipientId/{careRecipientId}/parentFactors/{parentFactors : .+}")
	@Produces("application/json")
	public Response getJson(@PathParam("careRecipientId") String careRecipientId,
			@PathParam("parentFactors") List<PathSegment> parentFactorsPathSegment) throws IOException {

		/**
		 * ****************Variables*************
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		List<String> dateList = new ArrayList<String>();
		List<DataIdValue> months = new ArrayList<DataIdValue>();
		Map<Long, List<Float>> fMap = new HashMap<Long, List<Float>>();
		Map<Long, List<Long>> idMap = new HashMap<Long, List<Long>>();
		List<GeriatricFactorValue> gereatricfactparamsList = new ArrayList<GeriatricFactorValue>();
		List<DetectionVariable> detectionvarsparamsList = new ArrayList<DetectionVariable>();
		ArrayList<C4ServiceGetOverallScoreListResponse> itemList;
		C4AGroupsResponse response = new C4AGroupsResponse();
		List<DetectionVariableType.Type> parentFactors = new ArrayList<DetectionVariableType.Type>();

		/**
		 * ****************Action*************
		 */

		for (PathSegment parentFactor : parentFactorsPathSegment) {
			parentFactors.add(DetectionVariableType.Type.valueOf(parentFactor.toString()));
		}

		List<TimeInterval> tis = timeIntervalRepository.getGroups(Long.valueOf(careRecipientId), parentFactors);

		if(tis != null && tis.size() > 0) {
			for (GeriatricFactorValue gef : tis.get(0).getGeriatricFactorValue()) {
	
				if (gef.getDetectionVariable() != null) {
	
					detectionvarsparamsList.add(gef.getDetectionVariable());
					fMap.put(gef.getDetectionVariable().getId(), new ArrayList<Float>());
					idMap.put(gef.getDetectionVariable().getId(), new ArrayList<Long>());
	
				}
	
			}
		}

		for (TimeInterval ti : tis) {
			String date = sdf.format(ti.getIntervalStart());

			dateList.add(date);

			months.add(new DataIdValue(ti.getId(), date));

			for (GeriatricFactorValue gef : ti.getGeriatricFactorValue()) {
				for (DetectionVariable type : detectionvarsparamsList) {


					gereatricfactparamsList.add(gef);

					if (gef.getDetectionVariable() != null && gef.getDetectionVariable().equals(type)) {
						fMap.get(type.getId()).add(gef.getGefValue().floatValue());
						idMap.get(type.getId()).add(gef.getId());

					}

				}

			}

			itemList = new ArrayList<C4ServiceGetOverallScoreListResponse>();

			if (gereatricfactparamsList.isEmpty()) {

				response.setMessage("No factors for this group");
				response.setResponseCode(0);
				response.setCareRecipientName("");
				response.setItemList(null);

			} else {

				for (DetectionVariable type : detectionvarsparamsList) {

					response.setMessage("success");
					response.setResponseCode(10);
					response.setCareRecipientName(
							gereatricfactparamsList.get(0).getUserInRole().getUserInSystem().getUsername());

					List<FrailtyStatusTimeline> fs = frailtyStatusTimelineRepository.findByPeriodAndUserId(tis,
							gereatricfactparamsList.get(0).getUserInRole().getId());

					OJDiagramFrailtyStatus frailtyStatus = transformToDto(fs, months);

					response.setFrailtyStatus(frailtyStatus);
					
					logger.info("2 tis: " + tis);
					logger.info("2 pilottype: " + type);

					String pilotCode = gereatricfactparamsList.get(0).getUserInRole().getPilotCode();						
					PilotDetectionVariable pdv = pilotDetectionVariableRepository.findByDetectionVariableAndPilotCode(type.getId(), pilotCode);

					itemList.add(new C4ServiceGetOverallScoreListResponse(tis, fMap.get(type.getId()),
							idMap.get(type.getId()), dateList, type.getDetectionVariableName(),
							pdv != null
									? pdv.getDerivedDetectionVariable().getDetectionVariableName() : null,
							type.getId()));

				}

			} // detectionVariables loop
			response.setItemList(itemList);

		}

		return Response.ok(objectMapper.writeValueAsString(response)).build();

	}// end method

	@Transactional("transactionManager")
	@GET
	@Path("getCareRecipients/pilotCode/{pilotCode}/")
	@Produces("application/json")
	public Response getJson(@PathParam("pilotCode") String pilotCode) throws IOException {

		/**
		 * ****************Variables*************
		 */
		C4ACareRecipientsResponse response = new C4ACareRecipientsResponse();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		/**
		 * ****************Action*************
		 */
		List<UserInRole> userinroleparamsList;
		if (pilotCode.equals("DEV")) {
			userinroleparamsList = userInRoleRepository.findAll();
		} else {
			userinroleparamsList = userInRoleRepository.findByRoleIdAndPilotCode(Short.valueOf("1"),
					String.valueOf(pilotCode));
		}

		if (userinroleparamsList.isEmpty()) {
			response.setMessage("No users found");
			response.setResponseCode(0);
			return Response.ok(objectMapper.writeValueAsString(response)).build();
		} else {
			List<C4ACareRecipientListResponse> itemList = new ArrayList<C4ACareRecipientListResponse>();
			for (UserInRole user : userinroleparamsList) {
				response.setMessage("success");
				response.setResponseCode(10);

				int age = 0;

				if (user.getCrProfile() != null) {

					LocalDate birthDate = user.getCrProfile().getBirthDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate();
					age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());

				}

				// **************************************

				String frailtyStatus = "N/A";
				String frailtyNotice = "N/A";
				char attention = '\0';
				String textline = "N/A";
				char interventionstatus = '\0';
				String interventionDate = "N/A";
				String detectionStatus = "N/A";
				String detectionDate = "N/A";

				if (user.getCareProfile() != null) {
					attention = user.getCareProfile().getAttentionStatus();
					textline = user.getCareProfile().getIndividualSummary();
					interventionstatus = user.getCareProfile().getInterventionStatus();
					interventionDate = sdf.format(user.getCareProfile().getLastInterventionDate());
				}

				List<FrailtyStatusTimeline> frailtyparamsList = new ArrayList<FrailtyStatusTimeline>(
						user.getFrailtyStatusTimeline());

				if (frailtyparamsList != null && frailtyparamsList.size() > 0) {
					frailtyStatus = frailtyparamsList.get(0).getCdFrailtyStatus().getFrailtyStatus();
					frailtyNotice = frailtyparamsList.get(0).getFrailtyNotice();
				}

				Pilot userPilot = pilotRepository.findOne(user.getPilotCode());

				itemList.add(new C4ACareRecipientListResponse(user.getId(), age, frailtyStatus, frailtyNotice,
						attention, textline, interventionstatus, interventionDate, detectionStatus, detectionDate,
						userPilot.getPilotCode()));

			} // detectionVariables loop
			response.setItemList(itemList);

		} // end detectionVariables is empty

		return Response.ok(objectMapper.writeValueAsString(response)).build();

	}// end method

	@GET
	@Path("getDiagramData/careRecipientId/{careRecipientId}/parentFactorId/{parentFactorId}")
	@Produces("application/json")
	public C4AGroupsResponse getDiagramData(@PathParam("careRecipientId") Long careRecipientId,
			@PathParam("parentFactorId") Long parentFactorId) throws IOException {
		DataSet response = new DataSet();
		List<GeriatricFactorValue> gfvList;
		ArrayList<C4ServiceGetOverallScoreListResponse> itemList = new ArrayList<C4ServiceGetOverallScoreListResponse>();

		// we use list to avoid "not found" exception
		gfvList = geriatricFactorRepository.findByDetectionVariableId(parentFactorId, careRecipientId);

		//
		if (gfvList.isEmpty()) {
			response.setMessage("No factors for this group");
			response.setResponseCode(0);
		} else {
			response.setMessage("success");
			response.setResponseCode(10);

			for (GeriatricFactorValue gfv : gfvList) {
				Group g = findOrCreateGroup(response, gfv.getTimeInterval());
				if (g == null) {
					g = new Group();
					g.setId(gfv.getTimeInterval().getId());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
					g.setName(sdf.format(gfv.getTimeInterval().getIntervalStart()));
					response.getGroups().add(g);
				}
				eu.city4age.dashboard.api.pojo.dto.Serie s = findOrCreateSerie(response, gfv.getGefTypeId());
				if (s == null) {
					s = new eu.city4age.dashboard.api.pojo.dto.Serie();
					s.setName(gfv.getGefTypeId().getDetectionVariableName());
					response.getSeries().add(s);
				}

				Item i = findOrCreateItem(response, gfv);
				if (i == null) {
					i = new Item();
					i.setId(gfv.getId());
					i.setValue(gfv.getGefValue().floatValue());
					i.setGefTypeId(gfv.getGefTypeId().getId().intValue());
					s.getItems().add(i);
				}

			}

		}
		response.setItemList(itemList);
		return response;
	}// end method

	@GET
	@Path("findOne/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOne(@PathParam("id") Long id) throws JsonProcessingException {
		CrProfile crP = crProfileRepository.findOne(id);
		return Response.ok(objectMapper.writeValueAsString(crP)).build();
	}

	private OJDiagramFrailtyStatus transformToDto(List<FrailtyStatusTimeline> fs, List<DataIdValue> months) {
		OJDiagramFrailtyStatus dto = new OJDiagramFrailtyStatus();
		dto.setMonths(months);

		Collections.sort(fs, new FSTComparator());

		Serie preFrail = new Serie("Pre-Frail", new ArrayList<Double>());
		Serie frail = new Serie("Frail", new ArrayList<Double>());
		Serie fit = new Serie("Fit", new ArrayList<Double>());

		for (FrailtyStatusTimeline frailty : fs) {

			if (frailty != null && frailty.getCdFrailtyStatus() != null) {
				switch (frailty.getCdFrailtyStatus().getFrailtyStatus()) {

				case "Pre-frail":
					preFrail.getItems().add(0.1);
					frail.getItems().add(null);
					fit.getItems().add(null);
					break;
				case "Frail":
					frail.getItems().add(0.1);
					preFrail.getItems().add(null);
					fit.getItems().add(null);
					break;
				case "Fit":
					fit.getItems().add(0.1);
					preFrail.getItems().add(null);
					frail.getItems().add(null);
					break;
				default:
					preFrail.getItems().add(null);
					frail.getItems().add(null);
					fit.getItems().add(null);
					break;

				}
			}
		}

		dto.getSeries().add(preFrail);
		dto.getSeries().add(frail);
		dto.getSeries().add(fit);

		return dto;
	}

	private Group findOrCreateGroup(DataSet ds, TimeInterval ti) {

		for (Group g : ds.getGroups()) {
			if (g.getId().equals(ti.getId()))
				return g;
		}
		return null;
	}

	private eu.city4age.dashboard.api.pojo.dto.Serie findOrCreateSerie(DataSet ds, DetectionVariable gefTypeId) {
		for (eu.city4age.dashboard.api.pojo.dto.Serie s : ds.getSeries()) {
			if (s.getName().equals(gefTypeId.getDetectionVariableName())) {
				return s;
			}
		}
		return null;
	}

	private Item findOrCreateItem(DataSet ds, GeriatricFactorValue gfv) {
		for (eu.city4age.dashboard.api.pojo.dto.Serie s : ds.getSeries()) {
			for (Item i : s.getItems()) {
				if (i.getId().equals(gfv.getId()))
					return i;
			}
		}
		return null;
	}

}// end class
