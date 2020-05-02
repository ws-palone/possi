package fr.istic.iodeman.dto;

import fr.istic.iodeman.models.Person;

public class AuthenticationResponseDTO {
    private Person user;
    private String token;

    public AuthenticationResponseDTO() {

    }

    public AuthenticationResponseDTO(Person user, String token) {
        this.user = user;
        this.token = token;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
