package eu.city4age.dashboard.api.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.core.Response;

import eu.city4age.dashboard.api.rest.MeasuresService;

@RunWith(MockitoJUnitRunner.class)
public class MeasureServiceTest {
	
	@InjectMocks
	MeasuresService measureService;
	
	@Test
	public void test() throws Exception {

		String test = measureService.test();
		
		Assert.assertEquals("hello", test);
		
	}

}