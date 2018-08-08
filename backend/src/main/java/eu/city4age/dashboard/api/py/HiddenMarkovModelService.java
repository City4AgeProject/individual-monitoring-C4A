package eu.city4age.dashboard.api.py;

import org.springframework.stereotype.Component;

import jep.Jep;

/**
 * @author marina-andric
 *
 */
@Component
public class HiddenMarkovModelService {

	
	public static String clusterSingleSeries(String path, Jep jep, Integer userId, Integer varId) throws Exception {

		/*String fileUrlPath = /*"src/main/python/" "/WEB-INF/classes/python/";*/
		String scriptName = "learnOptimalHMMs_and_persist.py";
		String funcName = "start";
		
        jep.runScript(path + scriptName);
        Object result = jep.invoke(funcName, userId, varId);
        String response = result.toString();
		return response;
	}

	/*public static void main(String[] args) throws Exception {

		HiddenMarkovModelService js = new HiddenMarkovModelService();
		Response response = js.clusterSingleSeries(109);
		System.out.println("done");

	}*/

}
