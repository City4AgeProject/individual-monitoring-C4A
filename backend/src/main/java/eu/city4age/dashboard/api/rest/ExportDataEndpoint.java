package eu.city4age.dashboard.api.rest;

import static io.ei.jsontoxls.AllConstants.ROOT_DATA_OBJECT;
import static io.ei.jsontoxls.AllConstants.TOKEN_PATH_PARAM;
import static io.ei.jsontoxls.util.ResponseFactory.badRequest;
import static io.ei.jsontoxls.util.ResponseFactory.internalServerError;
import static io.ei.jsontoxls.util.ResponseFactory.notFound;
import static java.text.MessageFormat.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
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
import io.ei.jsontoxls.Messages;
import io.ei.jsontoxls.repository.ExcelRepository;
import io.ei.jsontoxls.repository.TemplateRepository;
import io.ei.jsontoxls.util.ExcelUtils;
import io.ei.jsontoxls.util.JsonPojoConverter;
import io.ei.jsontoxls.util.ObjectDeserializer;
import io.ei.jsontoxls.util.PackageUtils;
import io.ei.jsontoxls.util.ResponseFactory;
import io.ei.jsontoxls.util.UUIDUtils;

@Path(ExportDataEndpoint.PATH)
public class ExportDataEndpoint {
	
	public static final String PATH = "exportData";
	
	static protected Logger logger = LogManager.getLogger(ExportDataEndpoint.class);
	
	static String EXPORT_CLASS_NAME = "Export";

    private ExcelUtils excelUtil;
    //private Logger logger = LoggerFactory.getLogger(XlsResource.class);
    private JsonPojoConverter converter;
    private ObjectDeserializer objectDeserializer;
    private PackageUtils packageUtil;
    private TemplateRepository templateRepository;
    private ExcelRepository excelRepository;
    
    
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
    public Response generateExcelFromTemplate(@PathParam(TOKEN_PATH_PARAM) String templateToken, String jsonData) {
        logger.debug(format("Got request with Token: {0} and JSON: {1}", templateToken, jsonData));
        String generatedPackageName = "";
        Object deserializedObject;
        try {
            byte[] template = templateRepository.findByToken(templateToken);
            if (template == null) {
                return notFound(format(Messages.INVALID_TOKEN, templateToken));
            }
            if (isBlank(jsonData)) {
                return badRequest(format(Messages.EMPTY_JSON_DATA, templateToken));
            }

            if(jsonData.startsWith("[")){
                //Able to handle json array
                deserializedObject = objectDeserializer.makeJsonList(generatedPackageName,
                    jsonData);
            }else{
                generatedPackageName = converter.generateJavaClasses(jsonData);
                deserializedObject = objectDeserializer.makeJsonObject(generatedPackageName,
                    jsonData);
            }

            Map<String, Object> beans = new HashMap<>();
            beans.put(ROOT_DATA_OBJECT, deserializedObject);
            String generatedExcelToken = UUIDUtils.newUUID();
            byte[] generatedExcel = excelUtil.generateExcel(beans, template, generatedExcelToken);

            excelRepository.add(generatedExcelToken, templateToken, generatedExcel);
            return ResponseFactory.created(URI.create("/xls/" + generatedExcelToken).toString());
        } catch (JsonParseException e) {
            logger.error(format(Messages.MALFORMED_JSON, e.getMessage(),
                    getStackTrace(e)));
            return badRequest(Messages.MALFORMED_JSON);
        } catch (Exception e) {
            logger.error(format(Messages.TRANSFORMATION_FAILURE, e.getMessage(),
                    getStackTrace(e)));
            return internalServerError(Messages.UNABLE_TO_GENERATE_EXCEL_ERROR);
        } finally {
            packageUtil.cleanup(generatedPackageName);
        }
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