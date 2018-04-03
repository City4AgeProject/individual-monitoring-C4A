package eu.city4age.dashboard.api.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import jep.Jep;
import jep.JepException;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JythonServiceTest {
	
	Jep jep;
	
	@InjectMocks
	JythonService jythonService;
	
	@Before
	public void initialize() throws JepException {
		
		jep = new Jep();
	
	}
	
	@After
	public void destroy() {
		
		jep.close();
		
	}
	
	@Test
	public void testJython() throws Exception {
		
		String result = jythonService.jythonTest();
		
		Assert.assertEquals("1764", result);
		
	}
	
	@Test
	public void testJythonModule() throws Exception {
		
		String result = jythonService.jythonModuleTest();
		
		Assert.assertEquals("25", result);

	}
	
	@Test
	public void testJepSingleSeries() throws Exception {

		jep.runScript("src\\main\\resources\\single_series.py");
		
	}
	
	@Test
	public void testJepMonotonicityNui() throws Exception {
		
		jep.runScript("src\\main\\resources\\monotonicity_nui.py");
		
	}
	
	@Test
	public void testJepLearnOptimalAndPersist() throws Exception {
		
		jep.runScript("src\\main\\resources\\learnOptimal_and_persist_NEW.py");

	}
	
	@Test
	public void testSingleSeries() throws Exception {

		jythonService.singleSeriesTest();
		
	}

	@Test
	public void helloWorldInternal() throws Exception {

		jep.eval("print (\"Hello World\")");
		
	}
	
	@Test
	public void helloWorldOExternal() throws Exception {

		jep.runScript("src\\main\\resources\\hello_world.py");
		
	}


}
