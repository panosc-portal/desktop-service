package eu.panosc.portal.vdi.listeners;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.listener.DisconnectListener;
import eu.panosc.portal.vdi.models.Connection;
import eu.panosc.portal.business.InstanceSessionService;
import eu.panosc.portal.core.domain.InstanceMember;
import eu.panosc.portal.core.domain.InstanceMemberRole;
import eu.panosc.portal.core.domain.InstanceSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static eu.panosc.portal.vdi.events.Event.USER_DISCONNECTED;

public class ClientDisconnectListener extends AbstractListener implements DisconnectListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientDisconnectListener.class);

    private final InstanceSessionService instanceSessionService;

    public ClientDisconnectListener(final InstanceSessionService instanceSessionService) {
        this.instanceSessionService = instanceSessionService;
    }

    @Override
    public void onDisconnect(final SocketIOClient client) {
        final Connection connection = client.get("connection");
        if (connection != null) {
            connection.closeTunnel();

            boolean ownerConnected = isOwnerConnected(client);
            if (!ownerConnected) {
                logger.info("Owner disconnected. Disconnect all clients");
                final SocketIONamespace namespace = client.getNamespace();
                final String room = connection.getRoom();
                disconnectAllRoomClients(namespace, room);
            }

            InstanceSession session = instanceSessionService.getByInstanceId(connection.getInstanceId());
            instanceSessionService.onClientRemoved(session);

            // broadcast an event that a user has disconnected
            broadcast(client, USER_DISCONNECTED);
        }
    }

    /**
     * Invoked when an owner has disconnected from their instance
     *
     * @param namespace the socket io namespace
     * @param room      the room
     */
        private void disconnectAllRoomClients(final SocketIONamespace namespace, final String room) {
        final BroadcastOperations operations = namespace.getRoomOperations(room);
        final Collection<SocketIOClient> clients = operations.getClients();
        for (final SocketIOClient client : clients) {
            client.sendEvent("owner:away");

            client.disconnect();
        }
    }

    private boolean isOwnerConnected(final SocketIOClient client) {
        final Connection connection = client.get("connection");

        final SocketIONamespace namespace = client.getNamespace();
        final String room = connection.getRoom();

        final BroadcastOperations operations = namespace.getRoomOperations(room);
        final Collection<SocketIOClient> clients = operations.getClients();
        for (final SocketIOClient aClient : clients) {
            final Connection aConnection = aClient.get("connection");

            final InstanceMember aMember = aConnection.getInstanceMember();
            if (aMember.isRole(InstanceMemberRole.OWNER)) {
                return true;
            }
        }

        return false;
    }
}

