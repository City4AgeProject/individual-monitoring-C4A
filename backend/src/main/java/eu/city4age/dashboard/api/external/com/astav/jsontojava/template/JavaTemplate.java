package eu.city4age.dashboard.api.external.com.astav.jsontojava.template;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import eu.city4age.dashboard.api.external.com.astav.jsontojava.ClassFileData;
import eu.city4age.dashboard.api.external.com.astav.jsontojava.ClassFiles;
import eu.city4age.dashboard.api.external.com.astav.jsontojava.util.StringHelper;

/**
 * User: Astav
 * Date: 10/21/12
 */
public class JavaTemplate {
    public static final String JSON_ANNOTATION = "@JsonProperty";

    public File writeOutJavaFile(String key, String outputDirectory, String packageName, ClassFiles classFiles) throws IOException, ClassNotFoundException {
        ClassFileData classFileData = classFiles.get(key);
        String className = StringHelper.capFirstLetter(key);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(packageName).append(";\r\n\r\n");
        for (String importPackageName : classFileData.getImportPackages()) {
            stringBuilder.append("import ").append(importPackageName).append(";\r\n");
        }
        stringBuilder.append("import java.util.*;\r\n\r\n");
        stringBuilder.append("public class ").append(className).append(" {\r\n");
        for (Map.Entry<String, String> variablesToTypeEntry : classFileData.getMapOfVariablesToTypes().entrySet()) {
            String v = variablesToTypeEntry.getKey();
            String t = variablesToTypeEntry.getValue();
            stringBuilder.append("\tprivate ").append(t).append(" ").append(v).append(";\r\n\r\n");
            stringBuilder.append("\tpublic ").append(t).append(" get").append(capitalize(v)).append("() {\r\n");
            stringBuilder.append("\t\treturn ").append(v).append(";\r\n\t}\r\n");
        }
        stringBuilder.append("}\r\n");

        String packageDirectory = packageName.replace(".", File.separator);
        if (!packageDirectory.endsWith(File.separator)) packageDirectory = packageDirectory + File.separator;
        String file = String.format("%s%s%s%s.java", outputDirectory, File.separator, packageDirectory, className);
        System.out.print(String.format("Writing file '%s' ...", file));
        File outputFile = new File(file);
        FileUtils.writeStringToFile(outputFile, stringBuilder.toString());
        System.out.print("done.");
        System.out.println();
        return outputFile;
    }

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
