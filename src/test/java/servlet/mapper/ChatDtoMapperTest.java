package servlet.mapper;

import entity.Chat;
import org.junit.jupiter.api.Test;
import servlet.dto.IncomingChatDto;
import servlet.dto.OutgoingChatDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChatDtoMapperTest {

    private final ChatDtoMapper chatDtoMapper = ChatDtoMapper.getInstance();

    @Test
    void toDto() {
        Chat chat = getChat();
        OutgoingChatDto expectedResult = new OutgoingChatDto(chat.getName());

        OutgoingChatDto actualResult = chatDtoMapper.toDto(chat);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void toEntity() {
        IncomingChatDto chatDto = getChatDto();
        Chat expectedResult = new Chat();
        expectedResult.setId(chatDto.getId());
        expectedResult.setName(chatDto.getName());

        Chat actualResult = chatDtoMapper.toEntity(chatDto);

        assertEquals(expectedResult, actualResult);
    }

    private Chat getChat() {
        Chat chat = new Chat();
        chat.setName("test_chat");
        return chat;
    }

    private IncomingChatDto getChatDto() {
        IncomingChatDto chatDto = new IncomingChatDto();
        chatDto.setId(UUID.randomUUID());
        chatDto.setName("test_chat_dto");
        return chatDto;
    }
}