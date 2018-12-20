package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class DerivedMeasureValueRepositoryTest {
	
	@Autowired
	private DerivedMeasureValueRepository derivedMeasureValueRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByUserInRoleIdAndParentFactorId() throws Exception {
		
		List<DerivedMeasureValue> dmvs = derivedMeasureValueRepository.findByUserInRoleIdAndParentFactorId(10L, 514L);
		
		System.out.println("dmvs: " + dmvs);
	}

}
