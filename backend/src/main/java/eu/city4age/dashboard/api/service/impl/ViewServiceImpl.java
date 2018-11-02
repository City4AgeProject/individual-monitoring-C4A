package eu.city4age.dashboard.api.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
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
import eu.city4age.dashboard.api.pojo.dto.AnalyticsDiagramData;
import eu.city4age.dashboard.api.pojo.dto.GenericTableData;
import eu.city4age.dashboard.api.pojo.dto.JsonToExcel;
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
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

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
		logger.info("createAllFilters");
		logger.info("allVariablesFilters size: " + allVariablesFilters.size());
		logger.info("allPilotsFilters size: " + allPilotsFilters.size());
		logger.info("allCategoryFilters size: " + allCategoryFilters.size());
		logger.info("allTimesFilters size: " + allTimesFilters.size());
		logger.info("allVariablesFilters isEmpty: " + allVariablesFilters.isEmpty());
		logger.info("allPilotsFilters isEmpty: " + allPilotsFilters.isEmpty());
		logger.info("allCategoryFilters isEmpty: " + allCategoryFilters.isEmpty());
		logger.info("allTimesFilters isEmpty: " + allTimesFilters.isEmpty());
		
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
	
	@Override
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
	
	@Override
	public void writeToXlsx (int viewSelecter, List<String> categories, List<AnalyticsDiagramData> data, File tmp) throws IOException {
		
		List<String> headers = new ArrayList<String> ();
		
		Map<String, String> categoryNames = new HashMap <String, String> ();
		
		categoryNames.put("sex", "Sex");
		categoryNames.put("marital_status", "Marital Status");
		categoryNames.put("age_group", "Age Group");
		categoryNames.put("education", "Education");
		categoryNames.put("cohabiting", "Cohabiting");
		categoryNames.put("informal_caregiver_ability", "Informal Caregiver Availability");
		categoryNames.put("quality_housing", "Quality Of Housing");
		categoryNames.put("quality_neighborhood", "Quality Of Neighborhood");
		categoryNames.put("working", "Working");
		
		headers.add("Pilot");
		headers.add("Variable Name");
		headers.add("Variable Type");
		
		switch (viewSelecter) {
		case 1:
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			break;
		case 2:
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			for (String c : categories) {
				headers.add(categoryNames.get(c));
			}
			break;
		case 3: 
			headers.add("Interval Start");
			headers.add("Typical Period");
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			break;
		case 4:
			headers.add("Interval Start");
			headers.add("Typical Period");
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			for (String c : categories) {
				headers.add(categoryNames.get(c));
			}
			break;
		default: return;
		}
		
		Workbook workbook = new XSSFWorkbook();
		
		workbook.getCreationHelper();
		
		Sheet sheet = workbook.createSheet("Data");
		
		Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);
        //headerFont.setFontName("Times New Roman");

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setBorderTop(BorderStyle.DOUBLE);
        headerCellStyle.setBorderBottom(BorderStyle.DOUBLE);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle leftHeaderCellStyle = workbook.createCellStyle();
        leftHeaderCellStyle.setFont(headerFont);
        leftHeaderCellStyle.setBorderTop(BorderStyle.DOUBLE);
        leftHeaderCellStyle.setBorderBottom(BorderStyle.DOUBLE);
        leftHeaderCellStyle.setBorderLeft(BorderStyle.DOUBLE);
        leftHeaderCellStyle.setBorderRight(BorderStyle.THIN);
        leftHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle rightHeaderCellStyle = workbook.createCellStyle();
        rightHeaderCellStyle.setFont(headerFont);
        rightHeaderCellStyle.setBorderTop(BorderStyle.DOUBLE);
        rightHeaderCellStyle.setBorderBottom(BorderStyle.DOUBLE);
        rightHeaderCellStyle.setBorderLeft(BorderStyle.THIN);
        rightHeaderCellStyle.setBorderRight(BorderStyle.DOUBLE);
        rightHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);

        // Create a Row
        Row headerRow = sheet.createRow(0);
        
        // Create cells
        for(int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            if (i == 0)
            	cell.setCellStyle(leftHeaderCellStyle);
            else if (i == headers.size() - 1)
            	cell.setCellStyle(rightHeaderCellStyle);
            else 
            	cell.setCellStyle(headerCellStyle);
        }
        
        int rowNum = 1;
        for( AnalyticsDiagramData singleRow: data) {
        	
        	Cell cell;
            Row row = sheet.createRow(rowNum++);

            cell = row.createCell(0);
            cell.setCellValue(singleRow.getPilot());
            cell.setCellStyle(dataCellStyle);

            cell = row.createCell(1);
            cell.setCellValue(singleRow.getDetectionVariableName());
            cell.setCellStyle(dataCellStyle);

            cell = row.createCell(2);
            cell.setCellValue(singleRow.getDetectionVariableType());
            cell.setCellStyle(dataCellStyle);
            
            switch (viewSelecter) {
            case 1:
            	if (singleRow.getAvgValue() != null) {
            		cell = row.createCell(3);
            		cell.setCellValue(singleRow.getAvgValue().doubleValue());
            		cell.setCellStyle(dataCellStyle);
            	}
            	else {
            		cell = row.createCell(3);
            		cell.setCellValue("");
            		cell.setCellStyle(dataCellStyle);
            	}
            	cell = row.createCell(4);
            	cell.setCellValue(singleRow.getCount());
            	cell.setCellStyle(dataCellStyle);
            	break;
            case 2:
            	if (singleRow.getAvgValue() != null) {
            		cell = row.createCell(3);
            		cell.setCellValue(singleRow.getAvgValue().doubleValue());
            		cell.setCellStyle(dataCellStyle);
            	}
            	else {
            		cell = row.createCell(3);
            		cell.setCellValue("");
            		cell.setCellStyle(dataCellStyle);
            	}
            	cell = row.createCell(4);
            	cell.setCellValue(singleRow.getCount());
            	cell.setCellStyle(dataCellStyle);
            	int i = 0;
    			for (String c : categories) {
    				cell = row.createCell(5 + i);
    				cell.setCellValue(singleRow.getCategory().get(c));
    				cell.setCellStyle(dataCellStyle);
    				i++;
    			}
    			break;
            case 3:
            	cell = row.createCell(3);
            	cell.setCellValue(singleRow.getIntervalStart());
            	cell.setCellStyle(dataCellStyle);
            	
            	cell = row.createCell(4);
            	cell.setCellValue(singleRow.getTypicalPeriod());
            	cell.setCellStyle(dataCellStyle);
            	if (singleRow.getAvgValue() != null) {
            		cell = row.createCell(5);
            		cell.setCellValue(singleRow.getAvgValue().doubleValue());
            		cell.setCellStyle(dataCellStyle);
            	}
            	else {
            		cell = row.createCell(5);
            		cell.setCellValue("");
            		cell.setCellStyle(dataCellStyle);
            	}
            	cell = row.createCell(6);
            	cell.setCellValue(singleRow.getCount());
            	cell.setCellStyle(dataCellStyle);
    			break;
            case 4:
            	cell = row.createCell(3);
            	cell.setCellValue(singleRow.getIntervalStart());
            	cell.setCellStyle(dataCellStyle);
            	
            	cell = row.createCell(4);
            	cell.setCellValue(singleRow.getTypicalPeriod());
            	cell.setCellStyle(dataCellStyle);
            	if (singleRow.getAvgValue() != null) {
            		cell = row.createCell(5);
            		cell.setCellValue(singleRow.getAvgValue().doubleValue());
            		cell.setCellStyle(dataCellStyle);
            	}
            	else {
            		cell = row.createCell(5);
            		cell.setCellValue("");
            		cell.setCellStyle(dataCellStyle);
            	}
            	cell = row.createCell(6);
            	cell.setCellValue(singleRow.getCount());
            	cell.setCellStyle(dataCellStyle);
				int ii = 0;
				for (String c : categories) {
					cell = row.createCell(7 + ii);
    				cell.setCellValue(singleRow.getCategory().get(c));
    				cell.setCellStyle(dataCellStyle);
    				ii++;
    			}
    			break;
        	default: return;
            }
        }
        
        for(int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }
		
		FileOutputStream fos = new FileOutputStream(tmp);
		workbook.write(fos);
		fos.close();
		
		workbook.close();
	}
	
	@Override
	public void writeToXls (int viewSelecter, List<String> categories, List<AnalyticsDiagramData> data, File tmp) throws IOException {
		
		List<String> headers = new ArrayList<String> ();
		
		Map<String, String> categoryNames = new HashMap <String, String> ();
		
		categoryNames.put("sex", "Sex");
		categoryNames.put("marital_status", "Marital Status");
		categoryNames.put("age_group", "Age Group");
		categoryNames.put("education", "Education");
		categoryNames.put("cohabiting", "Cohabiting");
		categoryNames.put("informal_caregiver_ability", "Informal Caregiver Availability");
		categoryNames.put("quality_housing", "Quality Of Housing");
		categoryNames.put("quality_neighborhood", "Quality Of Neighborhood");
		categoryNames.put("working", "Working");
		
		headers.add("Pilot");
		headers.add("Variable Name");
		headers.add("Variable Type");
		
		switch (viewSelecter) {
		case 1:
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			break;
		case 2:
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			for (String c : categories) {
				headers.add(categoryNames.get(c));
			}
			break;
		case 3: 
			headers.add("Interval Start");
			headers.add("Typical Period");
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			break;
		case 4:
			headers.add("Interval Start");
			headers.add("Typical Period");
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			for (String c : categories) {
				headers.add(categoryNames.get(c));
			}
			break;
		default: return;
		}
		
		Workbook workbook = new HSSFWorkbook();
		
		workbook.getCreationHelper();
		
		Sheet sheet = workbook.createSheet("Data");
		
		Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);
        //headerFont.setFontName("Times New Roman");

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setBorderTop(BorderStyle.DOUBLE);
        headerCellStyle.setBorderBottom(BorderStyle.DOUBLE);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle leftHeaderCellStyle = workbook.createCellStyle();
        leftHeaderCellStyle.setFont(headerFont);
        leftHeaderCellStyle.setBorderTop(BorderStyle.DOUBLE);
        leftHeaderCellStyle.setBorderBottom(BorderStyle.DOUBLE);
        leftHeaderCellStyle.setBorderLeft(BorderStyle.DOUBLE);
        leftHeaderCellStyle.setBorderRight(BorderStyle.THIN);
        leftHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle rightHeaderCellStyle = workbook.createCellStyle();
        rightHeaderCellStyle.setFont(headerFont);
        rightHeaderCellStyle.setBorderTop(BorderStyle.DOUBLE);
        rightHeaderCellStyle.setBorderBottom(BorderStyle.DOUBLE);
        rightHeaderCellStyle.setBorderLeft(BorderStyle.THIN);
        rightHeaderCellStyle.setBorderRight(BorderStyle.DOUBLE);
        rightHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);

        // Create a Row
        Row headerRow = sheet.createRow(0);
        
        // Create cells
        for(int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            if (i == 0)
            	cell.setCellStyle(leftHeaderCellStyle);
            else if (i == headers.size() - 1)
            	cell.setCellStyle(rightHeaderCellStyle);
            else 
            	cell.setCellStyle(headerCellStyle);
        }
        
        int rowNum = 1;
        for( AnalyticsDiagramData singleRow: data) {
        	
        	Cell cell;
            Row row = sheet.createRow(rowNum++);

            cell = row.createCell(0);
            cell.setCellValue(singleRow.getPilot());
            cell.setCellStyle(dataCellStyle);

            cell = row.createCell(1);
            cell.setCellValue(singleRow.getDetectionVariableName());
            cell.setCellStyle(dataCellStyle);

            cell = row.createCell(2);
            cell.setCellValue(singleRow.getDetectionVariableType());
            cell.setCellStyle(dataCellStyle);
            
            switch (viewSelecter) {
            case 1:
            	if (singleRow.getAvgValue() != null) {
            		cell = row.createCell(3);
            		cell.setCellValue(singleRow.getAvgValue().doubleValue());
            		cell.setCellStyle(dataCellStyle);
            	}
            	else {
            		cell = row.createCell(3);
            		cell.setCellValue("");
            		cell.setCellStyle(dataCellStyle);
            	}
            	cell = row.createCell(4);
            	cell.setCellValue(singleRow.getCount());
            	cell.setCellStyle(dataCellStyle);
            	break;
            case 2:
            	if (singleRow.getAvgValue() != null) {
            		cell = row.createCell(3);
            		cell.setCellValue(singleRow.getAvgValue().doubleValue());
            		cell.setCellStyle(dataCellStyle);
            	}
            	else {
            		cell = row.createCell(3);
            		cell.setCellValue("");
            		cell.setCellStyle(dataCellStyle);
            	}
            	cell = row.createCell(4);
            	cell.setCellValue(singleRow.getCount());
            	cell.setCellStyle(dataCellStyle);
            	int i = 0;
    			for (String c : categories) {
    				cell = row.createCell(5 + i);
    				cell.setCellValue(singleRow.getCategory().get(c));
    				cell.setCellStyle(dataCellStyle);
    				i++;
    			}
    			break;
            case 3:
            	cell = row.createCell(3);
            	cell.setCellValue(singleRow.getIntervalStart());
            	cell.setCellStyle(dataCellStyle);
            	
            	cell = row.createCell(4);
            	cell.setCellValue(singleRow.getTypicalPeriod());
            	cell.setCellStyle(dataCellStyle);
            	if (singleRow.getAvgValue() != null) {
            		cell = row.createCell(5);
            		cell.setCellValue(singleRow.getAvgValue().doubleValue());
            		cell.setCellStyle(dataCellStyle);
            	}
            	else {
            		cell = row.createCell(5);
            		cell.setCellValue("");
            		cell.setCellStyle(dataCellStyle);
            	}
            	cell = row.createCell(6);
            	cell.setCellValue(singleRow.getCount());
            	cell.setCellStyle(dataCellStyle);
    			break;
            case 4:
            	cell = row.createCell(3);
            	cell.setCellValue(singleRow.getIntervalStart());
            	cell.setCellStyle(dataCellStyle);
            	
            	cell = row.createCell(4);
            	cell.setCellValue(singleRow.getTypicalPeriod());
            	cell.setCellStyle(dataCellStyle);
            	if (singleRow.getAvgValue() != null) {
            		cell = row.createCell(5);
            		cell.setCellValue(singleRow.getAvgValue().doubleValue());
            		cell.setCellStyle(dataCellStyle);
            	}
            	else {
            		cell = row.createCell(5);
            		cell.setCellValue("");
            		cell.setCellStyle(dataCellStyle);
            	}
            	cell = row.createCell(6);
            	cell.setCellValue(singleRow.getCount());
            	cell.setCellStyle(dataCellStyle);
				int ii = 0;
				for (String c : categories) {
					cell = row.createCell(7 + ii);
    				cell.setCellValue(singleRow.getCategory().get(c));
    				cell.setCellStyle(dataCellStyle);
    				ii++;
    			}
    			break;
        	default: return;
            }
        }
        
        for(int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }
		
		FileOutputStream fos = new FileOutputStream(tmp);
		workbook.write(fos);
		fos.close();
		
		workbook.close();
	}
	
	@Override
	public void writeToJSON (GenericTableData data, File tmp) throws IOException {
		String json = objectMapper.writeValueAsString(data);
		FileOutputStream fos = new FileOutputStream(tmp);
		fos.write(json.getBytes());
		fos.close();
	}
	
	/*
		socioEconomics.put("sex", Arrays.asList("m", "f"));
		socioEconomics.put("marital_status", Arrays.asList("s", "m", "w", "d", "t"));
		socioEconomics.put("age_group", Arrays.asList("50-59", "60-69", "70-79", "80-89", "90+"));
		socioEconomics.put("education", Arrays.asList("none", "primary", "secondary", "tertiary"));
		socioEconomics.put("cohabiting", Arrays.asList("alone", "family", "friends", "other"));
		socioEconomics.put("informal_caregiver_ability", Arrays.asList("t", "f"));
		socioEconomics.put("quality_housing", Arrays.asList("low", "average", "high"));
		socioEconomics.put("quality_neighborhood", Arrays.asList("low", "average", "high"));
		socioEconomics.put("working", Arrays.asList("t", "f"));*/
	
	@SuppressWarnings("unchecked")
	@Override
	public GenericTableData addGenericTableData(ArrayList<Filter> filter, Object[] data, Boolean comp,
			GenericTableData tableData, List<String> pilotCodes) {
		logger.info("addGenericTableData");
		logger.info("tableData.getHeaders().size(): " + tableData.getHeaders().size());
		logger.info("filter size: " + filter.size());
		if(tableData.getHeaders().size() == 0) {
			logger.info("inside if");
			tableData.getHeaders().add("avgValue");
			tableData.getHeaders().add("count");
			for (Filter f : filter) {
				logger.info("inside for");
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
			row.add(null);
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
		
		logger.info("tableData size pre: " + tableData.getData().size());
		
		tableData.getData().add(row);
		
		logger.info("tableData size posle: " + tableData.getData().size());

		return tableData;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AnalyticsDiagramData createAnalyticsDiagramData (ArrayList<Filter> filter, Object[] data, Boolean comparison) {
		
		AnalyticsDiagramData ar = new AnalyticsDiagramData ();
		Map<String, String> arCategories = new LinkedHashMap <String, String> ();
		
		for (Filter f : filter) {
			
			switch (f.getName()) {
			case "detectionVariable":
				DetectionVariable dv = detectionVariableRepository.getOne((Long) f.getInParams().entrySet().iterator().next().getValue());
				ar.setDetectionVariable(dv.getId());
				ar.setDetectionVariableName(dv.getDetectionVariableName());
				ar.setDetectionVariableType(dv.getDetectionVariableType().toString());
				break;
			case "pilot":
				if (comparison)
					ar.setPilot((String) f.getInParams().entrySet().iterator().next().getValue());	
				else {
					ArrayList<String> list = (ArrayList<String>) f.getInParams().entrySet().iterator().next().getValue();
					StringBuilder pilotStringBuilder = new StringBuilder ();
					
					for (int i = 0; i < list.size() - 1; i++)
						pilotStringBuilder.append(list.get(i)).append(", ");
					pilotStringBuilder.append(list.get(list.size() - 1));
					ar.setPilot(pilotStringBuilder.toString());
				}
				break;
			case "intervalStart":
				ar.setIntervalStartJSON (OffsetDateTime.ofInstant(((Timestamp) f.getInParams().entrySet().iterator().next().getValue()).toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy/MM")));
				ar.setIntervalStart (OffsetDateTime.ofInstant(((Timestamp) f.getInParams().entrySet().iterator().next().getValue()).toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)));
				ar.setTypicalPeriod("month");
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
				arCategories.put(f.getName(), (String) f.getInParams().entrySet().iterator().next().getValue());
			}	
		}
		
		ar.setCategory(arCategories);
		
		if (data[0] != null)
			ar.setAvgValue(BigDecimal.valueOf((Double)data[0]).setScale(3, RoundingMode.HALF_UP));
		else 
			ar.setAvgValue(null);
		if (data[1] != null)
			ar.setCount((Long) data[1]);
		else 
			ar.setCount(0l);
		
		return ar;
	}
	
	@Override
	public JsonToExcel createExcelJson (List<AnalyticsDiagramData> data, List<String> categories, int viewSelecter) {
		
		JsonToExcel result = new JsonToExcel ();
		
		List<String> headers = new ArrayList<String> ();
		
		Map<String, String> categoryNames = new HashMap <String, String> ();
		
		categoryNames.put("sex", "Sex");
		categoryNames.put("marital_status", "Marital Status");
		categoryNames.put("age_group", "Age Group");
		categoryNames.put("education", "Education");
		categoryNames.put("cohabiting", "Cohabiting");
		categoryNames.put("informal_caregiver_ability", "Informal Caregiver Availability");
		categoryNames.put("quality_housing", "Quality Of Housing");
		categoryNames.put("quality_neighborhood", "Quality Of Neighborhood");
		categoryNames.put("working", "Working");
		
		headers.add("Pilot");
		headers.add("Variable Name");
		headers.add("Variable Type");
		
		switch (viewSelecter) {
		case 1:
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			break;
		case 2:
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			for (String c : categories) {
				headers.add(categoryNames.get(c));
			}
			break;
		case 3: 
			headers.add("Interval Start");
			headers.add("Typical Period");
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			break;
		case 4:
			headers.add("Interval Start");
			headers.add("Typical Period");
			headers.add("Average Value");
			headers.add("Num Of CR-s");
			for (String c : categories) {
				headers.add(categoryNames.get(c));
			}
			break;
		default: return null;
		}
		
		List<String[]> dataList = new ArrayList<String[]> ();
		
		for (AnalyticsDiagramData d : data) {
			String[] dataString = new String[headers.size()];
			
			dataString[0] = d.getPilot();
			dataString[1] = d.getDetectionVariableName();
			dataString[2] = d.getDetectionVariableType();
			
			switch (viewSelecter) {
			case 1:
            	if (d.getAvgValue() != null) {
            		dataString[3] = d.getAvgValue().toString();
            	}
            	else {
            		dataString[3] = "";
            	}
            	dataString[4] = d.getCount().toString();
            	break;
			case 2:
				if (d.getAvgValue() != null) {
            		dataString[3] = d.getAvgValue().toString();
            	}
            	else {
            		dataString[3] = "";
            	}
            	dataString[4] = d.getCount().toString();
            	
            	int i = 0;
    			for (String c : categories) {
    				dataString[5 + i] = d.getCategory().get(c);
    				i++;
    			}
    			break;
			case 3:
				dataString[3] = d.getIntervalStart();
				dataString[4] = d.getTypicalPeriod();
				if (d.getAvgValue() != null) {
            		dataString[5] = d.getAvgValue().toString();
            	}
            	else {
            		dataString[5] = "";
            	}
            	dataString[6] = d.getCount().toString();
            	break;
			case 4:
				dataString[3] = d.getIntervalStart();
				dataString[4] = d.getTypicalPeriod();
				if (d.getAvgValue() != null) {
            		dataString[5] = d.getAvgValue().toString();
            	}
            	else {
            		dataString[5] = "";
            	}
            	dataString[6] = d.getCount().toString();
            	int ii = 0;
    			for (String c : categories) {
    				dataString[7 + ii] = d.getCategory().get(c);
    				ii++;
    			}
    			break;
			}
			
			dataList.add(dataString);
		}
		
		result.setHeaders(headers);
		result.setData(dataList);
		return result;
	}



}
