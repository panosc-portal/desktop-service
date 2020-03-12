package eu.panosc.portal.application;

import eu.panosc.portal.web.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        logger.info("Starting VISA application");
        final WebApplication application = new WebApplication();
        application.run(args);
    }
}
