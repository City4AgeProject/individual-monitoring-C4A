package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.PathSegment;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.FastMath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.GenericTableData;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsGroups;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsSeries;
import eu.city4age.dashboard.api.pojo.persist.Filter;
import eu.city4age.dashboard.api.service.AssessmentService;
import eu.city4age.dashboard.api.service.GroupAnalyticsService;

@Component
public class GroupAnalyticsServiceImpl implements GroupAnalyticsService {
	
	static protected Logger logger = LogManager.getLogger(GroupAnalyticsServiceImpl.class);
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private AssessmentService assessmentService;
	
	@Autowired
	private NativeQueryRepository nativeQueryRepository;

	/**
	 * @param valuesList
	 * @param dv
	 * @param correlations
	 * @return 
	 */
	@Override
	public LinkedHashMap<String, Double> averageCorrelationValues(LinkedHashMap<String, Double> valuesList, String name,
			List<Double> correlations) {
		
		if (!correlations.isEmpty()) {
			Double sum = 0d;
			for (Double c : correlations) {
				sum += c;
			}
			valuesList.put(name, sum/correlations.size());
		} else {
			valuesList.put(name, null);
		}
		
		return valuesList;
	}
	
	/**
	 * @param pilotString
	 * @return
	 */
	@Override
	public List<Pilot> getPilots(String pilotString) {
		List<Pilot> pilots = new ArrayList<Pilot>();

		if (pilotString != null && !pilotString.contains("whole_population")) {
			List<String> pilotsString = Arrays.asList(pilotString.split(" "));

			for (String ps : pilotsString) pilots.add(pilotRepository.findByPilotCode(PilotCode.valueOf(ps.toUpperCase())));
		} else {
			pilots = pilotRepository.findAll();
		}
		return pilots;
	}
	
	/**
	 * @param detectionVariableId
	 * @return
	 */
	@Override
	public List<DetectionVariable> getDetectionVariables(List<PathSegment> detectionVariableId) {
		List<DetectionVariable> detectionVariables = new ArrayList<DetectionVariable>();
		List<Long> detectionVariableIds = assessmentService.convertToListLong(detectionVariableId);
		for (Long detectionVariable : detectionVariableIds) detectionVariables.add(detectionVariableRepository.findOne(detectionVariable));
		return detectionVariables;
	}
	
	/**
	 * @param overall
	 * @param dv
	 * @param correlations
	 * @param intervalStartDate
	 * @param intervalEndDate
	 * @param uir
	 * @return 
	 */
	@Override
	public Double calculateCorrelationCoefficientsForOneUser(DetectionVariable overall, DetectionVariable dv,
			Timestamp intervalStart, Timestamp intervalEnd, UserInRole uir) {

		List<Object[]> result = new ArrayList<Object[]>();			
		if (dv.getDetectionVariableType().getDetectionVariableType() != DetectionVariableType.Type.MEA) 
			result = nativeQueryRepository.findOvlAndGfgForUserInRoleIdAndDetectionVariableIdForPeriod(uir.getId(), dv.getId(), intervalStart, intervalEnd);
		else
			result = nativeQueryRepository.findOvlAndDmvForUserInRoleIdAndDetectionVariableIdForPeriod(uir.getId(), dv.getId(), intervalStart, intervalEnd);

		int arraySize = result.size();

		// calculate the correlation coefficient
		if (arraySize > 2) {

			double ovlValuesDoubles[] = new double[arraySize];
			double detectionVariableValuesDoubles[] = new double[arraySize];

			for (int i = 0; i < result.size(); i++) {
				ovlValuesDoubles[i] = ((BigDecimal) result.get(i)[1]).doubleValue();
				detectionVariableValuesDoubles[i] = ((BigDecimal) result.get(i)[0]).doubleValue();
			}

			double matrix[][] = { ovlValuesDoubles, detectionVariableValuesDoubles };

			RealMatrix mat = MatrixUtils.createRealMatrix(matrix);
			mat = mat.transpose();

			PearsonsCorrelation corrP = new PearsonsCorrelation(mat);

			double corrPValue = getCorrelationPValues(corrP.getCorrelationMatrix(), mat.getRowDimension());

			if (corrPValue <= 0.05) {
				return new PearsonsCorrelation().correlation(ovlValuesDoubles,
						detectionVariableValuesDoubles);
			} else {
				logger.info("p value is too high!!!");
			}

		} else {
			logger.info("Not enough data!!!");
		}

		return null;
	}
	
    private double getCorrelationPValues(RealMatrix matrix, int nObs) {
		TDistribution tDistribution = new TDistribution(nObs - 2);
        double r = matrix.getEntry(0, 1);
        double t = FastMath.abs(r * FastMath.sqrt((nObs - 2)/(1 - r * r)));
        return (2 * tDistribution.cumulativeProbability(-t));
    }
	
