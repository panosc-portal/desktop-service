package eu.panosc.portal.vdi.models;

import eu.panosc.portal.core.domain.InstanceMember;
import eu.panosc.portal.core.domain.User;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserModel {

    private Long   id;
    private String email;
    private String role;

    public UserModel() {

    }

    public UserModel(final InstanceMember member) {
        final User user = member.getUser();
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = member.getRole().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
            .append("email", email)
            .append("role", role)
            .toString();
    }
}
