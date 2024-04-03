package io.hexlet;

import java.util.Objects;

public class User {
    private Long id;
    private String username;
    private String phone;

    public User(Long id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }

    public User(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId())
                && Objects.equals(getUsername(), user.getUsername())
                && Objects.equals(getPhone(), user.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPhone());
    }
}
