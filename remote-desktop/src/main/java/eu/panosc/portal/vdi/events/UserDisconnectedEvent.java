package eu.panosc.portal.vdi.events;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import eu.panosc.portal.vdi.models.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDisconnectedEvent {

    private static final Logger logger = LoggerFactory.getLogger(UserDisconnectedEvent.class);

    public UserDisconnectedEvent(final SocketIOClient client, final BroadcastOperations operations) {
        final Connection connection = client.get("connection");
        logger.info("Disconnected: {}", connection.getUser().getFullName());
        client.leaveRoom(connection.getRoom());

        operations.sendEvent("user:disconnected", connection.getUser());
    }
}
