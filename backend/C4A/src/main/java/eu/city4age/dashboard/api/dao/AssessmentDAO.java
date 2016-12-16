package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.List;

import eu.city4age.dashboard.api.domain.OrderBy;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;

/** Javni API dao-a za asesment.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public interface AssessmentDAO extends BaseDAO {

	List<TimeInterval> getDiagramDataForUserInRoleId(final Long crId, final Long dvParentId, final Timestamp start, final Timestamp end);

	List<TimeInterval> getLastFiveAssessmentsForDiagram(final Integer crId, final Timestamp start, final Timestamp end);

	List<GeriatricFactorValue> getAssessmentsForSelectedDataSet(final List<Long> geriatricFactorIds, final List<Boolean> status, final Short authorRoleId, final OrderBy orderBy);

}
