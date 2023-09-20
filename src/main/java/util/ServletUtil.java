package util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public final class ServletUtil {

    private ServletUtil() { }

    public static void writeJsonToResponse(String json, HttpServletResponse resp) {
        try (PrintWriter out = resp.getWriter()) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
