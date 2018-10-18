package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

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
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValuesKey;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.service.ImputeFactorService;
import eu.city4age.dashboard.api.service.ViewService;

@Component
public class ViewServiceImpl implements ViewService {

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

}
