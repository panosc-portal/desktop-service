package eu.panosc.portal.vdi.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import eu.panosc.portal.vdi.models.Connection;
import eu.panosc.portal.business.InstanceSessionService;
import org.apache.guacamole.GuacamoleConnectionClosedException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.io.GuacamoleWriter;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMessageListener implements DataListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(ClientMessageListener.class);

    private final InstanceSessionService instanceSessionService;

    public ClientMessageListener(final InstanceSessionService instanceSessionService) {
        this.instanceSessionService = instanceSessionService;
    }

    @Override
    public void onData(final SocketIOClient client, final String data, final AckRequest ackRequest) {
        final Connection connection = client.get("connection");
        if (connection == null) {
            return;
        }

        final GuacamoleTunnel tunnel = connection.getConnectionThread().getTunnel();
        final GuacamoleWriter writer = tunnel.acquireWriter();

        try {
            writer.write(data.toCharArray());

            // Update last seen time in session
//            instanceSessionService.updateLastSeenAt(connection.getSession());

        } catch (GuacamoleConnectionClosedException exception) {
            logger.debug("Connection to guacd closed.", exception);
        } catch (GuacamoleException exception) {
            logger.debug("WebSocket tunnel write failed.", exception);
        } finally {
            tunnel.releaseWriter();
        }
    }
}
