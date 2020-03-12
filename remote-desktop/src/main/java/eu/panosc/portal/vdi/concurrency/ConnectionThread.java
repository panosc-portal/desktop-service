package eu.panosc.portal.vdi.concurrency;

import com.corundumstudio.socketio.SocketIOClient;
import org.apache.guacamole.GuacamoleClientException;
import org.apache.guacamole.GuacamoleConnectionClosedException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.io.GuacamoleReader;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.protocol.GuacamoleInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.guacamole.net.GuacamoleTunnel.INTERNAL_DATA_OPCODE;

public class ConnectionThread implements Runnable {

    private static final Logger          logger = LoggerFactory.getLogger(ConnectionThread.class);
    private final        SocketIOClient  client;
    private final        GuacamoleTunnel tunnel;

    public ConnectionThread(final SocketIOClient client, final GuacamoleTunnel tunnel) {
        this.client = client;
        this.tunnel = tunnel;
    }

    public SocketIOClient getClient() {
        return client;
    }

    public GuacamoleTunnel getTunnel() {
        return tunnel;
    }

    public void closeTunnel() {
        try {
            if (this.tunnel != null) {
                this.tunnel.close();
            }

        } catch (GuacamoleException exception) {
            logger.debug("Unable to close connection to guacd.", exception);
        }
    }

    @Override
    public void run() {
        sendIdentifierInstruction();
        read();
    }

    private void read() {
        final StringBuilder   buffer = new StringBuilder(8192);
        final GuacamoleReader reader = tunnel.acquireReader();
        char[]                readMessage;

        try {
            while ((readMessage = reader.read()) != null) {

                buffer.append(readMessage);

                // Flush if we expect to wait or buffer is getting full
                if (!reader.available() || buffer.length() >= 8192) {
                    client.sendEvent("display", buffer.toString());
                    buffer.setLength(0);
                }

            }
            client.disconnect();
        } catch (GuacamoleClientException exception) {
            logger.info("WebSocket connection terminated: {}", exception.getMessage());
            logger.debug("WebSocket connection terminated due to client error {}", exception.getMessage());
            client.disconnect();
        } catch (GuacamoleConnectionClosedException exception) {
            logger.debug("Connection to guacd closed: {}", exception.getMessage());
            client.disconnect();
        } catch (GuacamoleException exception) {
            logger.error("Connection to guacd terminated abnormally: {}", exception.getMessage());
            logger.debug("Internal error during connection to guacd: {}", exception.getMessage());
            client.disconnect();
        }
    }

    private void sendIdentifierInstruction() {
        final String               uuid        = tunnel.getUUID().toString();
        final GuacamoleInstruction instruction = new GuacamoleInstruction(INTERNAL_DATA_OPCODE, uuid);

        client.sendEvent("display", instruction.toString());
    }
}

