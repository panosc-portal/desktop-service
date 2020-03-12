package eu.panosc.portal.cloud;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.io.FileNotFoundException;

public class CloudModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    public CloudService providesCloudService(final CloudConfiguration configuration) throws FileNotFoundException {

        return new CloudService(configuration.getCloudServiceEndpoint());
    }

}
