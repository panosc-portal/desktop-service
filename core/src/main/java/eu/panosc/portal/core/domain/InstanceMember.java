package eu.panosc.portal.core.domain;

public class InstanceMember {

    private User user;
    private InstanceMemberRole role;
    private Long instanceId;

    public InstanceMember() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InstanceMemberRole getRole() {
        return role;
    }

    public boolean isRole(InstanceMemberRole role) {
        return this.role == role;
    }

    public void setRole(InstanceMemberRole role) {
        this.role = role;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }
}
