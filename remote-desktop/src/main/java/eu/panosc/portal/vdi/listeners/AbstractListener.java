package eu.panosc.portal.vdi.listeners;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import eu.panosc.portal.vdi.events.Event;
import eu.panosc.portal.vdi.events.UserConnectedEvent;
import eu.panosc.portal.vdi.events.UserDisconnectedEvent;
import eu.panosc.portal.vdi.events.UsersConnectedEvent;
import eu.panosc.portal.vdi.models.Connection;

public class AbstractListener {


    protected void broadcast(final SocketIOClient client, final Event event) {
        final Connection          connection = client.get("connection");
        final SocketIONamespace   namespace  = client.getNamespace();
        final BroadcastOperations operations = namespace.getRoomOperations(connection.getRoom());

        switch (event) {
            case USER_CONNECTED:
                new UserConnectedEvent(client, operations);
                break;
            case USERS_CONNECTED:
                new UsersConnectedEvent(operations);
                break;
            case USER_DISCONNECTED:
                new UserDisconnectedEvent(client, operations);
                new UsersConnectedEvent(operations);
                break;
        }
    }
}
