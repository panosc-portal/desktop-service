package eu.panosc.portal.vdi.listeners;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import eu.panosc.portal.vdi.exceptions.OwnerNotConnectedException;
import eu.panosc.portal.vdi.models.Connection;
import eu.panosc.portal.vdi.models.Size;
import eu.panosc.portal.vdi.services.ConnectionRequestService;
import eu.panosc.portal.vdi.services.HttpRequest;
import eu.panosc.portal.business.InstanceSessionService;
import eu.panosc.portal.cloud.CloudService;
import eu.panosc.portal.core.domain.InstanceAuthorisation;
import eu.panosc.portal.core.domain.InstanceMember;
import eu.panosc.portal.core.domain.InstanceSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static eu.panosc.portal.vdi.events.Event.USERS_CONNECTED;
import static eu.panosc.portal.vdi.events.Event.USER_CONNECTED;

public class ClientConnectListener extends AbstractListener implements ConnectListener {

    private final static Logger logger = LoggerFactory.getLogger(ClientConnectListener.class);
    private final static String TOKEN_PARAMETER = "token";
    private final static String INSTANCE_ID_PARAMETER = "instanceId";
    private final static String SCREEN_WIDTH_PARAMETER = "screenWidth";
    private final static String SCREEN_HEIGHT_PARAMETER = "screenHeight";
    private final static String GATEWAY_HOST_HEADER = "Gateway-host";

    private final CloudService cloudService;
    private final InstanceSessionService instanceSessionService;
    private final ConnectionRequestService connectionRequestService;

    public ClientConnectListener(final CloudService cloudService,
                                 final InstanceSessionService instanceSessionService,
                                 final ConnectionRequestService connectionRequestService) {
        this.cloudService = cloudService;
        this.instanceSessionService = instanceSessionService;
        this.connectionRequestService = connectionRequestService;
    }

    private InstanceAuthorisation validateToken(final HttpRequest request) {
        final String token = request.getStringParameter(TOKEN_PARAMETER);
        final Integer instanceId = request.getIntegerParameter(INSTANCE_ID_PARAMETER);
        final String gatewayHost = "http://" + request.getStringHeader(GATEWAY_HOST_HEADER) + "/cloud-service/api/v1";
        
        if (token == null) {
            logger.warn("Authorisation token not sent in the request");
            return null;
        }
        if (instanceId == null) {
            logger.warn("Instance ID not sent in the request");
            return null;
        }
        final InstanceAuthorisation instanceAuthorisation = cloudService.validateToken(instanceId, token, gatewayHost);
        if (instanceAuthorisation == null) {
            logger.warn("Cannot get instance Authorisation for instance {}", instanceId);
        }

        return instanceAuthorisation;
    }

    @Override
    public void onConnect(final SocketIOClient client) {
        logger.debug("Client with websocket id of: {} connected", client.getSessionId());

        final HttpRequest request = new HttpRequest(client.getHandshakeData());
        final InstanceAuthorisation instanceAuthorisation = validateToken(request);

        final Integer screenWidth = request.getIntegerParameter(SCREEN_WIDTH_PARAMETER, 1280);
        final Integer screenHeight = request.getIntegerParameter(SCREEN_HEIGHT_PARAMETER, 720);
        Size screenSize = new Size(screenWidth, screenHeight);

        if (instanceAuthorisation == null) {
            client.disconnect();
            return;
        }

        try {
            final Connection connection = connectionRequestService.createConnection(client, instanceAuthorisation, screenSize);

            if (connection == null) {
                client.disconnect();
                return;
            }

            client.set("connection", connection);
            client.joinRoom(connection.getRoom());

            // Update the number of connected clients to the session
            this.onClientAdded(instanceAuthorisation);

            broadcast(client, USER_CONNECTED);
            broadcast(client, USERS_CONNECTED);

        } catch (OwnerNotConnectedException e) {
            client.sendEvent("owner:away");
            client.disconnect();
        }
    }

    private void onClientAdded(final InstanceAuthorisation instanceAuthorisation) {
        final InstanceMember member = instanceAuthorisation.getMember();
        final Long instanceId = member.getInstanceId();
        InstanceSession session = instanceSessionService.getByInstanceId(instanceId);
        instanceSessionService.onClientAdded(session);
    }
}
