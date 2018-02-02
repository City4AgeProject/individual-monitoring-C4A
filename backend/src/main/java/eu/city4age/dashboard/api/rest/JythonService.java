package eu.city4age.dashboard.api.rest;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Component;

/**
 * @author milos.holclajtner
 *
 */
@Component
public class JythonService {
	
	public String jythonTest() {
       PythonInterpreter pi = new PythonInterpreter();
       pi.set("integer", new PyInteger(42));
       pi.exec("square = integer*integer");
       PyInteger square = (PyInteger)pi.get("square");
       System.out.println("square: " + square.asInt());
       pi.close();
		return String.valueOf(square.asInt());
	}
	
	public String jythonModuleTest() {
       PythonInterpreter pi = new PythonInterpreter();
       pi.exec("from pymodule import square");
       pi.set("integer", new PyInteger(42));
       pi.exec("result = square(integer)");
       pi.exec("print(result)");
       PyInteger result = (PyInteger)pi.get("result");
       System.out.println("result: "+ result.asInt());
       PyFunction pf = (PyFunction)pi.get("square");
       System.out.println(pf.__call__(new PyInteger(5)));
       pi.close();
       return String.valueOf(pf.__call__(new PyInteger(5)));
	}
	
	public void singleSeriesTest() {
       PythonInterpreter pi = new PythonInterpreter();
       pi.exec("from single_series import get_data");
       pi.exec("data = get_data()");
       pi.exec("print(data)");
       PyString data = (PyString) pi.get("data");
       System.out.println("data: " + data.asString());
       pi.close();
	}

}
