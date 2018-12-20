package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.py.HiddenMarkovModelService;
import eu.city4age.dashboard.api.service.ImputeFactorService;
import eu.city4age.dashboard.api.service.IndividualMonitoringService;
import jep.Jep;
import jep.JepConfig;
import jep.JepException;

@Component
public class IndividualMonitoringServiceImpl implements IndividualMonitoringService {

	static protected Logger logger = LogManager.getLogger(IndividualMonitoringServiceImpl.class);

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private ImputeFactorService imputeFactorService;

	@Autowired
	private PilotRepository pilotRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Override
	public TreeSet<DataIdValue> createMonthLabels(List<ViewGefCalculatedInterpolatedPredictedValues> gefs) {

		Calendar date = Calendar.getInstance();

		int index=0;

		ViewGefCalculatedInterpolatedPredictedValues gef = gefs.get(index);

		TreeSet<DataIdValue> monthLabels = new TreeSet<DataIdValue>();

		String pilotCodeString = gefs.get(0).getPilotCode().toUpperCase();		
		Pilot pilot = pilotRepository.findOne(PilotCode.valueOf(pilotCodeString));

		SimpleDateFormat formatWithTz = new SimpleDateFormat("yyyy/MM");
		formatWithTz.setTimeZone(TimeZone.getTimeZone(pilot.getCompZone()));

		TimeInterval startDate = timeIntervalRepository.findOne(gef.getTimeIntervalId());

		TimeInterval endDate = timeIntervalRepository.findOne(gefs.get(gefs.size() - 1).getTimeIntervalId());

		TimeInterval midDate;

		while (startDate.getIntervalStart().before(endDate.getIntervalStart())) {
			//push u monthLabels
			monthLabels.add(new DataIdValue(startDate.getId(), formatWithTz.format(startDate.getIntervalStart())));

			date.setTime(startDate.getIntervalStart());

			startDate = imputeFactorService.getFollowingTimeInterval(date);

			gef = gefs.get(index + 1);

			midDate = timeIntervalRepository.findOne(gef.getTimeIntervalId());

			if(!startDate.getIntervalStart().before(midDate.getIntervalStart())) {
				startDate=midDate;
				index++;
			}

		}

		monthLabels.add(new DataIdValue(startDate.getId(), formatWithTz.format(startDate.getIntervalStart())));

		return monthLabels;

	}

	@Override
	public List<ViewGefCalculatedInterpolatedPredictedValues> convertToViewGFVs(List<DerivedMeasureValue> derivedMeasures) {

		List<ViewGefCalculatedInterpolatedPredictedValues> gefs = new ArrayList<>();

		for (DerivedMeasureValue derivedMeasure : derivedMeasures) {

			ViewGefCalculatedInterpolatedPredictedValues gef = new ViewGefCalculatedInterpolatedPredictedValues();
			gef.setTimeIntervalId(Long.valueOf(derivedMeasure.getTimeInterval().getId()));
			gef.setPilotCode(userInRoleRepository.findByUirId(derivedMeasure.getUserInRoleId()).getPilotCode().name());

			gefs.add(gef);
		}
		return gefs;
	}

