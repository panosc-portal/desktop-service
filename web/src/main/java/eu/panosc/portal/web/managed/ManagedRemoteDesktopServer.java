package eu.panosc.portal.web.managed;

import com.corundumstudio.socketio.SocketIOServer;
import com.google.inject.Inject;
import eu.panosc.portal.vdi.RemoteDesktopApplication;
import eu.panosc.portal.vdi.RemoteDesktopConfiguration;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ManagedRemoteDesktopServer implements Managed {

    private final SocketIOServer application;
    private final RemoteDesktopConfiguration configuration;
    private final Logger logger = LoggerFactory.getLogger(ManagedRemoteDesktopServer.class);

    @Inject
    public ManagedRemoteDesktopServer(final RemoteDesktopApplication application,
                                      final RemoteDesktopConfiguration configuration
    ) {
        this.application = application.createServer();

        this.configuration = configuration;
    }

    @Override
    public void start() throws Exception {
        if (configuration.isEnabled()) {
            logger.info("Starting remote desktop server");
            this.application.start();
        } else {
            logger.info("Remote desktop server is disabled");
        }
    }

    @Override
    public void stop() throws Exception {
        if (configuration.isEnabled()) {
            logger.info("Stopping remote desktop server");
            this.application.stop();
        }
    }

}
