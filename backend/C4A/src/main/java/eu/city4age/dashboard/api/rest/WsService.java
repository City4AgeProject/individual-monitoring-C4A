package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.DetectionVariableRepository;
import eu.city4age.dashboard.api.persist.FrailtyStatusTimelineRepository;
import eu.city4age.dashboard.api.persist.GeriatricFactorRepository;
import eu.city4age.dashboard.api.persist.TimeIntervalRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.C4ACareReceiverListResponse;
import eu.city4age.dashboard.api.pojo.dto.C4ACareReceiversResponse;
import eu.city4age.dashboard.api.pojo.dto.C4AGroupsResponse;
import eu.city4age.dashboard.api.pojo.dto.C4ALoginResponse;
import eu.city4age.dashboard.api.pojo.dto.C4ServiceGetOverallScoreListResponse;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie;

/**
 *
 * @author EMantziou
 */
@Component(value = "wsService")
@Path(WsService.PATH)
public class WsService {

	public static final String PATH = "careReceiversData";

	static protected Logger logger = Logger.getLogger(WsService.class);

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Autowired
	private FrailtyStatusTimelineRepository frailtyStatusTimelineRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Transactional("transactionManager")
	@GET
	@Path("/getGroups/careReceiverId/{careReceiverId}/parentFactors/{parentFactors : .+}")
	@Produces("application/json")
	public Response getJson(@PathParam("careReceiverId") String careReceiverId,
			@PathParam("parentFactors") List<PathSegment> parentFactors) throws IOException {

		/**
		 * ****************Variables*************
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		List<String> dateList = new ArrayList<String>();
		List<DataIdValue> months = new ArrayList<DataIdValue>();
		List<Float> ItemList = new ArrayList<Float>();
		List<Long> idList = new ArrayList<Long>();
		String groupName = "";
		List<GeriatricFactorValue> gereatricfactparamsList = new ArrayList<GeriatricFactorValue>();
		// List<DetectionVariable> detectionvarsparamsList = new
		// ArrayList<DetectionVariable>();
		ArrayList<C4ServiceGetOverallScoreListResponse> itemList;
		C4AGroupsResponse response = new C4AGroupsResponse();
		List<String> pfs = new ArrayList<String>();

		/**
		 * ****************Action*************
		 */

		for (PathSegment pf : parentFactors) {
			pfs.add(pf.toString());
		}

		List<TimeInterval> tis = timeIntervalRepository.getGroups(Long.valueOf(careReceiverId), pfs);

		for (TimeInterval ti : tis) {
			ItemList.add(ti.getGeriatricFactorValue().iterator().next().getGefValue().floatValue());
			idList.add(ti.getGeriatricFactorValue().iterator().next().getId());
			String date = sdf.format(ti.getIntervalStart());
			dateList.add(date);
			months.add(new DataIdValue(ti.getId(), date));

			for (GeriatricFactorValue gef : ti.getGeriatricFactorValue()) {
				gereatricfactparamsList.add(gef);
				// detectionvarsparamsList.add(gef.getCdDetectionVariable());
			}
		}

		// detectionvarsparamsList =
		// detectionVariableRepository.findByDetectionVariableTypes(pfs);

		if (gereatricfactparamsList.isEmpty()) {
			response.setMessage("No detection variables found");
			response.setResponseCode(0);
			return Response.ok(objectMapper.writeValueAsString(response)).build();
		} else {
			itemList = new ArrayList<C4ServiceGetOverallScoreListResponse>();
			// for (DetectionVariable types : detectionvarsparamsList) {

			/*
			 * Long dvId = types.getId(); gereatricfactparamsList =
			 * geriatricFactorRepository.findByDetectionVariableId(dvId,
			 * Long.valueOf(careReceiverId));
			 */
			/*
			 * if (gereatricfactparamsList.isEmpty()) {
			 * 
			 * response.setMessage("No factors for this group");
			 * response.setResponseCode(0); response.setCareReceiverName("");
			 * response.setItemList(null);
			 * 
			 * } else {
			 */

			response.setMessage("success");
			response.setResponseCode(10);
			response.setCareReceiverName(
					gereatricfactparamsList.get(0).getUserInRole().getUserInSystem().getUsername());

			List<FrailtyStatusTimeline> fs = frailtyStatusTimelineRepository.findByPeriodAndUserId(tis,
					gereatricfactparamsList.get(0).getUserInRole().getId());

			OJDiagramFrailtyStatus frailtyStatus = transformToDto(fs, months);

			response.setFrailtyStatus(frailtyStatus);

			String parentGroupName = "";
			if (gereatricfactparamsList.get(0).getGefTypeId().getDerivedDetectionVariable() != null) {
				parentGroupName = gereatricfactparamsList.get(0).getGefTypeId().getDerivedDetectionVariable()
						.getDetectionVariableName();
			}

			itemList.add(new C4ServiceGetOverallScoreListResponse(tis, ItemList, idList, dateList, groupName,
					parentGroupName));

			// }

			// } // detectionVariables loop
			response.setItemList(itemList);
		} // end detectionVariables is empty

		return Response.ok(objectMapper.writeValueAsString(response)).build();

	}// end method

