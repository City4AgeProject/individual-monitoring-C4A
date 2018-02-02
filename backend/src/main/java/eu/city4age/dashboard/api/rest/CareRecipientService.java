package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.JSONP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.CrProfileRepository;
import eu.city4age.dashboard.api.jpa.FrailtyStatusTimelineRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
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
import eu.city4age.dashboard.api.pojo.dto.DataSet;
import eu.city4age.dashboard.api.pojo.dto.Group;
import eu.city4age.dashboard.api.pojo.dto.Item;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie;
import eu.city4age.dashboard.api.pojo.enu.AllPilotRoles;
import eu.city4age.dashboard.api.pojo.enu.SamePilotRoles;
import eu.city4age.dashboard.api.pojo.ws.C4ACareRecipientListResponse;
import eu.city4age.dashboard.api.pojo.ws.C4ACareRecipientsResponse;
import eu.city4age.dashboard.api.pojo.ws.C4AGroupsResponse;
import eu.city4age.dashboard.api.pojo.ws.C4ServiceGetOverallScoreListResponse;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.security.JwtIssuer;

/**
 *
 * @author EMantziou
 */
@Component(value = "wsService")
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
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

	@JSONP(queryParam = "callback")
	@GET
	@Path("getGroups/careRecipientId/{careRecipientId}/parentFactors/{parentFactors : .+}")
	@Produces({MediaType.APPLICATION_JSON, "application/javascript"})
	public Response getGroups(@PathParam("careRecipientId") String careRecipientId,
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
					fMap.put(gef.getDetectionVariableId(), new ArrayList<Float>());
					idMap.put(gef.getDetectionVariableId(), new ArrayList<Long>());
	
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

					if (gef.getDetectionVariableId() != null && gef.getDetectionVariableId().equals(type.getId())) {
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

		return JerseyResponse.build(objectMapper.writeValueAsString(response));

	}// end method

    @GET
    @Path("getCareRecipients")
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    public Response getCareRecipients(@HeaderParam("Authorization") String jwt) throws IOException {
        C4ACareRecipientsResponse response = new C4ACareRecipientsResponse();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<UserInRole> userinroleparamsList = new ArrayList<>();
        Short defaultRoleId = Short.valueOf("1");

        // Verify and decode JWT token
        DecodedJWT token;
        try {
            token = JwtIssuer.INSTANCE.verify(jwt);
        } catch (JWTVerificationException e) {
			e.printStackTrace();
			response.getStatus().setResponseCode("402 - JWT NOT VALID.");
			String status = "No valid Jwt used for service authorisation.";
			response.getStatus().setConsole(status);
			response.getStatus().setCause(e.getMessage());
			response.getStatus().setStackTrace(Arrays.toString(e.getStackTrace()));
            return JerseyResponse.buildTextPlain("402");
        }
        Map<String, Claim> claims = token.getClaims();
        Integer role = claims.get("rol").asInt();
        String pilotCode = claims.get("plt").asString();

        // 
        if (SamePilotRoles.getEnumAsSet().contains(role)) {

            userinroleparamsList = userInRoleRepository.findByRoleIdAndPilotCode(defaultRoleId, pilotCode);
        } else if (AllPilotRoles.getEnumAsSet().contains(role)) {

            userinroleparamsList = userInRoleRepository.findByRoleId(defaultRoleId);
        }
		userinroleparamsList.sort(Comparator.comparing(UserInRole::getId));

        if (userinroleparamsList.isEmpty()) {
            response.setMessage("No users found");
            response.setResponseCode(0);
            return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response));
        } else {
            List<C4ACareRecipientListResponse> itemList = new ArrayList<C4ACareRecipientListResponse>();
            for (UserInRole user : userinroleparamsList) {
                response.setMessage("success");
                response.setResponseCode(10);

                int age = 0;
                String gender="";

                if (user.getCrProfile() != null) {

                    LocalDate birthDate = user.getCrProfile().getBirthDate().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
                    gender = user.getCrProfile().isGender()?"male":"female";
                    

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
                        attention, textline, interventionstatus, interventionDate, detectionStatus, detectionDate, userPilot.getPilotCode(), gender));
            } // detectionVariables loop
            response.setItemList(itemList);

		} // end detectionVariables is empty

		return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response));

	}// end method
    
	@GET
	@Path("getCareRecipientPilotLocalData/{careRecipientId}")
	@Produces({MediaType.APPLICATION_JSON, "application/javascript"})
	public String getRemoteCareRecipientData(@PathParam("careRecipientId") Long careRecipientId) {
		
		// TODO: Get the pilot's baseurl from the Pilot Configuration Json
		final String uri = "http://c4amobile.atc.gr/backend/cityForAgeServices2/platform/routes/getCareRecipientData/" + careRecipientId.toString();

	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri, String.class);
	    return result;
	}

	@GET
	@Path("getDiagramData/careRecipientId/{careRecipientId}/parentFactorId/{parentFactorId}")
	@Produces({MediaType.APPLICATION_JSON, "application/javascript"})
	public Response getDiagramData(@PathParam("careRecipientId") Long careRecipientId,
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
		return JerseyResponse.build(response);
	}// end method

	@GET
	@Path("findOne/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOne(@PathParam("id") Long id) throws JsonProcessingException {
		CrProfile crP = crProfileRepository.findOne(id);
		return JerseyResponse.build(objectMapper.writeValueAsString(crP));
	}

	private OJDiagramFrailtyStatus transformToDto(List<FrailtyStatusTimeline> fs, List<DataIdValue> months) {
		OJDiagramFrailtyStatus dto = new OJDiagramFrailtyStatus();
		dto.setMonths(months);

		Collections.sort(fs, new FSTComparator());

		Serie preFrail = new Serie("Pre-Frail", new ArrayList<BigDecimal>());
		Serie frail = new Serie("Frail", new ArrayList<BigDecimal>());
		Serie fit = new Serie("Fit", new ArrayList<BigDecimal>());

		for (FrailtyStatusTimeline frailty : fs) {

			if (frailty != null && frailty.getCdFrailtyStatus() != null) {
				switch (frailty.getCdFrailtyStatus().getFrailtyStatus()) {

				case "Pre-frail":
					preFrail.getItems().add(new BigDecimal(0.1));
					frail.getItems().add(null);
					fit.getItems().add(null);
					break;
				case "Frail":
					frail.getItems().add(new BigDecimal(0.1));
					preFrail.getItems().add(null);
					fit.getItems().add(null);
					break;
				case "Fit":
					fit.getItems().add(new BigDecimal(0.1));
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
