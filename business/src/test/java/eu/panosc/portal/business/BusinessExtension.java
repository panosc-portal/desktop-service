package eu.panosc.portal.business;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import eu.panosc.portal.PersistenceModule;
import org.hibernate.Session;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Collectors;

public class BusinessExtension implements TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback {

    private static final Namespace NAMESPACE = Namespace.create(BusinessExtension.class);
    private Injector injector;

    private Injector getOrCreateInjector(ExtensionContext context) {
        if (injector == null) {
            Optional<Class<?>> testClass = context.getTestClass();
            ExtensionContext.Store store = context.getStore(NAMESPACE);
            Injector injector = store.getOrComputeIfAbsent(testClass, key -> createInjector(), Injector.class);
            this.injector = injector;
        }
        return this.injector;
    }

    private Injector createInjector() {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                install(new PersistenceModule("panosc"));
            }
        });
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        final Injector injector = this.getOrCreateInjector(context);
        injector.injectMembers(testInstance);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        executeNative(context, "TRUNCATE SCHEMA public AND COMMIT NO CHECK");
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        runSqlScript(context, "fixtures.sql");
    }

    private void executeNative(ExtensionContext context, final String query) {
        Injector injector = getOrCreateInjector(context);
        EntityManager em = injector.getInstance(EntityManager.class);
        em.clear();
        Session session = em.unwrap(Session.class);
        session.doWork(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        });
    }

    private String getInstanceFileAsString(String fileName) {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        return null;
    }

    private void runSqlScript(ExtensionContext context, String instance) {
        final String sql = getInstanceFileAsString(instance);
        executeNative(context, sql);
    }
}
