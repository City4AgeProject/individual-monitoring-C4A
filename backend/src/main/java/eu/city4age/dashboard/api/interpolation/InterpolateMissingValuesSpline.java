package eu.city4age.dashboard.api.interpolation;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

//import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.jpa.GeriatricFactorInterpolationValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorInterpolationValue;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.rest.MeasuresService;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/*
 * author: Vladimir Aleksic
 */

public class InterpolateMissingValuesSpline {
	
	@Autowired
	private MeasuresService measuresService;

	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Autowired
	private GeriatricFactorInterpolationValueRepository geriatricFactorInterpolationValueRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private PilotRepository pilotRepository;
	
	//private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	static protected Logger logger = LogManager.getLogger(InterpolateMissingValuesSpline.class);

	//private Long dvId, uId;
	private ArrayList<BigDecimal>gfValues;
	private ArrayList<Integer>intIntervalsExisting;
	private ArrayList<Integer>intIntervalsMissing;
	private ArrayList<TimeInterval>tiIntervaslMissing;
	private List<GeriatricFactorValue> geriatricFactorValue;
	private GeriatricFactorValue gf0;
	private int month, year, monthCounter, gfIndex, i;
	private String strDate;
	private TimeInterval ti;
	private Calendar prevDate=Calendar.getInstance(), endDate=Calendar.getInstance(), currentDate=Calendar.getInstance();
	private SplineInterpolator splineInterpolator=new SplineInterpolator();
	private PolynomialSplineFunction polynomialSplineFunction;
	private double[] x, y;
	private double rawX, interpolatedY;
	private GeriatricFactorInterpolationValue gefi;
	private Date latestVariablesComputed;
	
	public int getData(Long dvId, Long uId) {
		intIntervalsExisting=new ArrayList<Integer>();
		intIntervalsMissing=new ArrayList<Integer>();
		tiIntervaslMissing=new ArrayList<TimeInterval>();
		gfValues=new ArrayList<BigDecimal>();
		prevDate=Calendar.getInstance();
		monthCounter=0;
		gfIndex=0;

		latestVariablesComputed=pilotRepository.findByPilotCode(userInRoleRepository.findByUirId(uId).getPilotCode()).getLatestVariablesComputed();
		geriatricFactorValue=geriatricFactorRepository.findByDetectionVariableId(dvId, uId);
		if(geriatricFactorValue.size()<4) {
			return -1; //Ne radimo, nema dovoljno. BTW i polySpline baca exception kad ih ima manje od 3
		}
		if(latestVariablesComputed==null) {
		this.endDate.setTime(geriatricFactorValue.get(geriatricFactorValue.size()-1)
							.getTimeInterval().getIntervalStart());
		}else {
			this.endDate.setTime(latestVariablesComputed);
		}
		
		prevDate.setTime(geriatricFactorValue.get(0).getTimeInterval().getIntervalStart());
		while(prevDate.getTimeInMillis() <= endDate.getTimeInMillis()) {
			currentDate.setTime(geriatricFactorValue.get(gfIndex).getTimeInterval().getIntervalStart());
//			logger.info("currentDate: " + currentDate.get(Calendar.MONTH) + "/" + currentDate.get(Calendar.DATE));
			month=prevDate.get(Calendar.MONTH);
			year=prevDate.get(Calendar.YEAR);
			while(prevDate.getTimeInMillis() < currentDate.getTimeInMillis()) {
//				logger.info("prevDate: " + prevDate.get(Calendar.MONTH) + "/" + prevDate.get(Calendar.DATE));
				tiIntervaslMissing.add(ti);
				intIntervalsMissing.add(monthCounter);
				monthCounter++;
				month++;
				if(month==12) {
					month=0;
					year++;
				}
				strDate=String.format("%4d-%02d-01 00:00:00", year, month+1);
				ti = measuresService
						.getOrCreateTimeInterval(Timestamp.valueOf(strDate),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
				prevDate.setTime(ti.getIntervalStart());
			}
			intIntervalsExisting.add(monthCounter);
			gfValues.add(geriatricFactorValue.get(gfIndex).getGefValue());
			gfIndex++;
			monthCounter++;
			month++;
			if(month==12) {
				month=0;
				year++;
			}
			strDate=String.format("%4d-%02d-01 00:00:00", year, month+1);
			ti = measuresService
					.getOrCreateTimeInterval(Timestamp.valueOf(strDate),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
			prevDate.setTime(ti.getIntervalStart());
//			logger.info(prevDate);
		}
		
		x=new double[gfIndex];
		y=new double[gfIndex];
		for(int i=0;i<gfIndex;i++) {
			x[i]=intIntervalsExisting.get(i).doubleValue();
			y[i]=gfValues.get(i).doubleValue();
		}
		
		polynomialSplineFunction=splineInterpolator.interpolate(x, y);
		gf0=geriatricFactorValue.get(0);
		
		
		for(i=0;i<intIntervalsMissing.size();i++) {
			
			rawX=intIntervalsMissing.get(i).doubleValue();
			interpolatedY=polynomialSplineFunction.value(rawX);
//			logger.info("X: " + rawX + "Y: " + interpolatedY);
			
			gefi = new GeriatricFactorInterpolationValue();
			gefi.setUserInRole(gf0.getUserInRole());
			gefi.setUserInRoleId(gf0.getUserInRoleId());
			gefi.setDetectionVariable(gf0.getDetectionVariable());
			gefi.setDetectionVariableId(gf0.getDetectionVariableId());
			gefi.setTimeInterval(tiIntervaslMissing.get(i));
			gefi.setGefValue(BigDecimal.valueOf(interpolatedY));
			gefi = geriatricFactorInterpolationValueRepository.save(gefi);

		}
		
		return intIntervalsMissing.size();
	}
	
}
