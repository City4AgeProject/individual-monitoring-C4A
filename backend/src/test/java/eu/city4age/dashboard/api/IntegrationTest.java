package eu.city4age.dashboard.api;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;

import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.json.desobj.Gef;
import eu.city4age.dashboard.api.pojo.json.desobj.Ges;
import eu.city4age.dashboard.api.pojo.json.desobj.Groups;
import eu.city4age.dashboard.api.pojo.json.desobj.Mea;
import eu.city4age.dashboard.api.rest.MeasuresServiceTest;


public class IntegrationTest {
	
	static protected Logger logger = LogManager.getLogger(IntegrationTest.class);
	
	static protected ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void ConfigureDailyMeasuresDeserializerTest() throws IOException {
				
		File jsonFile = new ClassPathResource("/json1.json", IntegrationTest.class).getFile();

		@SuppressWarnings("deprecation")
		ConfigureDailyMeasuresDeserializer data = mapper.readerFor(ConfigureDailyMeasuresDeserializer.class)
			.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(jsonFile);

		//logger.info("Config tree: " + data.getConfigTree().get(3).getFactors().get(0).getSubFactors().get(0).getName());
		
		Groups grp1 = data.getGroups().get(0);
		grp1.setWeight(new BigDecimal("2"));
		
		Gef gef1 = data.getGroups().get(3).getFactors().get(1);
		gef1.setWeight(new BigDecimal("2"));
		
		Ges ges1 = data.getGroups().get(3).getFactors().get(0).getSubFactors().get(0);
		ges1.setName("**test_walking_test**");
		ges1.setLevel(9);

		Mea mea1 = data.getGroups().get(3).getFactors().get(0).getSubFactors().get(0).getMeasures().get(0);
		mea1.setName("**TEST_WALK_DISTANCE_OUTDOOR_TEST**");
		mea1.setLevel(5);
		
		//grp1 test
		Assert.assertTrue("overall".equals(grp1.getName()));
		Assert.assertEquals(Long.valueOf(2), grp1.getWeight());
		Assert.assertEquals(Integer.valueOf(1), grp1.getLevel());
		
		//gef1 test
		Assert.assertTrue("Basic Activities of Daily Living".equals(gef1.getName()));
		Assert.assertEquals(Long.valueOf(2), gef1.getWeight());
		Assert.assertEquals(Integer.valueOf(2), gef1.getLevel());
		
		//ges1 test
		Assert.assertTrue("**test_walking_test**".equals(ges1.getName()));
		Assert.assertEquals(Long.valueOf(1), ges1.getWeight());
		Assert.assertEquals(Integer.valueOf(9), ges1.getLevel());
		
		//mea1 test
		Assert.assertTrue("**TEST_WALK_DISTANCE_OUTDOOR_TEST**".equals(mea1.getName()));
		Assert.assertEquals(Long.valueOf(1), mea1.getWeight());
		Assert.assertEquals(Integer.valueOf(5), mea1.getLevel());
		
		//logger.info("##" + data.getConfigTree().get(3).getFactors().get(0).getSubFactors().get(0).getMeasures());
	
	}

}
