package servlet.dto;

import java.util.UUID;

public record IncomingPaymentDto(Integer amount, UUID userId) {
}
