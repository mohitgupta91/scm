package com.snapdeal.scm.web.core.dto;

import java.util.List;

/**
 * Created by harsh on 25/02/16.
 */
public class RolePersmissionsDTO {

    private String id;
    private List<String> permissions;
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RolePersmissionsDTO that = (RolePersmissionsDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (permissions != null ? !permissions.equals(that.permissions) : that.permissions != null) return false;
        return role != null ? role.equals(that.role) : that.role == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RolePersmissionsDTO{" +
                "id='" + id + '\'' +
                ", permissions=" + permissions +
                ", role='" + role + '\'' +
                '}';
    }
}
