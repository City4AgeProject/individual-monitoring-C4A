package eu.city4age.dashboard.api.persist;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.persist.DetectionVariableRepository;
import eu.city4age.dashboard.api.persist.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class DetectionVariableRepositoryTest {

	static protected Logger logger = Logger.getLogger(DetectionVariableRepositoryTest.class);

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;

	@Test
	@Transactional
	@Rollback(true)
	public void testFindNameByParentId() {

		DetectionVariableType dvt1 = new DetectionVariableType();
		dvt1.setDetectionVariableType("GES");
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableName("Walking");
		dv1.setDetectionVariableType(dvt1);
		dv1.setDerivedDetectionVariable(dv1);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableName("Climbing stairs");
		dv2.setDetectionVariableType(dvt1);
		dv2.setDerivedDetectionVariable(dv1);
		detectionVariableRepository.save(dv2);

		DetectionVariable dv3 = new DetectionVariable();
		dv3.setId(3L);
		dv3.setDetectionVariableName("Still/Moving");
		dv3.setDetectionVariableType(dvt1);
		dv3.setDerivedDetectionVariable(dv1);
		detectionVariableRepository.save(dv3);

		DetectionVariable dv4 = new DetectionVariable();
		dv4.setId(4L);
		dv4.setDetectionVariableName("Moving across rooms");
		dv4.setDetectionVariableType(dvt1);
		dv4.setDerivedDetectionVariable(dv1);
		detectionVariableRepository.save(dv4);

		DetectionVariable dv5 = new DetectionVariable();
		dv5.setId(5L);
		dv5.setDetectionVariableName("Gait balance");
		dv5.setDetectionVariableType(dvt1);
		dv5.setDerivedDetectionVariable(dv1);
		detectionVariableRepository.save(dv5);

		List<String> result = detectionVariableRepository.findNameByParentId(1L);

		Assert.assertNotNull(result);

		Assert.assertEquals(5, result.size());

		Assert.assertEquals("Walking", result.get(0));

	}

	@Test
	@Transactional
	@Rollback(true)
	public void testFindByDetectionVariableTypes() throws Exception {

		DetectionVariableType dvt1 = new DetectionVariableType();
		dvt1.setDetectionVariableType("OVL");
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = new DetectionVariableType();
		dvt2.setDetectionVariableType("GFG");
		detectionVariableTypeRepository.save(dvt2);

		DetectionVariableType dvt3 = new DetectionVariableType();
		dvt3.setDetectionVariableType("GEF");
		detectionVariableTypeRepository.save(dvt3);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableName("GFG name");
		dv2.setDetectionVariableType(dvt2);
		dv2.setDerivedDetectionVariable(null);
		detectionVariableRepository.save(dv2);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableType(dvt1);
		dv1.setDerivedDetectionVariable(dv2);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv3 = new DetectionVariable();
		dv3.setId(3L);
		dv3.setDetectionVariableType(dvt2);
		dv3.setDerivedDetectionVariable(null);
		detectionVariableRepository.save(dv3);

		DetectionVariable dv4 = new DetectionVariable();
		dv4.setId(4L);
		dv4.setDetectionVariableType(dvt2);
		dv4.setDerivedDetectionVariable(null);
		detectionVariableRepository.save(dv4);

		DetectionVariable dv5 = new DetectionVariable();
		dv5.setId(5L);
		dv5.setDetectionVariableType(dvt3);
		dv5.setDerivedDetectionVariable(null);
		detectionVariableRepository.save(dv5);

		List<String> parentFactors = Arrays.asList("OVL", "GFG");
		List<DetectionVariable> result = detectionVariableRepository.findByDetectionVariableTypes(parentFactors);

		Assert.assertNotNull(result);

		Assert.assertEquals(4, result.size());

	}

}