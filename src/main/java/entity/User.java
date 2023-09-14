package entity;

import java.util.ArrayList;
import java.util.List;

public class User extends Entity {

    private String username;

    private String firstName;

    private String lastName;

    private List<Chat> chats = new ArrayList<>();

    private List<Payment> payments = new ArrayList<>();


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
}

