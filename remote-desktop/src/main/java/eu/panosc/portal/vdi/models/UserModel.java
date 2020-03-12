package eu.panosc.portal.vdi.models;

import eu.panosc.portal.core.domain.InstanceMember;
import eu.panosc.portal.core.domain.User;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserModel {

    private Long   id;
    private String fullName;
    private String role;

    public UserModel() {

    }

    public UserModel(final InstanceMember member) {
        final User user = member.getUser();
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.role = member.getRole().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("fullName", fullName)
            .append("role", role)
            .toString();
    }
}
