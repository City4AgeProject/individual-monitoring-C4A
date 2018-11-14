package eu.city4age.dashboard.api.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import io.ei.jsontoxls.AllConstants;
import io.ei.jsontoxls.util.JsonPojoConverter;
import io.ei.jsontoxls.util.ObjectDeserializer;

@Path(ExportDataEndpoint.PATH)
public class ExportDataEndpoint {
	
	public static final String PATH = "exportData";
	
	static protected Logger logger = LogManager.getLogger(ExportDataEndpoint.class);
	
	static String EXPORT_CLASS_NAME = "Export";
    
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

    @POST
    @Path("generateExcel")
    @Consumes("text/plain")
    public Response generateExcel(String microServiceURL) throws Exception {

    	HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
    	ResponseEntity<String> jsonData = restTemplate().exchange(microServiceURL, HttpMethod.GET, entity, String.class);
   
		Object deserializedObject;
		ObjectDeserializer objectDeserializer = new ObjectDeserializer(AllConstants.GENERATED_CLASSES_OUTPUT_DIRECTORY,
				ExportDataEndpoint.EXPORT_CLASS_NAME);
		String generatedPackageName = "";
		JsonPojoConverter converter = new JsonPojoConverter(AllConstants.DOMAIN_PACKAGE, ExportDataEndpoint.EXPORT_CLASS_NAME,
                AllConstants.GENERATED_CLASSES_OUTPUT_DIRECTORY);

        generatedPackageName = converter.generateJavaClasses(jsonData.getBody());
        deserializedObject = objectDeserializer.makeJsonObject(generatedPackageName, jsonData.getBody());

		try(InputStream is = ExportDataEndpoint.class.getResourceAsStream("group_analytics_template.xlsx")) {
			try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
	            Map<String, Object> beans = new HashMap<>();
	            java.lang.reflect.Field field = deserializedObject.getClass().getDeclaredField("headers");    
	            field.setAccessible(true);
	            Object value = field.get(deserializedObject);
	            beans.put("headers", value);
	            java.lang.reflect.Field field2 = deserializedObject.getClass().getDeclaredField("data");    
	            field2.setAccessible(true);
	            Object value2 = field2.get(deserializedObject);
	            beans.put("data", value2);
	            Context context = new Context(beans);
	            JxlsHelper.getInstance().processTemplate(is, os, context);
	    	    return JerseyResponse.buildFile(os.toByteArray(), "group_analytics.xlsx"); 
			}
		}

    }
 
    
    @SuppressWarnings("unchecked")
	@POST
    @Path("generateCsv")
    @Consumes("text/plain")
    public Response generateCsv(String microServiceURL) throws Exception {

    	HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
    	ResponseEntity<String> jsonData = restTemplate().exchange(microServiceURL, HttpMethod.GET, entity, String.class);
    	
		Object deserializedObject;
		ObjectDeserializer objectDeserializer = new ObjectDeserializer(AllConstants.GENERATED_CLASSES_OUTPUT_DIRECTORY,
				ExportDataEndpoint.EXPORT_CLASS_NAME);
		String generatedPackageName = "";
		JsonPojoConverter converter = new JsonPojoConverter(AllConstants.DOMAIN_PACKAGE, ExportDataEndpoint.EXPORT_CLASS_NAME,
                AllConstants.GENERATED_CLASSES_OUTPUT_DIRECTORY);

        generatedPackageName = converter.generateJavaClasses(jsonData.getBody());
        deserializedObject = objectDeserializer.makeJsonObject(generatedPackageName, jsonData.getBody());
 
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			try (PrintWriter  writer = new PrintWriter(os)) {
	            java.lang.reflect.Field field = deserializedObject.getClass().getDeclaredField("headers");    
	            field.setAccessible(true);
	            List<Object> value = (List<Object>) field.get(deserializedObject);
	            StringBuilder sb = new StringBuilder();
	            for(int i = 0; i < value.size(); i++) {
	            	sb.append(value.get(i));
	            	if(i < value.size() - 1)
	            		sb.append(",");
	            }
	            writer.println(sb.toString());
	            java.lang.reflect.Field field2 = deserializedObject.getClass().getDeclaredField("data");    
	            field2.setAccessible(true);
	            List<List<Object>> value2 = (List<List<Object>>) field2.get(deserializedObject);
	            StringBuilder sb2 = null;
	            for(int i = 0; i < value2.size(); i++) {
	            	sb2 = new StringBuilder();
	            	for(int j = 0; j < value2.get(i).size(); j++) {
	            		sb2.append(value2.get(i).get(j));
		            	if(j < value2.get(i).size() - 1)
		            		sb2.append(",");
	            	}
	            	if(i < value2.size() - 1)
	            		writer.println(sb2.toString());
	            	else
	            		writer.print(sb2.toString());
	            }
	            writer.flush();
	            writer.close();
			}
    	    return JerseyResponse.buildFile(os.toByteArray(), "group_analytics.csv"); 
		}

    }
 

}
