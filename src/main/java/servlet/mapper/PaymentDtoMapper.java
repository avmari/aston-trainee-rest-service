package servlet.mapper;

import entity.Payment;
import entity.User;
import servlet.dto.IncomingPaymentDto;
import servlet.dto.OutgoingPaymentDto;

public class PaymentDtoMapper {

    private static final PaymentDtoMapper INSTANCE = new PaymentDtoMapper();
    private static final UserDtoMapper userDtoMapper = UserDtoMapper.getInstance();

    private PaymentDtoMapper() {}
    public static PaymentDtoMapper getInstance() {
        return INSTANCE;
    }

    public OutgoingPaymentDto toDto(Payment payment) {
        return new OutgoingPaymentDto(payment.getAmount(), userDtoMapper.toDto(payment.getUser()));
    }

    public Payment toEntity(IncomingPaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setId(paymentDto.getId());
        payment.setAmount(paymentDto.getAmount());

        User user = new User();
        user.setId(paymentDto.getUserId());
        payment.setUser(user);

        return payment;
    }
}
