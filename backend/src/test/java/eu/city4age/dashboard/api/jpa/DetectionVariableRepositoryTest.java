package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class DetectionVariableRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(DetectionVariableRepositoryTest.class);
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	
	@Test
	@Transactional
	@Rollback(true)
	public void	testFindByDetectionVariableNameAndDetectionVariableType () {
		DetectionVariableType dvt1 = DetectionVariableType.MEA;
		DetectionVariableType dvt2 = DetectionVariableType.GEF;
		detectionVariableTypeRepository.save(dvt1);
		detectionVariableTypeRepository.save(dvt2);
		
		DetectionVariable dv1 = new DetectionVariable ();
		dv1.setId(1L);
		dv1.setDetectionVariableName("DetectionVariable1");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable ();
		dv2.setId(2L);
		dv2.setDetectionVariableName("DetectionVariable2");
		dv2.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv2);
		
		DetectionVariable dv3 = new DetectionVariable ();
		dv3.setId(3L);
		dv3.setDetectionVariableName("DetectionVariable3");
		dv3.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv3);
		
		DetectionVariable dv4 = new DetectionVariable ();
		dv4.setId(4L);
		dv4.setDetectionVariableName("DetectionVariable4");
		dv4.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv4);
		
		String searchName = "DetectionVariable1";
		DetectionVariableType searchType = DetectionVariableType.MEA;
		
		DetectionVariable foundVariable = detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(searchName, searchType);
		Assert.assertNotNull(foundVariable);
		Assert.assertEquals(dv1.getId(), foundVariable.getId());
		
		searchType = DetectionVariableType.GEF;
		foundVariable = detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(searchName, searchType);
		Assert.assertNull(foundVariable);
		
		searchName = "DetectionVariable3";
		foundVariable = detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(searchName, searchType);
		Assert.assertNotNull(foundVariable);
		Assert.assertEquals(dv3.getId(), foundVariable.getId());
		
		searchName = "This VariableName Does Not Exist";
		foundVariable = detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(searchName, searchType);
		Assert.assertNull(foundVariable);
		
		searchType = DetectionVariableType.GES;
		foundVariable = detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(searchName, searchType);
		Assert.assertNull(foundVariable);
	}
}
