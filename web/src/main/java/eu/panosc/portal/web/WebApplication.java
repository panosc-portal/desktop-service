package eu.panosc.portal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import eu.panosc.portal.vdi.RemoteDesktopModule;
import eu.panosc.portal.business.BusinessModule;
import eu.panosc.portal.cloud.CloudModule;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.core.JsonParser.Feature.STRICT_DUPLICATE_DETECTION;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class WebApplication extends Application<WebConfiguration> {

    private static final String BASE_PACKAGES = "eu.panosc.portal";

    @Override
    public String getName() {
        return "PaNOSC Portal Desktop Service";
    }

    @Override
    public void initialize(Bootstrap<WebConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false))
        );
        addBundles(bootstrap);
    }

    private void addBundles(Bootstrap<WebConfiguration> bootstrap) {
        // Configure the guice bundle
        GuiceBundle<WebConfiguration> guiceBundle = configureGuiceBundle();
        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(new MultiPartBundle());
    }

    private GuiceBundle<WebConfiguration> configureGuiceBundle() {
        return GuiceBundle.<WebConfiguration>builder()
            .enableAutoConfig(BASE_PACKAGES)
            .modules(new RemoteDesktopModule())
            .modules(new WebModule())
            .modules(new BusinessModule())
            .modules(new CloudModule())

            .useWebInstallers()
            .build();
    }

    private void registerObjectMapping(final Environment environment) {
        final ObjectMapper objectMapper = environment.getObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.setSerializationInclusion(ALWAYS);
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.enable(STRICT_DUPLICATE_DETECTION);
    }

    @Override
    public void run(final WebConfiguration configuration, final Environment environment) {
        registerObjectMapping(environment);
    }

}