	@Override
	public OJDiagramFrailtyStatus transformToDto(List<ViewGefCalculatedInterpolatedPredictedValues> gefs, TreeSet<DataIdValue> months) {
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

				if (gef.getFrailtyStatus() != null && !gef.getId().getDataType().equals("p") && month.getId().equals(gef.getTimeIntervalId())) {

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

				if (!found && gef.getDetectionVariableId().equals(501L) && !gef.getId().getDataType().equals("p") && month.getId().equals(gef.getTimeIntervalId())) {

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

	/*public String[] getShapesOfClusters(int num) {
	switch (num) {
	case 1:
		String [] retArray1 = {"circle"};
		return retArray1;
	case 2:
		String [] retArray2 = {"circle", "square"};
		return retArray2;
	case 3:
		String [] retArray3 = {"circle", "triangleDown", "square"};
		return retArray3;
	case 4:
		String [] retArray4 = {"circle", "triangleDown", "plus", "square"};
		return retArray4;
	case 5:
		String [] retArray5 = {"circle", "triangleDown", "diamond", "plus", "square"};
		return retArray5;
	case 6:
		String [] retArray6 = {"circle", "triangleDown", "diamond", "plus", "square", "circle"};
		return retArray6;
	case 7:
		String [] retArray7 = {"circle", "triangleDown", "diamond", "plus", "square", "circle", "triangleDown"};
		return retArray7;
	case 8:
		String [] retArray8 = {"circle", "triangleDown", "diamond", "plus", "square", "circle", "triangleDown", "diamond"};
		return retArray8;
	case 9:
		String [] retArray9 = {"circle", "triangleDown", "diamond", "plus", "square", "circle", "triangleDown", "diamond", "plus"};
		return retArray9;
	case 10:
		String [] retArray10 = {"circle", "triangleDown", "diamond", "plus", "square", "circle", "triangleDown", "diamond", "plus", "square"};
		return retArray10;
	default:			
		return null;
	}	
}
	 */
	
	@Override
	public String[] getColorsOfClusters (int num) {

		switch (num) {
		case 1:
			String [] retArray1 = {"#7EE500"};
			return retArray1;
		case 2:
			String [] retArray2 = {"#D2E000", "#7EE500"};
			return retArray2;
		case 3:
			String [] retArray3 = {"#D2E000", "#25EA00", "#00F7F9"};
			return retArray3;
		case 4:
			String [] retArray4 = {"#D2E000", "#7EE500", "#00F497", "#00F7F9"};
			return retArray4;
		case 5:
			String [] retArray5 = {"#DB9200", "#D2E000", "#7EE500", "#00F497", "#00F7F9"};
			return retArray5;
		case 6:
			String [] retArray6 = {"#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00F497"};
			return retArray6;
		case 7:
			String [] retArray7 = {"#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00",  "#00F497", "#00F7F9"};
			return retArray7;
		case 8:
			String [] retArray8 = {"#D31700", "#D64800", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00F497", "#00F7F9"};
			return retArray8;
		case 9:
			String [] retArray9 = {"#D10017", "#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00EF37", "#00F497", "#00F7F9"};
			return retArray9;
		case 10:
			String [] retArray10 = {"#D10017", "#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00EF37", "#00F497", "#00F7F9", "#0099FF"};
			return retArray10;
		case 11:
			String [] retArray11 = {"#cc0066", "#D10017", "#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00EF37", "#00F497", "#00F7F9", "#0099FF"};
			return retArray11;
		default:			
			return null;
		}	
	}

	@Override
	public String getClusteredSeries(String path, Long userInRoleId, Long varId) throws JepException {

		String response;

		JepConfig jepConfig = new JepConfig ();
		jepConfig.addSharedModules("pandas");
		jepConfig.addSharedModules("numpy");
		jepConfig.addSharedModules("hmmlearn");
		jepConfig.addSharedModules("scipy");
		jepConfig.addSharedModules("sys");
		jepConfig.addSharedModules("json");
		jepConfig.addSharedModules("psycopg2");
		jepConfig.addSharedModules("csv");
		jepConfig.addSharedModules("sklearn");
		jepConfig.addSharedModules("warnings");
		jepConfig.addSharedModules("selectionCriteria");
		jepConfig.addSharedModules("data_preparation_uni");
		jepConfig.addSharedModules("data_preparation_multi");
		jepConfig.addSharedModules("learn_optimal_model");
		jepConfig.addSharedModules("learnOptimalHMMs_and_persist");
		Jep jep = new Jep (jepConfig);

		jep.setInteractive(true);

		try {
			response = HiddenMarkovModelService.clusterSingleSeries(path, jep, userInRoleId.intValue(), varId.intValue());
		} catch (Exception e) {
			logger.info("error unutar jep");
			logger.info(e.toString());
			e.printStackTrace();
			response = "error unutar jep";
		}
		//logger.info(response);
		jep.close();

		return response;
	}

}