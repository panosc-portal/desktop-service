package eu.panosc.portal.vdi.models;

import eu.panosc.portal.vdi.concurrency.ConnectionThread;
import eu.panosc.portal.core.domain.InstanceMember;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class Connection {

    private final InstanceMember instanceMember;
    private final ConnectionThread connectionThread;

    private String id;

    public Connection(final InstanceMember instanceMember,
                      final ConnectionThread connectionThread) {
        this.instanceMember = instanceMember;
        this.connectionThread = connectionThread;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InstanceMember getInstanceMember() {
        return instanceMember;
    }

    public Long getInstanceId() {
        return this.instanceMember.getInstanceId();
    }

    public UserModel getUser() {
        return new UserModel(this.instanceMember);
    }

    public String getRoom() {
        return this.getInstanceMember().getInstanceId().toString();
    }

    public ConnectionThread getConnectionThread() {
        return connectionThread;
    }

    public void closeTunnel() {
        if (this.connectionThread != null) {
            this.connectionThread.closeTunnel();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Connection that = (Connection) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .toHashCode();
    }
}
