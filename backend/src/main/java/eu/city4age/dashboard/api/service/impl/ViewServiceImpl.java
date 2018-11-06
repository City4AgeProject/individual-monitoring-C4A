package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.dto.GenericTableData;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.persist.Filter;
import eu.city4age.dashboard.api.service.ImputeFactorService;
import eu.city4age.dashboard.api.service.ViewService;

@Component
public class ViewServiceImpl implements ViewService {
	
	static protected Logger logger = LogManager.getLogger(ViewServiceImpl.class);

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private ImputeFactorService imputeFactorService;

	@Autowired
	private PilotRepository pilotRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

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
	
	@Override
	public List<ArrayList<Filter>> createAllFilters(List<ArrayList<Filter>> allVariablesFilters,
			List<ArrayList<Filter>> allPilotsFilters, List<ArrayList<Filter>> allCategoryFilters,
			List<ArrayList<Filter>> allTimesFilters) {
		
		List<ArrayList<Filter>> list = new ArrayList<ArrayList<Filter>> ();
		
		for (ArrayList<Filter> pilotFilter : allPilotsFilters)
			for (ArrayList<Filter> variableFilter : allVariablesFilters)
				if (!allCategoryFilters.isEmpty()) for (ArrayList<Filter> categoryFilter: allCategoryFilters)	
					if (!allTimesFilters.isEmpty())
						for (ArrayList<Filter> timeFilter: allTimesFilters) {
							ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
							
							specificFilters.addAll(variableFilter);
							specificFilters.addAll(pilotFilter);
							specificFilters.addAll(timeFilter);
							if (!allCategoryFilters.isEmpty())
								specificFilters.addAll(categoryFilter);
							
							list.add(specificFilters);				
						}
					else {
						ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
						
						specificFilters.addAll(variableFilter);
						specificFilters.addAll(pilotFilter);
						if (!allCategoryFilters.isEmpty())
							specificFilters.addAll(categoryFilter);
						
						list.add(specificFilters);						
					}

		return list;
	}

	@Override
	public List<ArrayList<Filter>> createAllTimeFilters(OffsetDateTime intervalStartODT,
			OffsetDateTime intervalEndODT) {
		
		List<ArrayList<Filter>> list = new ArrayList<ArrayList<Filter>> ();
		
		OffsetDateTime current = intervalStartODT;
		
		while (!current.isAfter(intervalEndODT)) {
			
			ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
			
			Filter byIntervalStart = new Filter();
			byIntervalStart.setName("intervalStart");
			byIntervalStart.getInParams().put("intervalStart", Timestamp.from(current.toInstant()));
			specificFilters.add(byIntervalStart);
			
			current = current.plusMonths(1l);
			
			Filter byIntervalEnd = new Filter();
			byIntervalEnd.setName("intervalEnd");
			byIntervalEnd.getInParams().put("intervalEnd", Timestamp.from(current.toInstant()));
			specificFilters.add(byIntervalEnd);
			
			list.add(specificFilters);
		}
		return list;
	}
	
	@Override
	public List<ArrayList<Filter>> createAllFiltersFromPilotCodes (List<String> pilotCodes, Boolean comparison) {
		
		List<ArrayList<Filter>> list = new ArrayList<ArrayList<Filter>> ();
		
		if (comparison ) {
			
			for (String pilot : pilotCodes) {
					
					ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
					
					Filter byPilotCodes = new Filter();
					byPilotCodes.setName("pilot");
					byPilotCodes.getInParams().put("pilot", pilot);
					specificFilters.add(byPilotCodes);
					
					list.add(specificFilters);				
			}		
		}
		
		else {
			ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
			
			Filter byPilotCodes = new Filter();
			byPilotCodes.setName("pilot");
			byPilotCodes.getInParams().put("pilot", pilotCodes);
			specificFilters.add(byPilotCodes);
			
			list.add(specificFilters);
		}
		
		return list;
	}
	
	@Override
	public List<ArrayList<Filter>> createAllFiltersFromVariables (List<Long> detectionVariableIDs) {
		
		List<ArrayList<Filter>> list = new ArrayList<ArrayList<Filter>> ();
		
		for (Long varID : detectionVariableIDs) {
			
			ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
			
			Filter byDetectionVariables = new Filter();
			byDetectionVariables.setName("detectionVariable");
			byDetectionVariables.getInParams().put("detectionVariable", varID);
			specificFilters.add(byDetectionVariables);
				
			list.add(specificFilters);
		}
		
		return list;
	}
	
	@Override
	public List<ArrayList<Filter>> createAllCategoryFilters(List<String> categories) {
		
		List<ArrayList<Filter>> allFilters = new ArrayList<ArrayList<Filter>>();
		
		HashMap<String, List<String>> socioEconomics = new HashMap <String, List<String>> ();
		
		socioEconomics.put("sex", Arrays.asList("m", "f"));
		socioEconomics.put("marital_status", Arrays.asList("s", "m", "w", "d", "t"));
		socioEconomics.put("age_group", Arrays.asList("50-59", "60-69", "70-79", "80-89", "90+"));
		socioEconomics.put("education", Arrays.asList("none", "primary", "secondary", "tertiary"));
		socioEconomics.put("cohabiting", Arrays.asList("alone", "family", "friends", "other"));
		socioEconomics.put("informal_caregiver_ability", Arrays.asList("t", "f"));
		socioEconomics.put("quality_housing", Arrays.asList("low", "average", "high"));
		socioEconomics.put("quality_neighborhood", Arrays.asList("low", "average", "high"));
		socioEconomics.put("working", Arrays.asList("t", "f"));
						
		allFilters = createCategoryFilter (socioEconomics, categories);
		
		return allFilters;
	}
	
