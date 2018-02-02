package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;

@Repository(value = "userInSystemRepository")
public interface UserInSystemRepository extends GenericRepository<UserInSystem, Long> {}