	@Override
	public List<List<Filter>> createAllFilters(List<List<Filter>> allVariablesFilters,
	List<List<Filter>> allPilotsFilters, List<List<Filter>> allCategoryFilters,
	List<List<Filter>> allTimesFilters) {
	
		List<List<Filter>> list = new ArrayList<List<Filter>> ();
	
		for (List<Filter> pilotFilter : allPilotsFilters)
			for (List<Filter> variableFilter : allVariablesFilters)
				if (!allCategoryFilters.isEmpty()) for (List<Filter> categoryFilter: allCategoryFilters)
					if (!allTimesFilters.isEmpty())
						for (List<Filter> timeFilter: allTimesFilters) {
							List<Filter> specificFilters = new ArrayList<Filter> ();
	
							specificFilters.addAll(variableFilter);
							specificFilters.addAll(pilotFilter);
							specificFilters.addAll(timeFilter);
							specificFilters.addAll(categoryFilter);
						
							list.add(specificFilters);	
						}
					else {
						List<Filter> specificFilters = new ArrayList<Filter> ();
					
						specificFilters.addAll(variableFilter);
						specificFilters.addAll(pilotFilter);
						specificFilters.addAll(categoryFilter);
					
						list.add(specificFilters);	
					}
					else
						if (!allTimesFilters.isEmpty())
							for (List<Filter> timeFilter: allTimesFilters) {
								List<Filter> specificFilters = new ArrayList<Filter> ();
				
								specificFilters.addAll(variableFilter);
								specificFilters.addAll(pilotFilter);
								specificFilters.addAll(timeFilter);
							
								list.add(specificFilters);	
							}
						else {
							ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
						
							specificFilters.addAll(variableFilter);
							specificFilters.addAll(pilotFilter);
						
							list.add(specificFilters);	
						}	
		return list;
	}

