package eu.city4age.dashboard.api.py;

import java.io.InputStream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class JythonTest {
	
	@Autowired
	JythonCaller jythonCaller;
	
	@Test
	public void testInvokeScript() {
	
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("hello_world.py");
		jythonCaller.invokeScript(inputStream);
	
	}

}
