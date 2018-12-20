package eu.city4age.dashboard.api.external.io.ei.jsontoxls.util;

import static eu.city4age.dashboard.api.external.io.ei.jsontoxls.util.UUIDUtils.newUUID;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.city4age.dashboard.api.external.com.astav.jsontojava.Generator;

public class JsonPojoConverter {
	static protected Logger logger = LogManager.getLogger(JsonPojoConverter.class);
    private String className;
    private String packageName;
    private String outputDirectory;

    public JsonPojoConverter(String packageName, String className, String outputDirectory) {
        this.className = className;
        this.packageName = packageName;
        this.outputDirectory = outputDirectory;
    }

    public String generateJavaClasses(String jsonString) throws IOException, ClassNotFoundException {
        String generatedPackageName = this.packageName + newUUID();
        Generator generator = new Generator(this.outputDirectory, generatedPackageName, null, null);
        generator.generateClasses(className, jsonString);
        logger.info("Generated package with name {0}", generatedPackageName);
        return generatedPackageName;
    }
}

