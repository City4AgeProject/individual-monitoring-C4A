package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewGefValuesPersistedSourceGesTypes;

@Repository(value = "viewGefValuesPersistedSourceGesTypesRepository")
public interface ViewGefValuesPersistedSourceGesTypesRepository extends GenericRepository<ViewGefValuesPersistedSourceGesTypes, Long> {}