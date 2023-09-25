package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat chat)) return false;
        return Objects.equals(getId(), chat.getId()) && Objects.equals(name, chat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getId());
    }
}
