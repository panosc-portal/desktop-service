package eu.panosc.portal.cloud;

import javax.validation.constraints.NotNull;

public class CloudConfiguration {

    @NotNull
    private String cloudServiceEndpoint;

    public String getCloudServiceEndpoint() {
        return cloudServiceEndpoint;
    }

    public void setCloudServiceEndpoint(String cloudServiceEndpoint) {
        this.cloudServiceEndpoint = cloudServiceEndpoint;
    }
}
