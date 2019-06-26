package io.joonak.account.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ResponseBody
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public AuthFailureHandler() {
        super();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        res.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}
