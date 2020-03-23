package eu.panosc.portal.core.domain;

import java.util.List;

public class InstanceNetwork {

    private String hostname;
    private List<Protocol> protocols;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<Protocol> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<Protocol> protocols) {
        this.protocols = protocols;
    }

    public Integer getPort(String protocolName) {
        return this.protocols.stream().filter(protocol -> protocol.getName().equals(protocolName)).map(Protocol::getPort).findFirst().orElse(null);
    }

    public Integer getInternalPort(String protocolName) {
        return this.protocols.stream().filter(protocol -> protocol.getName().equals(protocolName)).map(Protocol::getInternalPort).findFirst().orElse(null);
    }
}
