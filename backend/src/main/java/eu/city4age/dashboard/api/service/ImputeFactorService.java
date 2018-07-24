package eu.city4age.dashboard.api.service;

import java.util.Date;

import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;

public interface ImputeFactorService {
	
	int imputeMissingValues(DetectionVariable dv, UserInRole uir, Date endDate);

}
