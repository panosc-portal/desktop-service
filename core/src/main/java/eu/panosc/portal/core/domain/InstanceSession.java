package eu.panosc.portal.core.domain;

import java.util.Date;

public class InstanceSession extends Timestampable {

    private Long id;
    private Long clientCount = 0L;
    private String connectionId;
    private Date lastSeenAt = new Date();
    private Long instanceId;

    public InstanceSession() {
    }

    public InstanceSession(Long instanceId, String connectionId) {
        this.instanceId = instanceId;
        this.connectionId = connectionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public void updateLastSeenAt() {
        this.lastSeenAt = new Date();
    }

    public Date getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(Date lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public void incrementClientCount() {
        this.clientCount = this.clientCount + 1;
    }

    public void decrementClientCount() {
        this.clientCount = this.clientCount - 1;
    }

    public Long getClientCount() {
        return clientCount;
    }

    public void setClientCount(Long clientCount) {
        this.clientCount = clientCount;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

}
