package io.joonak.account.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.joonak.account.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws ServletException, IOException {
        clearAuthenticationAttributes(req);

        var securityAccount = (AccountDto.SecurityAccount) auth.getPrincipal();

        res.setStatus(HttpStatus.OK.value());
        res.setContentType("application/json");
        res.getWriter().println(mapper.writeValueAsString(securityAccount.getAccount()));
    }

}