	@Override
	public List<List<Filter>> createAllTimeFilters(OffsetDateTime intervalStartODT,
			OffsetDateTime intervalEndODT, String comparison, int numOfCategories) {
		
		List<List<Filter>> list = new ArrayList<List<Filter>> ();
		
		if ((comparison != null && comparison.contains("false")) || (comparison == null && numOfCategories == 0)) {
			
			OffsetDateTime current = intervalStartODT;
			
			while (!current.isAfter(intervalEndODT)) {
				
				List<Filter> specificFilters = new ArrayList<Filter> ();
				
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
		} else {
			
			List<Filter> specificFilters = new ArrayList<Filter> ();
			
			Filter byIntervalStart = new Filter();
			byIntervalStart.setName("intervalStart");
			byIntervalStart.getInParams().put("intervalStart", Timestamp.from(intervalStartODT.toInstant()));
			specificFilters.add(byIntervalStart);
			
			Filter byIntervalEnd = new Filter();
			byIntervalEnd.setName("intervalEnd");
			byIntervalEnd.getInParams().put("intervalEnd", Timestamp.from(intervalEndODT.toInstant()));
			specificFilters.add(byIntervalEnd);
			
			list.add(specificFilters);
		}
		
		return list;
	}
	
	@Override
	public List<List<Filter>> createAllFiltersFromPilotCodes (List<String> pilotCodes, Boolean comparison) {
		
		List<List<Filter>> list = new ArrayList<List<Filter>> ();
		
		if (comparison != null && comparison == true) {
			
			for (String pilot : pilotCodes) {
					
					ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
					
					Filter byPilotCodes = new Filter();
					byPilotCodes.setName("pilot");
					
					if (pilot.contains("whole_population")) {						
						List<String> pilots = getAllPilots();						
						byPilotCodes.getInParams().put("pilot", pilots);
					} else {
						byPilotCodes.getInParams().put("pilot", pilot);
					}
					
					specificFilters.add(byPilotCodes);
					
					list.add(specificFilters);				
			}		
		}
		
		else {
			ArrayList<Filter> specificFilters = new ArrayList<Filter> ();
			
			Filter byPilotCodes = new Filter();
			byPilotCodes.setName("pilot");
			
			if (pilotCodes.size() == 1 && pilotCodes.get(0).contains("whole_population")) {
				List<String> pilots = getAllPilots();						
				byPilotCodes.getInParams().put("pilot", pilots);
			} else {
				byPilotCodes.getInParams().put("pilot", pilotCodes);
			}
			
			specificFilters.add(byPilotCodes);
			
			list.add(specificFilters);
		}
		
		return list;
	}

	/**
	 * @return
	 */
	private List<String> getAllPilots() {
		List<String> pilots = new ArrayList<String>();
		List<Pilot> pilotList = pilotRepository.findAll();
		
		for (Pilot p : pilotList) pilots.add(p.getPilotCode().name().toLowerCase());
		return pilots;
	}
	
	@Override
	public List<List<Filter>> createAllFiltersFromVariables (List<Long> detectionVariableIDs) {
		
		List<List<Filter>> list = new ArrayList<List<Filter>> ();
		
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
	public List<List<Filter>> createAllCategoryFilters(List<String> categories) {
		
		List<List<Filter>> allFilters = new ArrayList<List<Filter>>();
		
		HashMap<String, List<String>> socioEconomics = createSocioEconomicsMap();
						
		allFilters = createCategoryFilter (socioEconomics, categories);
		
		return allFilters;
	}
	
	@Override
	public List<List<Filter>> createCategoryFilter(HashMap<String, List<String>> socioEconomics, List<String> categories) {
		
		List<List<Filter>> allFilters = new ArrayList<List<Filter>>();
		
		int cnt = categories.size();
		
		if (cnt - 1 == 0) {
			
			String type = categories.get(cnt - 1);
			
			//logger.info("type: " + type);
			//logger.info("socioEconomics size: " + socioEconomics.size());
			//logger.info("socioEconomics get type: " + socioEconomics.get(type));
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
			List<List<Filter>> alreadyCreatedFilters = createCategoryFilter (socioEconomics, currCategories);
						
			for (List<Filter> alreadyCreatedFilter: alreadyCreatedFilters) {
				
				for (String category : socioEconomics.get(type)) {
					
					List<Filter> specificFilters = new ArrayList<Filter> ();
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
	public GenericTableData addGenericTableData(List<Filter> filter, Object[] data, Boolean comp,
			GenericTableData tableData, List<String> pilotCodes, Boolean hasCategories) {

		if(tableData.getHeaders().size() == 0) {
			tableData.getHeaders().add("avgValue");
			tableData.getHeaders().add("count");
			for (Filter f : filter) {
				switch (f.getName()) {
					case "detectionVariable":
						tableData.getHeaders().add("detectionVariableName");
						tableData.getHeaders().add("detectionVariableType");
						break;
					case "pilot":
						tableData.getHeaders().add("pilot");						
						break;
					case "intervalStart":
						if ((comp == null && hasCategories) || (comp != null && !comp)) {
						//if (!comp) {
							tableData.getHeaders().add("intervalStart");
							tableData.getHeaders().add("typicalPeriod");
						}
						else {
							tableData.getHeaders().add("intervalStart");
							tableData.getHeaders().add("intervalEnd");
						}
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
				row.add(dv.getDetectionVariableName());
				row.add(dv.getDetectionVariableType().getDetectionVariableType().getName());
				break;
			case "pilot":
				if (comp != null && comp) {
					try {
						row.add((String) f.getInParams().entrySet().iterator().next().getValue());
					} catch (Exception e) {
						row.add("Whole population");
					}
				} else {
					ArrayList<String> list = (ArrayList<String>) f.getInParams().entrySet().iterator().next().getValue();
					StringBuilder pilotStringBuilder = new StringBuilder ();
					
					for (int i = 0; i < list.size() - 1; i++)
						pilotStringBuilder.append(list.get(i)).append(", ");
					pilotStringBuilder.append(list.get(list.size() - 1));
					row.add(pilotStringBuilder.toString());
				}
				break;
			case "intervalStart":
				//row.add(OffsetDateTime.ofInstant(((Timestamp) f.getInParams().entrySet().iterator().next().getValue()).toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy/MM")));
				row.add(OffsetDateTime.ofInstant(((Timestamp) f.getInParams().entrySet().iterator().next().getValue()).toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)));
				if ((comp == null && hasCategories) || (comp != null && !comp)) row.add("month");
				break;
			case "intervalEnd":
				if (!((comp == null && hasCategories) || (comp != null && !comp))) row.add(OffsetDateTime.ofInstant(((Timestamp) f.getInParams().entrySet().iterator().next().getValue()).toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)));
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
	
	/**
	 * @param comparison
	 * @param data
	 * @return
	 */
	@Override
	public List<GroupAnalyticsSeries> createSeries(boolean comparison, GenericTableData json) {
		
		List<List<String>> data = json.getData();
		List<String> headers = json.getHeaders();
		
		GroupAnalyticsSeries serie = new GroupAnalyticsSeries();
		List<GroupAnalyticsSeries> series = new ArrayList<GroupAnalyticsSeries>();
		
		int pilotIndex = headers.indexOf("pilot");
		String previousPilot = data.get(0).get(pilotIndex);
		int variableNameIndex = headers.indexOf("detectionVariableName");
		String previousDetectionVariableName = data.get(0).get(variableNameIndex);
		
		int avgValueIndex = headers.indexOf("avgValue");

		String firstPilot = data.get(0).get(pilotIndex);

		serie.setName(previousDetectionVariableName);
		serie.setPilot(previousPilot);

		List<BigDecimal> items = new ArrayList<BigDecimal>();

		for (List<String> entry : data) {
			
			String varName = entry.get(variableNameIndex);
			String pilot = entry.get(pilotIndex);
			String avgValue = entry.get(avgValueIndex);

			if (!(previousDetectionVariableName.contains(varName) && previousPilot.contains(pilot))) {

				previousDetectionVariableName = varName;
				previousPilot = pilot;

				serie.setItems(items);
				series.add(serie);
				items = new ArrayList<BigDecimal>();

				serie = new GroupAnalyticsSeries();

				serie.setName(varName);
				serie.setPilot(previousPilot);
				
				if (comparison && !pilot.equals(firstPilot)) {
					serie.setAssignedToY2("on");
					serie.setDisplayInLegend("off");
				}

			}

			
			if (avgValue.matches("")) {
				items.add(null);
			} else {
				if (comparison && pilot.equals(firstPilot)) {
					items.add(BigDecimal.valueOf(0 - Double.parseDouble(avgValue)));
				} else {
					items.add(BigDecimal.valueOf(Double.parseDouble(avgValue)));
				}
			}

		}
		serie.setItems(items);
		series.add(serie);
		return series;
	}

	@Override
	public List<?> createGroups(List<String> categories,
			HashMap<String, List<String>> socioEconomics, List<String> datesStringList, boolean comparison, boolean comp) {

		int cnt = categories.size();

		List<GroupAnalyticsGroups> groups = new ArrayList<GroupAnalyticsGroups>();

		if (cnt - 1 == 0) {
			if (!comp || comparison) {
				// base case for comparison and/or initial diagram where the groups is a List<String>
				return socioEconomics.get(categories.get(cnt - 1));
			} else {
				// base case for evolution in time (contains the timeline) where the dates are a List<String>
				for (String category1 : socioEconomics.get(categories.get(cnt - 1))) {
					GroupAnalyticsGroups group = new GroupAnalyticsGroups();
					group.setName(category1);
					group.setGroups(datesStringList);
					groups.add(group);
				}
				return groups;
			}
		} else {

			ArrayList<String> currCategories = new ArrayList<String> ();
			for (int i = 0; i < cnt - 1; i++) 
				currCategories.add(categories.get(i));

			String type = categories.get(cnt - 1);

			List<?> alreadyCreatedGroups = createGroups(currCategories, socioEconomics, datesStringList, comparison, comp);

			for (String category : socioEconomics.get(type)) {
				GroupAnalyticsGroups group = new GroupAnalyticsGroups();
				group.setName(category);
				group.setGroups(alreadyCreatedGroups);
				groups.add(group);

			}
			return groups;
		}
	}

	/**
	 * @param url
	 * @return
	 */
	@Override
	public List<String> createDateList(String url) {
		List<String> datesStringList;
		String startDate = "";
		String endDate = "";
		for (String str : Arrays.asList(url.split("&"))) {
			if (str.contains("intervalStart"))
				startDate = str.split("=")[1];
			if (str.contains("intervalEnd"))
				endDate = str.split("=")[1];
		}
		
		OffsetDateTime intervalStartODT = LocalDate.parse(startDate.substring(0, 10), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd")).withDayOfMonth(1).atStartOfDay().atOffset(ZoneOffset.UTC);
		
		OffsetDateTime intervalEndODT = LocalDate.parse(endDate.substring(0, 10), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd")).withDayOfMonth(1).atStartOfDay().atOffset(ZoneOffset.UTC);
		
		datesStringList = new ArrayList<String>();
		
		OffsetDateTime current = intervalStartODT;
		
		while (!current.isAfter(intervalEndODT)) {
			String currentString = current.format(DateTimeFormatter.ofPattern("yyyy/MM"));
			datesStringList.add(currentString);
			current = current.plusMonths(1L);
		}
		
		return datesStringList;
	}

	/**
	 * @return
	 */
	@Override
	public HashMap<String, List<String>> createSocioEconomicsMap() {
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
		return socioEconomics;
	}

	/**
	 * @param url
	 * @param list
	 * @param property
	 * @return
	 */
	@Override
	public List<String> getPropertyFromURL(String url, String property) {
		
		List<String> propertyList = new ArrayList<String>();
		for (String str : Arrays.asList(url.split("&"))) {
			if (str.contains(property)) {
				propertyList = Arrays.asList(str.split("=")[1].split(" "));
			}
		}
		return propertyList;
	}

}
