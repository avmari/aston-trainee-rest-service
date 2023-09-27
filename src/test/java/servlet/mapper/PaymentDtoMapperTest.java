package servlet.mapper;

import entity.Payment;
import entity.User;
import org.junit.jupiter.api.Test;
import servlet.dto.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDtoMapperTest {

    private final PaymentDtoMapper paymentDtoMapper = PaymentDtoMapper.getInstance();

    @Test
    void toDto() {
        Payment payment = getPayment();
        OutgoingPaymentDto expectedResult = new OutgoingPaymentDto(payment.getAmount(),
                new OutgoingUserDto(payment.getUser().getUsername(), payment.getUser().getFirstName(),
                        payment.getUser().getLastName()));

        OutgoingPaymentDto actualResult = paymentDtoMapper.toDto(payment);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void toEntity() {
        IncomingPaymentDto paymentDto = getPaymentDto();

        Payment expectedResult = new Payment();
        expectedResult.setId(paymentDto.getId());
        expectedResult.setAmount(paymentDto.getAmount());
        User user = new User();
        user.setId(paymentDto.getUserId());
        expectedResult.setUser(user);

        Payment actualResult = paymentDtoMapper.toEntity(paymentDto);

        assertEquals(expectedResult, actualResult);
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

    private IncomingPaymentDto getPaymentDto() {
        IncomingPaymentDto paymentDto = new IncomingPaymentDto();
        paymentDto.setId(UUID.randomUUID());
        paymentDto.setAmount(1000);
        paymentDto.setUserId(UUID.randomUUID());
        return paymentDto;
    }
}