	@Override
	public List<ArrayList<Filter>> createCategoryFilter(HashMap<String, List<String>> socioEconomics, List<String> categories) {
		
		List<ArrayList<Filter>> allFilters = new ArrayList<ArrayList<Filter>>();
		
		int cnt = categories.size();
		
		if (cnt - 1 == 0) {
			
			String type = categories.get(cnt - 1);
			
			logger.info("type: " + type);
			logger.info("socioEconomics size: " + socioEconomics.size());
			logger.info("socioEconomics get type: " + socioEconomics.get(type));
			for (String category : socioEconomics.get(type)) {
				
				ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
				Filter filter = new Filter();
				filter.setName(type);
				filter.getInParams().put(type, category);
				specificFilters.add(filter);
				allFilters.add(specificFilters);
			}
			
			return allFilters;
		}
		else {
			
			ArrayList<String> currCategories = new ArrayList<String> ();
			for (int i = 0; i < cnt - 1; i++) 
				currCategories.add(categories.get(i));
			String type = categories.get(cnt - 1);
			List<ArrayList<Filter>> alreadyCreatedFilters = createCategoryFilter (socioEconomics, currCategories);
						
			for (ArrayList<Filter> alreadyCreatedFilter: alreadyCreatedFilters) {
				
				for (String category : socioEconomics.get(type)) {
					
					ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
					Filter filter = new Filter();
					filter.setName(type);
					filter.getInParams().put(type, category);
					specificFilters.addAll(alreadyCreatedFilter);
					specificFilters.add(filter);					
					allFilters.add(specificFilters);
				}
			}
			
			return allFilters;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericTableData addGenericTableData(ArrayList<Filter> filter, Object[] data, Boolean comp,
			GenericTableData tableData, List<String> pilotCodes) {

		if(tableData.getHeaders().size() == 0) {
			tableData.getHeaders().add("avgValue");
			tableData.getHeaders().add("count");
			for (Filter f : filter) {
				switch (f.getName()) {
					case "detectionVariable":
						tableData.getHeaders().add("detectionVariable");
						tableData.getHeaders().add("detectionVariableName");
						tableData.getHeaders().add("detectionVariableType");
						break;
					case "pilot":
						if (comp)
							tableData.getHeaders().add("pilot");
						else {
							for(String pilot : pilotCodes) {
								tableData.getHeaders().add(pilot);
							}
						}
						break;
					case "intervalStart":
						tableData.getHeaders().add("intervalStartJSON");
						tableData.getHeaders().add("intervalStart");
						tableData.getHeaders().add("typicalPeriod");
						break;
					case "intervalEnd":
						break;
					default:
						tableData.getHeaders().add(f.getName());
						break;
				}
				
			}
		}
		
		ArrayList<String> row = new ArrayList<String>();
		
		if (data[0] != null)
			row.add(BigDecimal.valueOf((Double)data[0]).setScale(3, RoundingMode.HALF_UP).toString());
		else 
			row.add("");
		if (data[1] != null)
			row.add(((Long) data[1]).toString());
		else 
			row.add(Long.valueOf(0l).toString());
		
		for (Filter f : filter) {
			switch (f.getName()) {
			case "detectionVariable":
				DetectionVariable dv = detectionVariableRepository.getOne((Long) f.getInParams().entrySet().iterator().next().getValue());
				row.add(dv.getId().toString());
				row.add(dv.getDetectionVariableName());
				row.add(dv.getDetectionVariableType().getDetectionVariableType().getName());
				break;
			case "pilot":
				if (comp)
					row.add((String) f.getInParams().entrySet().iterator().next().getValue());	
				else {
					ArrayList<String> list = (ArrayList<String>) f.getInParams().entrySet().iterator().next().getValue();
					StringBuilder pilotStringBuilder = new StringBuilder ();
					
					for (int i = 0; i < list.size() - 1; i++)
						pilotStringBuilder.append(list.get(i)).append(", ");
					pilotStringBuilder.append(list.get(list.size() - 1));
					row.add(pilotStringBuilder.toString());
				}
				break;
			case "intervalStart":
				row.add(OffsetDateTime.ofInstant(((Timestamp) f.getInParams().entrySet().iterator().next().getValue()).toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy/MM")));
				row.add(OffsetDateTime.ofInstant(((Timestamp) f.getInParams().entrySet().iterator().next().getValue()).toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)));
				row.add("month");
				break;
			case "intervalEnd":
				break;
			case "sex":
			case "marital_status":
			case "age_group":
			case "education":
			case "cohabiting":
			case "informal_caregiver_ability":
			case "quality_housing":
			case "quality_neighborhood":
			case "working":
				row.add((String) f.getInParams().entrySet().iterator().next().getValue().toString());
			}		
		}

		tableData.getData().add(row);

		return tableData;
	}

}
