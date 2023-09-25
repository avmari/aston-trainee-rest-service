package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity {

    private String username;

    private String firstName;

    private String lastName;

    private List<Chat> chats = new ArrayList<>();

    private List<Payment> payments = new ArrayList<>();

    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, getId());
    }
}