	@Transactional("transactionManager")
	@GET
	@Path("getCareReceivers")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getJson() throws IOException {
		/**
		 * ****************Variables*************
		 */
		C4ACareReceiversResponse response = new C4ACareReceiversResponse();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		/**
		 * ****************Action*************
		 */

		List<UserInRole> userinroleparamsList = userInRoleRepository.findByRoleId(Short.valueOf("1"));

		if (userinroleparamsList.isEmpty()) {
			response.setMessage("No users found");
			response.setResponseCode(0);
			return Response.ok(objectMapper.writeValueAsString(response)).build();
		} else {
			List<C4ACareReceiverListResponse> itemList = new ArrayList<C4ACareReceiverListResponse>();
			for (UserInRole users : userinroleparamsList) {
				response.setMessage("success");
				response.setResponseCode(10);

				int age = 0;

				if (users.getCrProfile() != null) {
					LocalDate birthDate = new LocalDate(users.getCrProfile().getBirthDate());
					Years age2 = Years.yearsBetween(birthDate, new LocalDate());
					age = age2.getYears();
				}

				// **************************************

				String frailtyStatus = null;
				String frailtyNotice = null;
				char attention = 0;
				String textline = null;
				char interventionstatus = 0;
				String interventionDate = null;
				String detectionStatus = null;
				String detectionDate = null;

				if (users.getCareProfile() != null) {
					attention = users.getCareProfile().getAttentionStatus();
					textline = users.getCareProfile().getIndividualSummary();
					interventionstatus = users.getCareProfile().getInterventionStatus();
					interventionDate = sdf.format(users.getCareProfile().getLastInterventionDate());
				}

				List<FrailtyStatusTimeline> frailtyparamsList = new ArrayList<FrailtyStatusTimeline>(
						users.getFrailtyStatusTimeline());

				if (frailtyparamsList != null && frailtyparamsList.size() > 0) {
					frailtyStatus = frailtyparamsList.get(0).getCdFrailtyStatus().getFrailtyStatus();
					frailtyNotice = frailtyparamsList.get(0).getFrailtyNotice();
				}

				itemList.add(new C4ACareReceiverListResponse(users.getId(), age, frailtyStatus, frailtyNotice,
						attention, textline, interventionstatus, interventionDate, detectionStatus, detectionDate));
			} // detectionVariables loop
			response.setItemList(itemList);

		} // end detectionVariables is empty

		return Response.ok(objectMapper.writeValueAsString(response)).build();

	}// end method

