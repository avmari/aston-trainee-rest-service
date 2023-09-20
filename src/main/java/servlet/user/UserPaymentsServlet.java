package servlet.user;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.impl.UserService;
import servlet.dto.OutgoingPaymentDto;
import servlet.mapper.PaymentDtoMapper;
import util.ServletUtil;

import java.util.List;
import java.util.UUID;

@WebServlet("/userPayments")
public class UserPaymentsServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final PaymentDtoMapper paymentDtoMapper = PaymentDtoMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        List<OutgoingPaymentDto> payments = userService.getUserPaymentsById(id).stream().map(paymentDtoMapper::toDto).toList();
        String paymentsJson = new Gson().toJson(payments);

        ServletUtil.writeJsonToResponse(paymentsJson, resp);
    }
}
