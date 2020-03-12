package eu.panosc.portal.vdi.services;

import com.corundumstudio.socketio.HandshakeData;

import java.util.UUID;

public class HttpRequest {

    private final HandshakeData handshakeData;

    public HttpRequest(final HandshakeData handshakeData) {
        this.handshakeData = handshakeData;
    }

    public String getStringParameter(final String key) {
        return this.handshakeData.getSingleUrlParam(key);
    }

    public UUID getUUIDParameter(final String key) {
        final String value = this.getStringParameter(key);
        try {
            if (value == null) {
                return null;
            }
            return UUID.fromString(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public Integer getIntegerParameter(final String key) {
        final String value = this.getStringParameter(key);
        try {
            if (value == null) {
                return null;
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public Integer getIntegerParameter(final String key, final Integer defaultValue) {
        final Integer value = this.getIntegerParameter(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
