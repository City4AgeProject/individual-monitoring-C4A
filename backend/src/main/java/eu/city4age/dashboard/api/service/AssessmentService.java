package eu.city4age.dashboard.api.service;

import java.util.List;

import javax.ws.rs.core.PathSegment;

import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramLast5Assessment;

public interface AssessmentService {
	
	OJDiagramLast5Assessment transformToOJ(List<Object[]> lfas);
	
	List<Assessment> orderByForFiltering(List<Assessment> list, Long orderById);
	
	List<Long> convertToListLong(List<PathSegment> ids);

}
