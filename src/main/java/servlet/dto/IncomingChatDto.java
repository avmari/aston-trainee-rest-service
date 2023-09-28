package servlet.dto;

import java.util.Objects;
import java.util.UUID;

public class IncomingChatDto {

    private UUID id;
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IncomingChatDto chatDto)) return false;
        return Objects.equals(id, chatDto.id) && Objects.equals(name, chatDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
