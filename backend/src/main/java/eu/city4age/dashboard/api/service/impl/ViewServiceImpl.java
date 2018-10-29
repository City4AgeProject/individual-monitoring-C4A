package eu.city4age.dashboard.api.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.dto.AnalyticsDiagramData;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsCSVCategoryView;
import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsCSVTimeCategoryView;
import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsCSVTimeView;
import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsCSVView;
import eu.city4age.dashboard.api.pojo.persist.Filter;
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
	
	@Override
	public List<ArrayList<Filter>> createAllFilters(List<ArrayList<Filter>> allVariablesFilters,
			List<ArrayList<Filter>> allPilotsFilters, List<ArrayList<Filter>> allCategoryFilters,
			List<ArrayList<Filter>> allTimesFilters) {
		
		List<ArrayList<Filter>> list = new ArrayList<ArrayList<Filter>> ();
		
		for (ArrayList<Filter> pilotFilter : allPilotsFilters) {
		
			for (ArrayList<Filter> variableFilter : allVariablesFilters) {			
						
				for (ArrayList<Filter> categoryFilter: allCategoryFilters) {			
					
					for (ArrayList<Filter> timeFilter: allTimesFilters) {
						
						ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
						
						specificFilters.addAll(variableFilter);
						specificFilters.addAll(pilotFilter);
						specificFilters.addAll(timeFilter);
						specificFilters.addAll(categoryFilter);
						
						list.add(specificFilters);				
					}			
				}
			}
		}
		
		return list;
	}
	
	@Override
	public List<ArrayList<Filter>> createAllFiltersWithoutCategories(List<ArrayList<Filter>> allVariablesFilters,
			List<ArrayList<Filter>> allPilotsFilters, List<ArrayList<Filter>> allTimesFilters) {
		
		List<ArrayList<Filter>> list = new ArrayList<ArrayList<Filter>> ();
		
		for (ArrayList<Filter> pilotFilter : allPilotsFilters) {
		
			for (ArrayList<Filter> variableFilter : allVariablesFilters) {
			
				for (ArrayList<Filter> timeFilter: allTimesFilters) {
					
					ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
					
					specificFilters.addAll(variableFilter);
					specificFilters.addAll(pilotFilter);
					specificFilters.addAll(timeFilter);
					
					list.add(specificFilters);				
				}			
			}
		}
		
		return list;
	}
	
	@Override
	public List<ArrayList<Filter>> createAllFiltersWithoutTimes(List<ArrayList<Filter>> allVariablesFilters,
			List<ArrayList<Filter>> allPilotsFilters, List<ArrayList<Filter>> allCategoryFilters) {
		
		List<ArrayList<Filter>> list = new ArrayList<ArrayList<Filter>> ();
		
		for (ArrayList<Filter> pilotFilter : allPilotsFilters) {
		
			for (ArrayList<Filter> variableFilter : allVariablesFilters) {
			
				for (ArrayList<Filter> categoryFilter: allCategoryFilters) {
					
					ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
					
					specificFilters.addAll(variableFilter);
					specificFilters.addAll(pilotFilter);
					specificFilters.addAll(categoryFilter);
					
					list.add(specificFilters);				
				}			
			}
		}
		
		return list;
	}
	
	@Override
	public List<ArrayList<Filter>> createAllFiltersWithoutCategoriesAndTime(
			List<ArrayList<Filter>> allVariablesFilters, List<ArrayList<Filter>> allPilotsFilters) {
		
		List<ArrayList<Filter>> list = new ArrayList<ArrayList<Filter>> ();
		
		for (ArrayList<Filter> pilotFilter : allPilotsFilters) {
		
			for (ArrayList<Filter> variableFilter : allVariablesFilters) {
				
				ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
				
				specificFilters.addAll(variableFilter);
				specificFilters.addAll(pilotFilter);
				
				list.add(specificFilters);				
			}			
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
	
	@Override
	@Async
	public void writeToCsv (int viewSelecter, List<String> categories, List<AnalyticsDiagramData> data, File tmp) throws IOException {
		
		CsvMapper mapper = new CsvMapper();
		CsvSchema.Builder schemaBuilder = new  CsvSchema.Builder ();
		schemaBuilder.addColumn("pilot").addColumn("detectionVariableName").addColumn("detectionVariableType");
		String csv;
		
		switch (viewSelecter) {
		case 1:
			schemaBuilder.addColumn("avgValue").addColumn("count");
			csv = mapper.writer(schemaBuilder.build().withHeader()).withView(AnalyticsCSVView.class).writeValueAsString(data);
			break;
		case 2:	
			schemaBuilder.addColumn("avgValue").addColumn("count");
			for (String c : categories) {
				schemaBuilder.addColumn(c);
			}
			csv = mapper.writer(schemaBuilder.build().withHeader()).withView(AnalyticsCSVCategoryView.class).writeValueAsString(data);
			break;		
		case 3:		
			schemaBuilder.addColumn("intervalStart").addColumn("typicalPeriod");
			schemaBuilder.addColumn("avgValue").addColumn("count");
			csv = mapper.writer(schemaBuilder.build().withHeader()).withView(AnalyticsCSVTimeView.class).writeValueAsString(data);
			break;
		case 4:
			schemaBuilder.addColumn("intervalStart").addColumn("typicalPeriod");
			schemaBuilder.addColumn("avgValue").addColumn("count");
			for (String c : categories) {
				schemaBuilder.addColumn(c);
			}
			csv = mapper.writer(schemaBuilder.build().withHeader()).withView(AnalyticsCSVTimeCategoryView.class).writeValueAsString(data);
			break;
		default: return;
		}
		
		FileOutputStream fos = new FileOutputStream(tmp);
		fos.write(csv.getBytes());
		fos.close();
		
	}

}
