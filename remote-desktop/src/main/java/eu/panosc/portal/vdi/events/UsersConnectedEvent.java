package eu.panosc.portal.vdi.events;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import eu.panosc.portal.vdi.models.Connection;
import eu.panosc.portal.vdi.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsersConnectedEvent {

    private final static Logger logger = LoggerFactory.getLogger(UsersConnectedEvent.class);

    public UsersConnectedEvent(final BroadcastOperations operations) {
        final List<UserModel>            users   = new ArrayList<>();
        final Collection<SocketIOClient> clients = operations.getClients();
        logger.info("Clients: {}", clients.size());
        for (SocketIOClient client : clients) {
            logger.info("Client session id: {}", client.getSessionId());
            final Connection connection = client.get("connection");
            users.add(connection.getUser());
        }
        logger.info("Users connected: {}", users);
        operations.sendEvent("users:connected", users);
    }
}
