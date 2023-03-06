package org.openxava.web.filters;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

/**
 * tmr
 * 
 * @since 7.1
 * @author Javier Paniza
 */

@WebFilter("/*")
public class ContentSecurityPolicyFilter implements Filter {
	
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	System.out.println("[ContentSecurityPolicyFilter.doFilter] v5"); // tmr
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // TMR nonce-tmr1 tiene que ser generado
        httpResponse.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self' 'nonce-tmr1' 'unsafe-eval'; frame-ancestors 'self'; form-action 'self'");
        chain.doFilter(request, response);
    }

}
