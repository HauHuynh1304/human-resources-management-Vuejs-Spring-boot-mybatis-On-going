package com.company.hrsystem.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.hrsystem.constants.ApiUrlConstant;
import com.company.hrsystem.response.ResponseTemplate;
import com.company.hrsystem.service.CacheService;
import com.company.hrsystem.service.UserDetailsServiceImp;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class RequestFilter extends OncePerRequestFilter {

	@Value("${token.store}")
	private String tokenStore;

	@Value("${system.name}")
	private String system;

	@Value("${system.version}")
	private String version;

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	private UserDetailsServiceImp userDetailsService;

	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private MessageUtil messageUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String requestTokenHeader = tokenUtil.getHeaderFromRequest(request);

		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = tokenUtil.getUsernameFromToken(jwtToken);
				if (!cacheService.isExistsStringInCache(tokenStore, username, jwtToken)) {
					LogUtil.warn(messageUtil.getMessagelangUS("not.valid.access.token"));
					HttpServletResponseUtil.ServletResponse(response,
							new ResponseTemplate(system, version, HttpStatus.FORBIDDEN.value(), null,
									messageUtil.getMessagelangUS("not.valid.access.token"), null));
					return;
				}
			} catch (MalformedJwtException | SignatureException e) {
				LogUtil.warn(messageUtil.getMessagelangUS("not.valid.access.token"));
				LogUtil.error(ExceptionUtils.getStackTrace(e));
				HttpServletResponseUtil.ServletResponse(response,
						new ResponseTemplate(system, version, HttpStatus.FORBIDDEN.value(), null,
								messageUtil.getMessagelangUS("not.valid.access.token"), null));
				return;
			} catch (ExpiredJwtException e) {
				String requestURL = request.getRequestURI().toString();
				if (requestURL
						.equals(StringUtil.apiBuilder(ApiUrlConstant.ROOT_API, ApiUrlConstant.AUTHEN_REFRESH_TOKEN))) {
					request.setAttribute("claims", e.getClaims());
				} else {
					LogUtil.warn(messageUtil.getMessagelangUS("not.valid.access.token"));
					LogUtil.error(ExceptionUtils.getStackTrace(e));
					HttpServletResponseUtil.ServletResponse(response,
							new ResponseTemplate(system, version, HttpStatus.FORBIDDEN.value(), null,
									messageUtil.getMessagelangUS("not.valid.access.token"), null));
					return;
				}
			}
		}

		// Once we get the token validate it.
		if (username != null) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set
			// authentication
			if (tokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}
	
}
