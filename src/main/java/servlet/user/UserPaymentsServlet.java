package servlet.user;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.impl.UserRepository;
import service.impl.UserService;
import servlet.dto.OutgoingPaymentDto;
import servlet.mapper.PaymentDtoMapper;
import util.ServletUtil;

import java.util.List;
import java.util.UUID;

@WebServlet("/userPayments")
public class UserPaymentsServlet extends HttpServlet {

    private final UserService userService;
    private final PaymentDtoMapper paymentDtoMapper;
    private final ServletUtil servletUtil;
    private final Gson gson;

    public UserPaymentsServlet(UserService userService, PaymentDtoMapper paymentDtoMapper,
                            ServletUtil servletUtil, Gson gson) {
        this.userService = userService;
        this.paymentDtoMapper = paymentDtoMapper;
        this.servletUtil = servletUtil;
        this.gson = gson;
    }

    public UserPaymentsServlet() {
        this.userService = new UserService(UserRepository.getInstance());
        this.paymentDtoMapper = PaymentDtoMapper.getInstance();
        this.servletUtil = new ServletUtil();
        this.gson = new Gson();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        List<OutgoingPaymentDto> payments = userService.getUserPaymentsById(id).stream().map(paymentDtoMapper::toDto).toList();
        String paymentsJson = gson.toJson(payments);

        servletUtil.writeJsonToResponse(paymentsJson, resp);
    }
}
