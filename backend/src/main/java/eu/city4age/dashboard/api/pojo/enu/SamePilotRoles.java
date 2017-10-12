package eu.city4age.dashboard.api.pojo.enu;

import java.util.HashSet;

/**
 *
 * @author GGiotis
 */
public enum SamePilotRoles {

    CARE_RECIPIENT(1),
    INFORMAL_CAREGIVER(2),
    FORMAL_CAREGIVER(3),
    ELDERLY_COMMUNITY_CENTRE_EXECUTIVE(4),
    SHELTERED_ACCOMODATION_MANAGER(5),
    GENERAL_PRACTIONONER(6),
    LOCAL_PILOT_GERIATRICIAN(7),
    CITY_POLICY_PLANNER(12),
    SOCIAL_SERVICE_REPRESENTATIVE(13),
    MUNICIPALITY_REPRESENTATIVE(14),
    PILOT_SOURCE_SYSTEM(15);

    private final int value;

    SamePilotRoles(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static HashSet<Integer> getEnumAsSet() {

        HashSet<Integer> values = new HashSet<>();

        for (SamePilotRoles role : SamePilotRoles.values()) {
            values.add(role.getValue());
        }

        return values;
    }
}
