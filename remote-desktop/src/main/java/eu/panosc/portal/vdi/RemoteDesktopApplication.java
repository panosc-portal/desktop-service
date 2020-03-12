package eu.panosc.portal.vdi;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.inject.Singleton;
import eu.panosc.portal.business.InstanceSessionService;
import eu.panosc.portal.cloud.CloudService;
import eu.panosc.portal.vdi.listeners.*;
import eu.panosc.portal.vdi.services.ConnectionRequestService;

import com.google.inject.Inject;

@Singleton
public class RemoteDesktopApplication {

    private final ConnectionRequestService connectionRequestService;
    private final RemoteDesktopConfiguration configuration;
    private final InstanceSessionService instanceSessionService;
    private final CloudService cloudService;

    @Inject
    public RemoteDesktopApplication(final ConnectionRequestService connectionRequestService,
                                    final RemoteDesktopConfiguration configuration,
                                    final CloudService cloudService,
                                    final InstanceSessionService instanceSessionService) {
        this.connectionRequestService = connectionRequestService;
        this.cloudService = cloudService;
        this.configuration = configuration;
        this.instanceSessionService = instanceSessionService;
    }

    private Configuration buildConfiguration() {
        final String host = configuration.getHost();
        final Integer port = configuration.getPort();
        final ClientAuthorizationListener authorizationListener = new ClientAuthorizationListener();
        final Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setWebsocketCompression(true);
        config.setAuthorizationListener(authorizationListener);
        config.setPingInterval(100);
        SocketConfig socketConfig = config.getSocketConfig();
        socketConfig.setReuseAddress(true);
        return config;
    }

    private void addListeners(final SocketIOServer server) {
        server.addConnectListener(
            new ClientConnectListener(cloudService, instanceSessionService, connectionRequestService)
        );
        server.addEventListener("display", String.class, new ClientMessageListener(instanceSessionService));
        server.addDisconnectListener(new ClientDisconnectListener(instanceSessionService));
    }

    private SocketIOServer buildServer() {
        final Configuration configuration = buildConfiguration();
        return new SocketIOServer(configuration);
    }

    public SocketIOServer createServer() {
        final SocketIOServer server = buildServer();
        addListeners(server);
        return server;
    }
}
