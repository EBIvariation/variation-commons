package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Map;

public interface BeaconOrganization {
    public String getId();

    public String getName();

    public String getDescription();

    public String getAddress();

    public String getWelcomeUrl();

    public String getContactUrl();

    public String getLogoUrl();

    public Map<String, String> getInfo();
}
