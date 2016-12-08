package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.List;

import eu.city4age.dashboard.api.model.Assessment;

/** Javni API dao-a za asesment.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public interface AssessmentDAO extends BaseDAO {

	List<Object[]> getDiagramDataForUserInRoleId(final Integer userInRoleId, final Timestamp start, final Timestamp end);

	List<Assessment> getLastFiveAssessments();
	
	List<Assessment> getAssessmentsForGeriatricFactorId(final Long geriatricFactorId);

	List<Assessment> getAssessmentsByFilter(final Long geriatricFactorId, final String filter);

}
