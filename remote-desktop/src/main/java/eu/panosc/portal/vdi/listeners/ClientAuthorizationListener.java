package eu.panosc.portal.vdi.listeners;


import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientAuthorizationListener implements AuthorizationListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientAuthorizationListener.class);

    public ClientAuthorizationListener() {
    }


    @Override
    public boolean isAuthorized(final HandshakeData data) {
        return true;
    }
}
