package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.AttentionStatus;

@Repository(value = "attentionStatusRepository")
public interface AttentionStatusRepository extends GenericRepository<AttentionStatus, Long> {

}