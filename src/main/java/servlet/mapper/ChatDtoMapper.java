package servlet.mapper;


import entity.Chat;
import servlet.dto.IncomingChatDto;
import servlet.dto.OutgoingChatDto;

public class ChatDtoMapper {

    private static final ChatDtoMapper INSTANCE = new ChatDtoMapper();

    private ChatDtoMapper() {}

    public static ChatDtoMapper getInstance() {
        return INSTANCE;
    }

    public OutgoingChatDto toDto(Chat chat) {
        return new OutgoingChatDto(chat.getName());
    }

    public Chat toEntity(IncomingChatDto chatDto) {
        Chat chat = new Chat();
        chat.setId(chatDto.getId());
        chat.setName(chatDto.getName());
        return chat;
    }
}
