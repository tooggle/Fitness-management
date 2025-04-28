package org.fm.backend.model;

public class Manager {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "email='" + email + '\'' +
                '}';
    }
}
