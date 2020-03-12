package eu.panosc.portal.web;

import com.google.inject.Provides;
import eu.panosc.portal.vdi.RemoteDesktopConfiguration;
import eu.panosc.portal.PersistenceModule;
import eu.panosc.portal.cloud.CloudConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

import java.util.Map;
import java.util.Properties;

public class WebModule extends DropwizardAwareModule<WebConfiguration> {

    @Override
    protected void configure() {
        configuration();
        environment();
        bootstrap();
        bindServices();
        configureDatabase();
    }

    private Properties createDatabasePropertiesFromDataSource(final DataSourceFactory dataSourceFactory) {
        final Properties properties = new Properties();
        properties.put("javax.persistence.jdbc.driver", dataSourceFactory.getDriverClass());
        properties.put("javax.persistence.jdbc.url", dataSourceFactory.getUrl());
        properties.put("javax.persistence.jdbc.user", dataSourceFactory.getUser());
        properties.put("javax.persistence.jdbc.password", dataSourceFactory.getPassword());

        for (Map.Entry<String, String> entry : dataSourceFactory.getProperties().entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
        return properties;
    }

    private void configureDatabase() {
        final DataSourceFactory dataSourceFactory = this.configuration().getDataSourceFactory();
        final ManagedDataSource managedDataSource = dataSourceFactory.build(environment().metrics(), "database");
        final Properties properties = createDatabasePropertiesFromDataSource(dataSourceFactory);

        install(new PersistenceModule("panosc", properties));

        environment().lifecycle().manage(managedDataSource);
    }

    private void bindServices() {

    }

    @Provides
    public RemoteDesktopConfiguration providesVirtualDesktopConfiguration() {
        return this.configuration().getRemoteDesktopConfiguration();
    }

    @Provides
    public CloudConfiguration providesCloudConfiguration() {
        return this.configuration().getCloudConfiguration();
    }

    @Provides
    public ManagedDataSource providesManagedDataSource() {
        DataSourceFactory dataSourceFactory = this.configuration().getDataSourceFactory();
        return dataSourceFactory.build(environment().metrics(), "panosc");
    }

}
