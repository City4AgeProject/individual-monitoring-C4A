package eu.city4age.dashboard.api.external.io.ei.jsontoxls.util;

import java.util.UUID;

public class UUIDUtils {
    public static String newUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
