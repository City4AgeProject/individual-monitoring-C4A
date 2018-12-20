package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.jpa.VmvFilteringRepository;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.VmvFiltering;
import eu.city4age.dashboard.api.service.ExcludeService;

@Component
public class ExcludeServiceImpl implements ExcludeService {

	static protected Logger logger = LogManager.getLogger(ExcludeServiceImpl.class);

	@Autowired
	private VmvFilteringRepository vmvFilteringRepository;

	public void excludeMeasures(List<VariationMeasureValue> vmvMonthly) {

		List<VariationMeasureValue> vmvDaily = new ArrayList<VariationMeasureValue>();
		for(VariationMeasureValue measure : vmvMonthly) {
			if (measure.getTimeInterval().getTypicalPeriod() != null && measure.getTimeInterval().getTypicalPeriod().toLowerCase().equals("day")) {
				if (!(vmvDaily.isEmpty()
						|| (vmvDaily.get(0).getTimeInterval().getId().equals(measure.getTimeInterval().getId())
								&& vmvDaily.get(0).getUserInRole().getId().equals(measure.getUserInRole().getId())))) {
					excludeMeasuresFor1UserAnd1Ti(vmvDaily);
					vmvDaily.clear();
					vmvDaily = new ArrayList<VariationMeasureValue>();
				}
				vmvDaily.add(measure);
			}
		}
		if(!vmvDaily.isEmpty()) {
			excludeMeasuresFor1UserAnd1Ti(vmvDaily);
		}
		vmvDaily.clear();
	}

	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	private void excludeMeasuresFor1UserAnd1Ti(List<VariationMeasureValue> vmvDaily) {

		Boolean allIsDefault = true;

		for(VariationMeasureValue measure : vmvDaily) {
			switch(measure.getDetectionVariable().getDetectionVariableName()) {
			case "home_time":
				if(!measure.getMeasureValue().setScale(0, RoundingMode.HALF_UP).equals((new BigDecimal(86400.0)).setScale(0, RoundingMode.HALF_UP))) {
					allIsDefault = false;
				}
				break;
			case "walk_distance_outdoor_slow_perc":
				if(!measure.getMeasureValue().setScale(0, RoundingMode.HALF_UP).equals((new BigDecimal(100.0)).setScale(0, RoundingMode.HALF_UP))) {
					allIsDefault = false;
				}
				break;	
			default:
				if(!measure.getMeasureValue().setScale(0, RoundingMode.HALF_UP).equals((new BigDecimal(0)).setScale(0, RoundingMode.HALF_UP))) {
					allIsDefault = false;
				}
				break;
			}
		}
		if (allIsDefault) {
			List<VmvFiltering> filterings = new ArrayList<VmvFiltering>();
			for(VariationMeasureValue measure : vmvDaily) {
				VmvFiltering filtering = new VmvFiltering();
				filtering.setFilterType("E");
				filtering.setVmv(measure);
				filtering.setValidFrom(new Date());
				filterings.add(filtering);
			}
			vmvFilteringRepository.bulkSave(filterings);
			vmvFilteringRepository.flush();
			filterings.clear();
		}
	}
}
