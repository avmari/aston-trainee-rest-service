package entity;

import java.util.ArrayList;
import java.util.List;

public class Chat extends Entity {

    private String name;

    private List<User> users = new ArrayList<>();


    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }
}
