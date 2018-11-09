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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.PathSegment;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.city4age.dashboard.api.jpa.DerivedMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
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
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;
	
	@Autowired
	private DerivedMeasureValueRepository derivedMeasureValueRepository;
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private AssessmentService assessmentService;
	

	/**
	 * @param overall
	 * @param dv
	 * @param uir
	 * @param ovlDates
	 * @param dvDates
	 * @param ovlValuesDoubles
	 * @param detectionVariableValuesDoubles
	 * @return
	 */
	public int findDetectionVariableValues(DetectionVariable overall, DetectionVariable dv, UserInRole uir,
			List<Date> ovlDates, List<Date> dvDates, double[] ovlValuesDoubles,
			double[] detectionVariableValuesDoubles) {
		Date intervalStart = null;
		int cnt = 0;
		for (Date ovlDate : ovlDates) {
			if (dvDates.contains(ovlDate)) {
				intervalStart = ovlDate;


				BigDecimal ovlValue = viewGefCalculatedInterpolatedPredictedValuesRepository
						.findByUserInRoleIdAndDetectionVariableIdForOneMonth(uir.getId(), overall.getId(),
								intervalStart);
				ovlValuesDoubles[cnt] = ovlValue.doubleValue();

				BigDecimal detectionVariableValue = null;
				if (dv.getDetectionVariableType()
						.getDetectionVariableType() != DetectionVariableType.Type.MEA) {
					detectionVariableValue = viewGefCalculatedInterpolatedPredictedValuesRepository
							.findByUserInRoleIdAndDetectionVariableIdForOneMonth(uir.getId(), dv.getId(),
									intervalStart);
				} else {
					detectionVariableValue = derivedMeasureValueRepository
							.findByUserInRoleIdAndDetectionVariableIdForOneMonth(uir.getId(), dv.getId(),
									intervalStart);
				}
				detectionVariableValuesDoubles[cnt] = detectionVariableValue.doubleValue();

				cnt++;
			}
		}
		return cnt;
	}

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
	 * @param dv
	 * @param intervalStartDate
	 * @param intervalEndDate
	 * @param uir
	 * @return
	 */
	@Override
	public List<Date> findAllDatesForDetectionVariable(DetectionVariable dv, Date intervalStartDate,
			Date intervalEndDate, UserInRole uir) {
		
		List<Date> dvDates = new ArrayList<Date>();

		if (dv.getDetectionVariableType()
				.getDetectionVariableType() != DetectionVariableType.Type.MEA) {
			dvDates = viewGefCalculatedInterpolatedPredictedValuesRepository
					.findDatesForUserInRoleIdAndDetectionVariableIdForInterval(uir.getId(), dv.getId(), intervalStartDate, intervalEndDate);
		} else {
			dvDates = derivedMeasureValueRepository
					.findDatesForUserInRoleIdAndDetectionVariableIdForInterval(uir.getId(), dv.getId(), intervalStartDate, intervalEndDate);
		}
		return dvDates;
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
	public List<Double> calculateCorrelationCoefficientsForOneUser(DetectionVariable overall, DetectionVariable dv,
			List<Double> correlations, Date intervalStartDate, Date intervalEndDate, UserInRole uir) {
		
		// find min and max dates for overall and the selected detection variable and determine the interval common to both
		List<Date> ovlDates = viewGefCalculatedInterpolatedPredictedValuesRepository
				.findDatesForUserInRoleIdAndDetectionVariableIdForInterval(uir.getId(), overall.getId(), intervalStartDate, intervalEndDate);

		List<Date> dvDates = findAllDatesForDetectionVariable(dv, intervalStartDate, intervalEndDate,
				uir);

		correlations = calculateCorrelationCoefficients(overall, dv, correlations, uir, ovlDates, dvDates);
		
		return correlations;
	}
	
	/**
	 * @param overall
	 * @param dv
	 * @param correlations
	 * @param uir
	 * @param ovlDates
	 * @param dvDates
	 * @return 
	 */
	private List<Double> calculateCorrelationCoefficients(DetectionVariable overall, DetectionVariable dv, List<Double> correlations,
			UserInRole uir, List<Date> ovlDates, List<Date> dvDates) {
		
		if (!ovlDates.isEmpty() && !dvDates.isEmpty()) {

			int arraySize = getArraySize(ovlDates, dvDates);

			double ovlValuesDoubles[] = new double[arraySize];
			double detectionVariableValuesDoubles[] = new double[arraySize];

			// find appropriate values for overall and the selected detection variable for the selected interval
			int cnt = findDetectionVariableValues(overall, dv, uir, ovlDates, dvDates, ovlValuesDoubles,
					detectionVariableValuesDoubles);

			// calculate the correlation coefficient
			if (cnt > 2) {

				double matrix[][] = { ovlValuesDoubles, detectionVariableValuesDoubles };

				RealMatrix mat = MatrixUtils.createRealMatrix(matrix);
				mat = mat.transpose();

				PearsonsCorrelation corrP = new PearsonsCorrelation(mat);

				RealMatrix corrMatrix = corrP.getCorrelationPValues();

				double corrPValue = corrMatrix.getEntry(0, 1);

				if (corrPValue <= 0.05) {
					double corr = new PearsonsCorrelation().correlation(ovlValuesDoubles,
							detectionVariableValuesDoubles);

					correlations.add(corr);
				} else {
					logger.info("p value previse visoka!!!");
				}

			} else {
				logger.info("nema dovoljno podataka!!!");
			}
		}
		
		return correlations;
	}
	
	/**
	 * @param ovlDates
	 * @param dvDates
	 * @return
	 */
	private int getArraySize(List<Date> ovlDates, List<Date> dvDates) {
		int arraySize = 0;
		if (ovlDates.size() <= dvDates.size())
			arraySize = ovlDates.size();
		else
			arraySize = dvDates.size();
		return arraySize;
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
		
		if (comparison) {
			
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
				if (comp) {
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
	
	/**
	 * @param comparison
	 * @param data
	 * @return
	 */
	@Override
	public List<GroupAnalyticsSeries> createSeries(boolean comparison, List<List<String>> data) {
		GroupAnalyticsSeries serie = new GroupAnalyticsSeries();
		List<GroupAnalyticsSeries> series = new ArrayList<GroupAnalyticsSeries>();
		
		String previousPilot = data.get(0).get(5);
		String previousDetectionVariableName = data.get(0).get(3);

		String firstPilot = data.get(0).get(5);

		serie.setName(previousDetectionVariableName);
		serie.setPilot(previousPilot);

		List<BigDecimal> items = new ArrayList<BigDecimal>();

		for (List<String> entry : data) {

			if (!(previousDetectionVariableName.contains(entry.get(3)) && previousPilot.contains(entry.get(5)))) {

				previousDetectionVariableName = entry.get(3);
				previousPilot = entry.get(5);

				serie.setItems(items);
				series.add(serie);
				items = new ArrayList<BigDecimal>();

				serie = new GroupAnalyticsSeries();

				serie.setName(entry.get(3));
				serie.setPilot(previousPilot);
				
				if (comparison && !entry.get(5).equals(firstPilot)) {
					serie.setAssignedToY2("on");
					serie.setDisplayInLegend("off");
				}

			}

			if (entry.get(0).matches("")) {
				items.add(null);
			} else {
				if (comparison && entry.get(5).equals(firstPilot)) {
					items.add(BigDecimal.valueOf(0 - Double.parseDouble(entry.get(0))));
				} else {
					items.add(BigDecimal.valueOf(Double.parseDouble(entry.get(0))));
				}
			}

		}
		serie.setItems(items);
		series.add(serie);
		return series;
	}

	@Override
	public List<Object> createGroups(List<String> categories,
			HashMap<String, List<String>> socioEconomics, List<String> datesStringList, boolean comparison, boolean comp) {

		int cnt = categories.size();

		List<GroupAnalyticsGroups> groups = new ArrayList<GroupAnalyticsGroups>();

		if (cnt - 1 == 0) {
			if (!comp || comparison) {				
				GroupAnalyticsGroups group1 = new GroupAnalyticsGroups();
				group1.setGroups(socioEconomics.get(categories.get(cnt - 1)));
				return new ArrayList<Object>(socioEconomics.get(categories.get(cnt - 1)));
			} else {
				for (String category1 : socioEconomics.get(categories.get(cnt - 1))) {
					GroupAnalyticsGroups group1 = new GroupAnalyticsGroups();
					group1.setName(category1);
					group1.setGroups(datesStringList);
					groups.add(group1);
				}
				return new ArrayList<Object>(groups);
			}
		} else {

			ArrayList<String> currCategories = new ArrayList<String> ();
			for (int i = 0; i < cnt - 1; i++) 
				currCategories.add(categories.get(i));

			String type = categories.get(cnt - 1);

			List<Object> alreadyCreatedGroups = createGroups(currCategories, socioEconomics, datesStringList, comparison, comp);

			for (String category : socioEconomics.get(type)) {
				GroupAnalyticsGroups group = new GroupAnalyticsGroups();
				group.setName(category);
				group.setGroups(alreadyCreatedGroups);
				groups.add(group);

			}
			return new ArrayList<Object>(groups);
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
