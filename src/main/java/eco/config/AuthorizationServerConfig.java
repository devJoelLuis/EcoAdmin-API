package eco.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.filter.CorsFilter;

import eco.token.CustomTokenEnhancer;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
  
 
    private AuthenticationManager authm;
    private UserDetailsService usdService;
    private PasswordEncoder pe;
    private CorsFilter csf;
    
    @Autowired
    public AuthorizationServerConfig(AuthenticationManager authm, UserDetailsService usdService, PasswordEncoder pe,
			CorsFilter csf) {
		this.authm = authm;
		this.usdService = usdService;
		this.pe = pe;
		this.csf = csf;
	}
    
    

	@Value("${token.key}")
    private String tokenkey;
    
    @Value("${senhaAuthBasic}")
    private String senhaBasicAuth;
    
    @Value("${token.expire}")
    private int tokenExpire;
    
    @Value("${refreshToken.expire}")
    private int refreshTokenExpire;
    
//    @Autowired
//    private UserDetailsService userDts;
  
    @Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		       .withClient("browser")
		       .secret(pe.encode(senhaBasicAuth))
		       .scopes("read", "write") 
		       .authorizedGrantTypes("password", "refresh_token")
		       .accessTokenValiditySeconds(tokenExpire)
		       .refreshTokenValiditySeconds(refreshTokenExpire); // o 2 é duas hora desloga
	}
  
    @Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    	TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		 endpoints
		     .tokenStore(tokenStore())
		     .tokenEnhancer(tokenEnhancerChain)
		     .reuseRefreshTokens(false)
		     .userDetailsService(usdService)
		     .authenticationManager(authm);
	}
    
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    	security.addTokenEndpointAuthenticationFilter(this.csf);
    }

    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter acc  = new JwtAccessTokenConverter();
		acc.setSigningKey(tokenkey);
		return acc;
	}

	@Bean
    public TokenStore tokenStore() {
    	return new JwtTokenStore(accessTokenConverter());
    }
	
	@Bean
	public TokenEnhancer tokenEnhancer() {
	    return new CustomTokenEnhancer();
	}
	
	
  
  
}// fecha classe
