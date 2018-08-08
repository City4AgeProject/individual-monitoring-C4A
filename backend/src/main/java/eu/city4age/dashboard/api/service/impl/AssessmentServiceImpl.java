package eu.city4age.dashboard.api.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.core.PathSegment;

import org.springframework.stereotype.Component;

import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramLast5Assessment;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValueLastFiveAssessment;
import eu.city4age.dashboard.api.pojo.dto.oj.Serie;
import eu.city4age.dashboard.api.service.AssessmentService;

@Component
public class AssessmentServiceImpl implements AssessmentService {
	
	public OJDiagramLast5Assessment transformToOJ(List<Object[]> lfas) {

		OJDiagramLast5Assessment ojLfa = new OJDiagramLast5Assessment();

		for (Object[] lfa : lfas) {
			ojLfa.getGroups().add(new DataIdValue(((Integer) lfa[0]).longValue(), lfa[1].toString()));//(time_interval_id, interval_start)
		}
		
		ojLfa.getSeries().add(new Serie("Only", new HashSet<DataIdValueLastFiveAssessment>(), "", "20px", 32, "on", "none"));


		for (DataIdValue group : ojLfa.getGroups()) {

			for (int i = 0; i < lfas.size(); i++) {

				if (group.getId().equals(new Long(((Integer)lfas.get(i)[0]).longValue()))) {//time_interval_id

					Serie serie = ojLfa.getSeries().iterator().next();

					if (lfas.get(i)[4] != null) {//assessment_id
						DataIdValueLastFiveAssessment item = new DataIdValueLastFiveAssessment();
						item.setId(((Integer)lfas.get(i)[2]).longValue());//gef_id
						item.setValue(lfas.get(i)[3].toString());//gef_value
						item.getAssessmentObjects().add(lfas.get(i));

						for (Object[] lfa : lfas) {
							if (lfa[4] != null && group.getId().equals(new Long(((Integer)lfa[0]).longValue()))//assessment_id, time_interval_id
									&& lfas.get(i)[3].equals(lfa[3])//gef_value
									&& !lfas.get(i)[4].equals(lfa[4])) {//assessment_id

								item.getAssessmentObjects().add(lfa);
							}
						}

						serie.getItems().add(item);
					}
				

				}

			}
		}
		
		ojLfa.setGroups(new HashSet<DataIdValue>());

		return ojLfa;

	}
	
	public List<Assessment> orderByForFiltering(List<Assessment> list, Long orderById) {

		switch (orderById.intValue()) {
		case 1:
			list.sort(Comparator.comparing(Assessment::getCreated));
			break;
		case 2:
			list.sort(Comparator.comparing(Assessment::getCreated).reversed());
			break;
		case 3:
			list.sort(Comparator.comparing(Assessment::getUserInSystemDisplayName));
			break;
		case 4:
			list.sort(Comparator.comparing(Assessment::getUserInSystemDisplayName).reversed());
			break;
		case 5:
			list.sort(Comparator.comparing(Assessment::getRoleId));
			break;
		case 6:
			list.sort(Comparator.comparing(Assessment::getRoleId).reversed());
			break;
		case 7:
			break;
		}

		return list;
	}
	
	public List<Long> convertToListLong(List<PathSegment> ids) {
		List<Long> idsList = new ArrayList<Long>(ids.size());
		for (PathSegment segment : ids) {
			idsList.add(Long.valueOf(segment.toString()));
		}
		return idsList;
	}

}
