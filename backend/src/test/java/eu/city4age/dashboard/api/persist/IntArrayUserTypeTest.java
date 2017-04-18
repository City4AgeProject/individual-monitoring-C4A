package eu.city4age.dashboard.api.persist;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.persist.convert.IntArrayUserType;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class IntArrayUserTypeTest {
	
	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testIntArrayUserType() throws HibernateException, SQLException {
		
		VariationMeasureValue vm = new VariationMeasureValue();
		
		vm.setId(1L);
		
		int[] intArray = {111, 122, 133};

		vm.setCdDataSourceType(intArray);

		variationMeasureValueRepository.save(vm);
		
		VariationMeasureValue result = variationMeasureValueRepository.findOne(1L);
		
		Assert.assertEquals(3, result.getCdDataSourceType().length);
		
		Assert.assertEquals(111, result.getCdDataSourceType()[0]);
		
		Assert.assertEquals(122, result.getCdDataSourceType()[1]);
		
		Assert.assertEquals(133, result.getCdDataSourceType()[2]);

	}

}
