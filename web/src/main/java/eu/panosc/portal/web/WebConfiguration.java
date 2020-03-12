package eu.panosc.portal.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.panosc.portal.vdi.RemoteDesktopConfiguration;
import eu.panosc.portal.cloud.CloudConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class WebConfiguration extends Configuration {

    @Valid
    @NotNull
    private final DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @NotNull
    @Valid
    private RemoteDesktopConfiguration remoteDesktopConfiguration;

    @NotNull
    @Valid
    private CloudConfiguration cloudConfiguration;

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return this.dataSourceFactory;
    }

    @JsonProperty("vdi")
    public RemoteDesktopConfiguration getRemoteDesktopConfiguration() {
        return remoteDesktopConfiguration;
    }

    @JsonProperty("cloud")
    public CloudConfiguration getCloudConfiguration() {
        return cloudConfiguration;
    }

}
