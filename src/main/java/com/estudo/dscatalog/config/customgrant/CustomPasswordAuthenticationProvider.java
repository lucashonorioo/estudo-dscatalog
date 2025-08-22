package com.estudo.dscatalog.config.customgrant;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class CustomPasswordAuthenticationProvider implements AuthenticationProvider {

	private static final Logger log = LoggerFactory.getLogger(CustomPasswordAuthenticationProvider.class);
	private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	private final OAuth2AuthorizationService authorizationService;
	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	public CustomPasswordAuthenticationProvider(
			OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
			UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {

		Assert.notNull(authorizationService, "authorizationService cannot be null");
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		Assert.notNull(userDetailsService, "userDetailsService cannot be null");
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomPasswordAuthenticationToken auth = (CustomPasswordAuthenticationToken) authentication;

		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(auth);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>) auth.getAdditionalParameters();

		String username = (String) params.get(OAuth2ParameterNames.USERNAME);
		String rawPassword = (String) params.get(OAuth2ParameterNames.PASSWORD);

		log.debug("PROVIDER: username='{}' hasPassword={}", username, StringUtils.hasText(rawPassword));

		if (!StringUtils.hasText(username) || !StringUtils.hasText(rawPassword)) {
			throw new OAuth2AuthenticationException(new OAuth2Error(
					OAuth2ErrorCodes.INVALID_REQUEST, "username/password ausentes", ERROR_URI));
		}

		UserDetails ud = userDetailsService.loadUserByUsername(username);
		boolean matches = passwordEncoder.matches(rawPassword, ud.getPassword());
		log.debug("PROVIDER: matches? {}", matches);

		if (!matches) {
			throw new BadCredentialsException("Invalid credentials");
		}

		Set<String> requested = auth.getScopes() != null ? auth.getScopes() : Collections.emptySet();
		Set<String> authorizedScopes = requested.isEmpty()
				? new HashSet<>(registeredClient.getScopes())
				: requested.stream().filter(registeredClient.getScopes()::contains).collect(Collectors.toSet());

		if (authorizedScopes.isEmpty()) {
			throw new OAuth2AuthenticationException(new OAuth2Error(
					OAuth2ErrorCodes.INVALID_SCOPE, "Scope inválido", ERROR_URI));
		}

		if (clientPrincipal instanceof AbstractAuthenticationToken aat) {
			aat.setDetails(new CustomUserAuthorities(ud.getUsername(), ud.getAuthorities()));
		}

		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
				.registeredClient(registeredClient)
				.principal(clientPrincipal)
				.authorizationServerContext(AuthorizationServerContextHolder.getContext())
				.authorizedScopes(authorizedScopes)
				.authorizationGrantType(new AuthorizationGrantType("password"))
				.authorizationGrant(auth);

		OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
		if (generatedAccessToken == null) {
			throw new OAuth2AuthenticationException(new OAuth2Error(
					OAuth2ErrorCodes.SERVER_ERROR, "Falha ao gerar o access token", ERROR_URI));
		}

		OAuth2AccessToken accessToken = new OAuth2AccessToken(
				OAuth2AccessToken.TokenType.BEARER,
				generatedAccessToken.getTokenValue(),
				generatedAccessToken.getIssuedAt(),
				generatedAccessToken.getExpiresAt(),
				tokenContext.getAuthorizedScopes()
		);

		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
				.principalName(ud.getUsername())
				.authorizationGrantType(new AuthorizationGrantType("password"))
				.authorizedScopes(authorizedScopes)
				.attribute(Principal.class.getName(), clientPrincipal);
		if (generatedAccessToken instanceof ClaimAccessor ca) {
			authorizationBuilder.token(accessToken, (metadata) ->
					metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ca.getClaims()));
		} else {
			authorizationBuilder.accessToken(accessToken);
		}

		OAuth2Authorization authorization = authorizationBuilder.build();
		this.authorizationService.save(authorization);

		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CustomPasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
		OAuth2ClientAuthenticationToken clientPrincipal = null;
		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
			clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
		}
		if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
			return clientPrincipal;
		}
		throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
	}
}
