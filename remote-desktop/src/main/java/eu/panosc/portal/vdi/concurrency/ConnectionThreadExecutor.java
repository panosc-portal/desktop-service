package eu.panosc.portal.vdi.concurrency;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Singleton;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class ConnectionThreadExecutor {

    private final ExecutorService executorService = Executors.newCachedThreadPool(new ConnectionThreadFactory());

    public ConnectionThread startConnectionThread(SocketIOClient client, ConfiguredGuacamoleSocket socket) {

        final GuacamoleTunnel tunnel = new SimpleGuacamoleTunnel(socket);

        ConnectionThread connectionThread = new ConnectionThread(client, tunnel);

        this.executorService.submit(connectionThread);

        return connectionThread;
    }
}
