package servlet.mapper;


import entity.Chat;
import servlet.dto.ChatDto;

public class ChatDtoMapper {

    private static final ChatDtoMapper INSTANCE = new ChatDtoMapper();

    private ChatDtoMapper() {}

    public static ChatDtoMapper getInstance() {
        return INSTANCE;
    }

    public ChatDto toDto(Chat chat) {
        return new ChatDto(chat.getName());
    }
}
