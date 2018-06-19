package eu.city4age.dashboard.api.py;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import jep.Jep;
import jep.JepException;

/**
 * @author marina-andric
 *
 */
@Component
public class HiddenMarkovModelService {

	private Jep jep;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	public HiddenMarkovModelService() throws JepException {
		jep = new Jep();
	}
	
	public HiddenMarkovModelService(Jep jep) {
		this.jep = jep;
	}

	@SuppressWarnings("resource")
	public Response clusterSingleSeries(Integer userId) throws Exception {

		String fileUrlPath = "src/main/python/";
		String scriptName = "learnOptimalHMMs_and_persist.py";
		String funcName = "start";
		
        jep.runScript(fileUrlPath + scriptName);
        Object result = jep.invoke(funcName, userId);
		Response response = JerseyResponse.build(objectMapper.writeValueAsString(result.toString()));
		return response;
	}

	public static void main(String[] args) throws Exception {

		HiddenMarkovModelService js = new HiddenMarkovModelService();
		Response response = js.clusterSingleSeries(109);
		System.out.println("done");

	}

}
