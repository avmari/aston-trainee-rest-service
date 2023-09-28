package servlet.user;

import com.google.gson.Gson;
import entity.Payment;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.impl.UserService;
import servlet.dto.OutgoingPaymentDto;
import servlet.dto.OutgoingUserDto;
import servlet.mapper.PaymentDtoMapper;
import util.ServletUtil;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPaymentsServletTest {
    @Mock
    private UserService userService;
    @Mock
    private PaymentDtoMapper paymentDtoMapper;
    @Mock
    private ServletUtil servletUtil;
    @Mock
    private Gson gson;
    @InjectMocks
    private UserPaymentsServlet userPaymentsServlet;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void doGet() {
        Payment payment1 = getPayment();
        Payment payment2 = getPayment();
        when(httpServletRequest.getParameter("id")).thenReturn(UUID.randomUUID().toString());
        when(userService.getUserPaymentsById(any(UUID.class))).thenReturn(List.of(payment1, payment2));

        List<OutgoingPaymentDto> paymentDtos = List.of(new OutgoingPaymentDto(payment1.getAmount(),
                new OutgoingUserDto(payment1.getUser().getUsername(), payment1.getUser().getFirstName(),
                        payment1.getUser().getLastName())),
                new OutgoingPaymentDto(payment2.getAmount(),
                        new OutgoingUserDto(payment2.getUser().getUsername(), payment2.getUser().getFirstName(),
                                payment2.getUser().getLastName())));
        when(paymentDtoMapper.toDto(payment1)).thenReturn(paymentDtos.get(0));
        when(paymentDtoMapper.toDto(payment2)).thenReturn(paymentDtos.get(1));
        when(gson.toJson(any(List.class))).thenReturn(new Gson().toJson(paymentDtos));

        ArgumentCaptor<List<OutgoingPaymentDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        userPaymentsServlet.doGet(httpServletRequest, httpServletResponse);

        verify(userService, times(1)).getUserPaymentsById(any(UUID.class));
        verify(paymentDtoMapper, times(2)).toDto(any(Payment.class));
        verify(gson, times(1)).toJson(argumentCaptor.capture());
        verify(servletUtil, times(1)).writeJsonToResponse(any(String.class), any(HttpServletResponse.class));

        assertEquals(paymentDtos, argumentCaptor.getValue());
    }

    private Payment getPayment() {
        Payment payment = new Payment();
        payment.setAmount(1000);
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("test_username");
        user.setFirstName("test_first_name");
        user.setLastName("test_last_name");
        payment.setUser(user);
        return payment;
    }
}