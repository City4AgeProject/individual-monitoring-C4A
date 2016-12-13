package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.List;

import eu.city4age.dashboard.api.domain.OrderBy;
import eu.city4age.dashboard.api.dto.DiagramQuerryDTO;
import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;

/** Javni API dao-a za asesment.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public interface AssessmentDAO extends BaseDAO {

	List<DiagramQuerryDTO> getDiagramDataForUserInRoleId(final Integer crId, final Timestamp start, final Timestamp end);
	
	List<GeriatricFactorValue> getDiagramDataForUserInRoleId(final Integer crId, final Short dvParentId, final Timestamp start, final Timestamp end);

	List<GeriatricFactorValue> getLastFiveAssessmentsForDiagram(final Integer crId, final Timestamp start, final Timestamp end);
	
	List<Assessment> getAssessmentsForGeriatricFactorId(final Long geriatricFactorId);

	List<Object[]> getAssessmentsByFilter(final Long geriatricFactorId, final List<Boolean> status, final Short authorRoleId, final OrderBy orderBy);

}
