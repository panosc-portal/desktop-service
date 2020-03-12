package eu.panosc.portal.vdi.events;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import eu.panosc.portal.vdi.models.Connection;
import eu.panosc.portal.vdi.models.UserModel;

public class UserConnectedEvent {

    public UserConnectedEvent(final SocketIOClient client,
                              final BroadcastOperations operations) {
        final Connection connection = client.get("connection");
        final UserModel  user       = connection.getUser();
        operations.sendEvent("user:connected", client, user);

    }
}
