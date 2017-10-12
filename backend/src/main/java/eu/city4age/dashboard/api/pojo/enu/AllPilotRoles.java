package eu.city4age.dashboard.api.pojo.enu;

import java.util.HashSet;

/**
 *
 * @author GGiotis
 */
public enum AllPilotRoles {

    PROJECT_GERIATRICIAN(8),
    BEHAVIOURAL_SCIENTIST(9),
    MEDICAL_RESEARCHER(10),
    EPIDEMIOLOGIST(11);

    private final int value;

    AllPilotRoles(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static HashSet<Integer> getEnumAsSet() {

        HashSet<Integer> values = new HashSet<>();

        for (AllPilotRoles role : AllPilotRoles.values()) {
            values.add(role.getValue());
        }

        return values;
    }
}