	@Transactional("transactionManager")
	@GET
	@Path("login/username/{username}/password/{password}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response login(@PathParam("username") String username, @PathParam("password") String password)
			throws IOException {
		/**
		 * ****************Variables*************
		 */
		UserInRole user;
		C4ALoginResponse response = new C4ALoginResponse();
		/**
		 * ****************Action*************
		 */
		try {
			user = userInRoleRepository.findBySystemUsernameAndPassword(username, password);

			logger.info("user: " + username);
			logger.info("password: " + password);
			logger.info("repository: " + user);

			if (user == null) {
				response.setMessage("wrong credentials");
				response.setResponseCode(0);
				response.setDisplayName("");
				return Response.ok(response).build();
			} else {
				if (user.getRoleId().equals(Short.valueOf("8"))) {
					response.setMessage("success");
					response.setResponseCode(10);
					if (user.getUserInSystem().getDisplayName() != null) {
						response.setDisplayName(user.getUserInSystem().getDisplayName());
					} else {
						response.setDisplayName("");
					}
					return Response.ok(response).build();
				} else {
					response.setMessage("you don't have the right permissions");
					response.setResponseCode(0);
					response.setDisplayName("");
					return Response.ok(response).build();
				}
			}

		} catch (Exception e) {
			response.setMessage("something went terrible wrong");
			response.setResponseCode(2);
			response.setDisplayName("");
			return Response.ok(response).build();
		}
	}

	// Should be AssessmentsService.getDiagramData
	@GET
	@Path("getDiagramData")
	@Produces("application/json")
	public Response getDiagramData(@QueryParam("careReceiverId") String careReceiverId,
			@QueryParam("parentFactorId") Integer parentFactorId) throws IOException {
		C4AGroupsResponse response = new C4AGroupsResponse();

		List<GeriatricFactorValue> gereatricfactparamsList;
		List<DetectionVariable> detectionvarsparamsList;
		ArrayList<C4ServiceGetOverallScoreListResponse> itemList;

		/**
		 * ****************Action*************
		 */

		List<String> parentFactors = null;

		if (parentFactorId.equals(Integer.valueOf(-1))) {
			parentFactors = Arrays.asList("OVL", "GFG");
		} else {
			parentFactors = Arrays.asList("GES");
		}

		detectionvarsparamsList = detectionVariableRepository.findByDetectionVariableTypes(parentFactors);

		if (detectionvarsparamsList.isEmpty()) {
			response.setMessage("No detection variables found");
			response.setResponseCode(0);
			return Response.ok(objectMapper.writeValueAsString(response)).build();
		} else {
			itemList = new ArrayList<C4ServiceGetOverallScoreListResponse>();
			for (DetectionVariable types : detectionvarsparamsList) {

				Long dvId = types.getId();

				// we use list to avoid "not found" exception
				gereatricfactparamsList = geriatricFactorRepository.findByDetectionVariableId(dvId,
						Long.valueOf(careReceiverId));
				//
				if (gereatricfactparamsList.isEmpty()) {
					response.setMessage("No factors for this group");
					response.setResponseCode(0);
					response.setCareReceiverName("");
					response.setItemList(null);
				} else {

					response.setMessage("success");
					response.setResponseCode(10);
					response.setCareReceiverName(
							gereatricfactparamsList.get(0).getUserInRole().getUserInSystem().getUsername());

					List<Long> timeintervalIds = new ArrayList<Long>();

					for (GeriatricFactorValue gef : gereatricfactparamsList) {
						timeintervalIds.add(gef.getTimeInterval().getId());
					}

					String parentGroupName = "";
					if (gereatricfactparamsList.get(0).getGefTypeId().getDerivedDetectionVariable() != null) {
						parentGroupName = gereatricfactparamsList.get(0).getGefTypeId().getDerivedDetectionVariable()
								.getDetectionVariableName();
					}

					itemList.add(new C4ServiceGetOverallScoreListResponse(gereatricfactparamsList, parentGroupName,
							frailtyStatusTimelineRepository.findByPeriodAndUserIdOld(timeintervalIds,
									gereatricfactparamsList.get(0).getUserInRole().getId())));

				}

			} // detectionVariables loop
			response.setItemList(itemList);
		} // end detectionVariables is empty

		return Response.ok(objectMapper.writeValueAsString(response)).build();

	}// end method

	private OJDiagramFrailtyStatus transformToDto(List<FrailtyStatusTimeline> fs, List<DataIdValue> months) {
		OJDiagramFrailtyStatus dto = new OJDiagramFrailtyStatus();
		dto.setMonths(months);

		Serie preFrail = new Serie("Pre-Frail",
				new ArrayList<eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue>());
		Serie frail = new Serie("Frail", new ArrayList<eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue>());
		Serie fit = new Serie("Fit", new ArrayList<eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue>());

		for (FrailtyStatusTimeline frailty : fs) {

			if (frailty != null && frailty.getCdFrailtyStatus() != null) {
				switch (frailty.getCdFrailtyStatus().getFrailtyStatus()) {

				case "Pre-frail":
					preFrail.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), 0.1));
					frail.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), null));
					fit.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), null));
					break;
				case "Frail":
					frail.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), 0.1));
					preFrail.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), null));
					fit.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), null));
					break;
				case "Fit":
					fit.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), 0.1));
					preFrail.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), null));
					frail.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), null));
					break;
				default:
					preFrail.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), null));
					frail.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), null));
					fit.getItems().add(new eu.city4age.dashboard.api.pojo.dto.oj.variant.DataIdValue(
							frailty.getTimeInterval().getId(), null));
					break;

				}
			}
		}

		dto.getSeries().add(preFrail);
		dto.getSeries().add(frail);
		dto.getSeries().add(fit);

		return dto;
	}

}// end class