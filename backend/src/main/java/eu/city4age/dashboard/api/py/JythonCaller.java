package eu.city4age.dashboard.api.py;

import java.io.InputStream;

import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;

public class JythonCaller {

	@Autowired
    private PythonInterpreter pythonInterpreter;

    public JythonCaller() {
        pythonInterpreter = new PythonInterpreter();
    }
 
    public void invokeScript(InputStream inputStream) {
        pythonInterpreter.execfile(inputStream);
    }

}
