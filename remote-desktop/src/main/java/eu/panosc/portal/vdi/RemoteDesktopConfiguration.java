package eu.panosc.portal.vdi;

import javax.validation.constraints.NotNull;

public class RemoteDesktopConfiguration {

    @NotNull
    private Integer port = 8087;

    @NotNull
    private String host = "localhost";

    @NotNull
    private boolean enabled = true;


    public RemoteDesktopConfiguration() {

    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
