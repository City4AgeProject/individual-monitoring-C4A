package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.service.ViewService;

@Component
public class ViewServiceImpl implements ViewService {
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Override
	public DataIdValue createMonthLabel(Long timeIntervalId) {
		DataIdValue monthLabel = new DataIdValue();

		SimpleDateFormat formatWithTz = new SimpleDateFormat("yyyy/MM");

		formatWithTz.setTimeZone(TimeZone.getTimeZone("UTC"));

		monthLabel.setId(timeIntervalId);
		monthLabel.setName(formatWithTz.format(timeIntervalRepository.findOne(timeIntervalId).getIntervalStart()));

		return monthLabel;
	}

	@Override
	public TreeSet<DataIdValue> createMonthLabels(List<ViewGefCalculatedInterpolatedPredictedValues> gefs) {

		TreeSet<DataIdValue> monthLabels = new TreeSet<DataIdValue>();

		SimpleDateFormat formatWithTz = new SimpleDateFormat("yyyy/MM");

		formatWithTz.setTimeZone(TimeZone.getTimeZone("UTC"));

		for (ViewGefCalculatedInterpolatedPredictedValues gef: gefs) {
			monthLabels.add(new DataIdValue(gef.getId().getTimeIntervalId(), gef.getIntervalStartLabel()));
		}

		return monthLabels;
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
