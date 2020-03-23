package eu.panosc.portal.vdi.services;


import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import eu.panosc.portal.vdi.exceptions.RemoteDesktopException;
import eu.panosc.portal.vdi.models.Size;
import eu.panosc.portal.vdi.RemoteDesktopConfiguration;
import eu.panosc.portal.vdi.concurrency.ConnectionThread;
import eu.panosc.portal.vdi.concurrency.ConnectionThreadExecutor;
import eu.panosc.portal.vdi.exceptions.OwnerNotConnectedException;
import eu.panosc.portal.vdi.models.Connection;
import eu.panosc.portal.business.InstanceSessionService;
import eu.panosc.portal.core.domain.*;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionRequestService {

    private final static Logger logger = LoggerFactory.getLogger(ConnectionRequestService.class);
    private final RemoteDesktopConfiguration configuration;
    private final ConnectionThreadExecutor executorService;
    private final InstanceSessionService instanceSessionService;

    @Inject
    ConnectionRequestService(final RemoteDesktopConfiguration configuration,
                             final ConnectionThreadExecutor executorService,
                             final InstanceSessionService instanceSessionService) {
        this.configuration = configuration;
        this.executorService = executorService;
        this.instanceSessionService = instanceSessionService;
    }

    private GuacamoleClientInformation createClientInformation(final Integer width, final Integer height) {
        final GuacamoleClientInformation info = new GuacamoleClientInformation();
        info.setOptimalScreenHeight(height);
        info.setOptimalScreenWidth(width);
        info.getImageMimetypes().add("image/png");
        info.getAudioMimetypes().add("audio/L16");
        return info;
    }

    private ConfiguredGuacamoleSocket buildSocket(final InstanceNetwork network, final InstanceMember member, final Account account, final Size screenSize) throws GuacamoleException, RemoteDesktopException {
        return this.buildSocket(network, member, account, screenSize, null);
    }

    private ConfiguredGuacamoleSocket buildSocket(final InstanceNetwork network,
                                                  final InstanceMember member,
                                                  final Account account,
                                                  final Size screenSize,
                                                  final InstanceSession session) throws GuacamoleException, RemoteDesktopException {

        final GuacamoleConfiguration config = new GuacamoleConfiguration();
        Integer guacdPort = network.getPort("GUACD");
        Integer rdpPort = network.getInternalPort("RDP");
        if (guacdPort == null) {
            throw new RemoteDesktopException("Instance does not support GUACD protocols");
        }
        if (rdpPort == null) {
            // Set to default value if not obtained from the network information
            rdpPort = 3389;
        }
        config.setProtocol("rdp");
        config.setParameter("hostname", "localhost");
        config.setParameter("port",rdpPort.toString());

        // Verify again that the instance member is the owner
        if (session != null) {
            config.setConnectionID(session.getConnectionId());
        }

        if (member.isRole(InstanceMemberRole.GUEST)) {
            config.setParameter("read-only", "true");
        }

        final GuacamoleClientInformation info = createClientInformation(screenSize.getWidth(), screenSize.getHeight());

        return new ConfiguredGuacamoleSocket(new InetGuacamoleSocket(network.getHostname(), guacdPort), config, info);
    }

    public Connection createConnection(final SocketIOClient client,
                                       final InstanceAuthorisation instanceAuthorisation,
                                       final Size screenSize) throws OwnerNotConnectedException {

        final InstanceMember member = instanceAuthorisation.getMember();
        final Account account = instanceAuthorisation.getAccount();
        final InstanceNetwork network = instanceAuthorisation.getNetwork();

        // Determine if a session is already open for the instance
        InstanceSession session = instanceSessionService.getByInstanceId(member.getInstanceId());

        try {
            ConfiguredGuacamoleSocket socket;
            if (session == null) {
                // Create new session - verify that the connected user is the instance owner
                if (member.isRole(InstanceMemberRole.OWNER)) {
                    socket = buildSocket(network, member, account, screenSize);

                    session = instanceSessionService.create(member.getInstanceId(), socket.getConnectionID());

                } else {
                    logger.error("A non-owner member is trying to create a new instance session");
                    throw new OwnerNotConnectedException();
                }

            } else {
                socket = buildSocket(network, member, account, screenSize, session);
            }

            // Create connection thread
            ConnectionThread connectionThread = this.executorService.startConnectionThread(client, socket);

            return new Connection(member, connectionThread);

        } catch (GuacamoleException | RemoteDesktopException exception) {
            logger.error("Creation of WebSocket tunnel to guacd failed: {}", exception.getMessage());
            logger.info("Error connecting WebSocket tunnel: {}", exception.getMessage());
            return null;
        }
    }

}
