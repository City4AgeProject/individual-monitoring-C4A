package eu.city4age.dashboard.api.service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import io.ei.jsontoxls.AllConstants;
import io.ei.jsontoxls.util.JsonPojoConverter;
import io.ei.jsontoxls.util.ObjectDeserializer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class ExportServiceTest {
	
	static protected Logger logger = LogManager.getLogger(ExportServiceTest.class);
	
	static String EXPORT_CLASS_NAME = "Export";
	
	@Test
	@Transactional
	@Rollback(true)
	public void test() throws Exception { //ExportDataEndpoint
		logger.info("trt");
		List<Employee> employees = generateSampleEmployeeData();
		List<List<Object>> data = createGridData(employees);
		Object deserializedObject;
		ObjectDeserializer objectDeserializer = new ObjectDeserializer(AllConstants.GENERATED_CLASSES_OUTPUT_DIRECTORY,
                this.EXPORT_CLASS_NAME);
		String generatedPackageName = "";
		JsonPojoConverter converter = new JsonPojoConverter(AllConstants.DOMAIN_PACKAGE, this.EXPORT_CLASS_NAME,
                AllConstants.GENERATED_CLASSES_OUTPUT_DIRECTORY);

		String name = "testtest.json";
		String myString = "{\"headers\": [\"Name\",\"Birthday\",\"Payment\"],\"data\": [[\"Elsa\", \"1\", \"2\"],[\"Oleg\", \"2\", \"3\"],[\"Neil\", \"3\", \"4\"]]}";
		// new ByteArrayInputStream( myString.getBytes( charset ) );
		//try(InputStream isJson = ExportServiceTest.class.getResourceAsStream( name)) {
		try(InputStream isJson = new ByteArrayInputStream( myString.getBytes( "UTF-8" ) ) ) {
			String jsonData = IOUtils.toString( isJson );
			System.out.println("jsonData: " + jsonData);
            if(jsonData.startsWith("[")){
                //Able to handle json array
            	//generatedPackageName = converter.generateJavaClasses(jsonData);
            	System.out.println("Making list");
                deserializedObject = objectDeserializer.makeJsonList(generatedPackageName,
                    jsonData);
                System.out.println("deserializedObject list: " + deserializedObject);
            }else{
            	System.out.println("Making object");
                generatedPackageName = converter.generateJavaClasses(jsonData);
                System.out.println("Ovde ne dodje");
                deserializedObject = objectDeserializer.makeJsonObject(generatedPackageName,
                    jsonData);
                System.out.println("deserializedObject object: " + deserializedObject);
            }
            
            /*Map<String, Object> beans = new HashMap<>();
            beans.put(ROOT_DATA_OBJECT, deserializedObject);*/
			try(InputStream is = ExportServiceTest.class.getResourceAsStream("grid_template.xlsx")) {
				try (OutputStream os = new FileOutputStream("target/grid_output1.xlsx")) {
					//Context context = new Context(beans);
					/*Context context = new Context();
					context.putVar("headers", Arrays.asList("Name", "Birthday", "Payment"));
					context.putVar("data", deserializedObject);
					JxlsHelper.getInstance().processTemplate(is, os, context); //ExcelUtils
					logger.info("mrt");*/
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
		            System.out.println("context: " + context);
		            JxlsHelper.getInstance().processTemplate(is, os, context);
				}
			}
		}
	}
	
    private static List<List<Object>> createGridData(List<Employee> employees) {
        List<List<Object>> data = new ArrayList<>();
        for(Employee employee : employees){
            data.add( convertEmployeeToList(employee));
        }
        return data;
    }
    
    private static List<Employee> generateSampleEmployeeData() throws Exception {
        List<Employee> employees = new ArrayList<Employee>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
        employees.add( new Employee("Elsa", dateFormat.parse("1970-Jul-10"), 1500, 0.15) );
        employees.add(new Employee("Oleg", dateFormat.parse("1973-Apr-30"), 2300, 0.25));
        employees.add(new Employee("Neil", dateFormat.parse("1975-Oct-05"), 2500, 0.00));
        employees.add(new Employee("Maria", dateFormat.parse("1978-Jan-07"), 1700, 0.15));
        employees.add(new Employee("John", dateFormat.parse("1969-May-30"), 2800, 0.20));
        return employees;
    }
    
    private static List<Object> convertEmployeeToList(Employee employee){
        List<Object> list = new ArrayList<>();
        list.add(employee.getName());
        list.add(employee.getBirthDate());
        list.add(employee.getPayment());
        return list;
    }

}
