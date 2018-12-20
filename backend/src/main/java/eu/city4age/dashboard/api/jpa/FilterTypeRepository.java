package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.FilterType;

@Repository(value = "filterTypeRepository")
public interface FilterTypeRepository extends GenericRepository<FilterType, Character> {

}
