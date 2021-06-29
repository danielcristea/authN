package tech.ing.authorization.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RequestLoggingInterceptor extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final long start = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            if( logger.isInfoEnabled() ) {
                final long end = System.nanoTime();
                logger.info(buildMessage(request, end - start));
            }
        }
    }

    private String buildMessage(final HttpServletRequest request, final long executionTime) {

        return "method=" + request.getMethod() +
                " uri=" + request.getRequestURI() +
                " executionTime=" + TimeUnit.MILLISECONDS.convert(executionTime, TimeUnit.NANOSECONDS) + " ms";
    }
}
