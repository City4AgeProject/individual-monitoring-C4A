package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.RiskStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class RiskStatusRepositoryTest {

	@Autowired
	private RiskStatusRepository riskStatusRepository;

	@Test
	@Transactional
	@Rollback(true)
	public void testFindAll() throws Exception {

		RiskStatus rs1 = new RiskStatus();
		rs1.setRiskStatus('W');
		rs1.setRiskStatusDescription("Risk warning");
		riskStatusRepository.save(rs1);
		
		RiskStatus rs2 = new RiskStatus();
		rs2.setRiskStatus('A');
		rs2.setRiskStatusDescription("Risk alert");
		riskStatusRepository.save(rs2);
		
		List<RiskStatus> result = riskStatusRepository.findAll();
		
		Assert.assertNotNull(result);

		Assert.assertEquals(2, result.size());
		
	}

}
