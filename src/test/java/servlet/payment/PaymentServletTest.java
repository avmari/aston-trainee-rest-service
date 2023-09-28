package servlet.payment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Payment;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.impl.PaymentService;
import servlet.dto.IncomingPaymentDto;
import servlet.dto.OutgoingPaymentDto;
import servlet.dto.OutgoingUserDto;
import servlet.mapper.PaymentDtoMapper;
import util.ServletUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServletTest {

    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentDtoMapper paymentDtoMapper;
    @Mock
    private ServletUtil servletUtil;
    @Mock
    private Gson gson;
    @InjectMocks
    private PaymentServlet paymentServlet;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void doGet() {
        Payment payment = getPayment(UUID.randomUUID());

        when(httpServletRequest.getParameter("id")).thenReturn(payment.getId().toString());
        when(paymentService.findById(any(UUID.class))).thenReturn(Optional.of(payment));

        OutgoingPaymentDto paymentDto = new OutgoingPaymentDto(payment.getAmount(),
                new OutgoingUserDto(payment.getUser().getUsername(), payment.getUser().getFirstName(), payment.getUser().getLastName()));
        when(paymentDtoMapper.toDto(payment)).thenReturn(paymentDto);
        when(gson.toJson(any(OutgoingPaymentDto.class))).thenReturn(new Gson().toJson(paymentDto));

        paymentServlet.doGet(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getParameter("id");
        verify(paymentService, times(1)).findById(any(UUID.class));
        verify(paymentDtoMapper, times(1)).toDto(any(Payment.class));
        verify(gson, times(1)).toJson(any(OutgoingPaymentDto.class));
        verify(servletUtil, times(1)).writeJsonToResponse(any(String.class), any(HttpServletResponse.class));
    }

    @Test
    void doPost() throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(httpServletRequest.getReader()).thenReturn(reader);

        Payment payment = getPayment(UUID.randomUUID());

        JsonObject jsonData = new JsonObject();
        JsonObject userJson = new JsonObject();
        jsonData.addProperty("amount", payment.getAmount());
        jsonData.add("user", userJson);
        userJson.addProperty("id", payment.getUser().getId().toString());
        when(gson.fromJson(reader, JsonObject.class)).thenReturn(jsonData);

        IncomingPaymentDto incomingPaymentDto = getPaymentDto(null, payment.getAmount(), payment.getUser().getId());
        OutgoingPaymentDto outgoingPaymentDto = new OutgoingPaymentDto(payment.getAmount(),
                new OutgoingUserDto(payment.getUser().getUsername(), payment.getUser().getFirstName(), payment.getUser().getLastName()));

        when(paymentDtoMapper.toEntity(any(IncomingPaymentDto.class))).thenReturn(payment);
        when(paymentService.save(any(Payment.class))).thenReturn(payment);
        when(paymentDtoMapper.toDto(any(Payment.class))).thenReturn(outgoingPaymentDto);
        when(gson.toJson(any(OutgoingPaymentDto.class))).thenReturn(new Gson().toJson(payment));

        ArgumentCaptor<IncomingPaymentDto> argumentCaptor = ArgumentCaptor.forClass(IncomingPaymentDto.class);

        paymentServlet.doPost(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getReader();
        verify(gson, times(1)).fromJson(reader, JsonObject.class);
        verify(paymentService, times(1)).save(any(Payment.class));
        verify(paymentDtoMapper, times(1)).toEntity(argumentCaptor.capture());
        verify(paymentDtoMapper, times(1)).toDto(any(Payment.class));
        verify(gson, times(1)).toJson(any(OutgoingPaymentDto.class));
        verify(servletUtil, times(1)).writeJsonToResponse(any(String.class), any(HttpServletResponse.class));

        assertEquals(incomingPaymentDto, argumentCaptor.getValue());
    }

    @ParameterizedTest
    @MethodSource("getPaymentAttributesToUpdate")
    void doPut(Integer amount, UUID userId) throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(httpServletRequest.getReader()).thenReturn(reader);

        IncomingPaymentDto expectedArgument = getPaymentDto(UUID.randomUUID(), amount, userId);

        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("id", expectedArgument.getId().toString());
        if (amount != null)
            jsonData.addProperty("amount", expectedArgument.getAmount());
        if (userId != null){
            JsonObject userJson = new JsonObject();
            jsonData.add("user", userJson);
            userJson.addProperty("id", expectedArgument.getUserId().toString());
        }
        when(gson.fromJson(reader, JsonObject.class)).thenReturn(jsonData);

        Payment payment = getPayment(expectedArgument.getId());
        when(paymentDtoMapper.toEntity(any(IncomingPaymentDto.class))).thenReturn(payment);

        ArgumentCaptor<IncomingPaymentDto> argumentCaptor = ArgumentCaptor.forClass(IncomingPaymentDto.class);

        paymentServlet.doPut(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getReader();
        verify(gson, times(1)).fromJson(reader, JsonObject.class);
        verify(paymentDtoMapper, times(1)).toEntity(argumentCaptor.capture());
        verify(paymentService, times(1)).update(any(Payment.class));

        assertEquals(expectedArgument, argumentCaptor.getValue());
    }

    @Test
    void doDelete() {
        when(httpServletRequest.getParameter("id")).thenReturn(UUID.randomUUID().toString());

        paymentServlet.doDelete(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getParameter("id");
        verify(paymentService, times(1)).deleteById(any(UUID.class));
    }

    private Payment getPayment(UUID userId) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setAmount(1000);
        User user = new User();
        user.setId(userId);
        user.setUsername("test_username");
        user.setFirstName("test_first_name");
        user.setLastName("test_last_name");
        payment.setUser(user);
        return payment;
    }

    private IncomingPaymentDto getPaymentDto(UUID id, Integer amount, UUID userId) {
        IncomingPaymentDto paymentDto = new IncomingPaymentDto();
        paymentDto.setId(id);
        paymentDto.setAmount(amount);
        paymentDto.setUserId(userId);
        return paymentDto;
    }

    private static Stream<Arguments> getPaymentAttributesToUpdate() {
        return Stream.of(
                Arguments.of(2000, null),
                Arguments.of(null, UUID.randomUUID()),
                Arguments.of(2000, UUID.randomUUID())
        );
    }
}