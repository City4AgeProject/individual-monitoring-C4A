package eu.city4age.dashboard.api.external.com.astav.jsontojava.util;

/**
 * User: Astav
 * Date: 10/21/12
 */
public class StringHelper {
    public static String capFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
