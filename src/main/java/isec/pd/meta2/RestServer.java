package isec.pd.meta2;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import isec.pd.meta2.security.RsaKeysProperties;
import isec.pd.meta2.security.UserAuthenticationProvider;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RestServer {

	private final RsaKeysProperties rsaKeys;

	public RestServer(RsaKeysProperties rsaKeys)
	{
		this.rsaKeys = rsaKeys;
	}

	@Configuration
	@EnableWebSecurity
	public class SecurityConfig
	{
		@Autowired
		private UserAuthenticationProvider authProvider;

		@Autowired
		public void configAuthentication(AuthenticationManagerBuilder auth)
		{
			auth.authenticationProvider(authProvider);
		}

		@Bean
		public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception
		{
			return http
				.csrf(csrf -> csrf.disable())
				.securityMatcher("/login")
				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
		}

		@Bean
		public SecurityFilterChain unauthenticatedFilterChain(HttpSecurity http) throws Exception
		{
			return http
				.csrf(csrf -> csrf.disable())
				.securityMatcher("/register")
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
		}

		@Bean
		public SecurityFilterChain genericFilterChain(HttpSecurity http) throws Exception
		{
			return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
		}
	}

	@Bean
	JwtEncoder jwtEncoder()
	{
		JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwkSource);
	}

	@Bean
	JwtDecoder jwtDecoder()
	{
		return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
	}
	/*public static void main(String[] args) {
		if(args.length != 4){
			System.out.println("Wrong syntax! java Main port DatabaseLocation RMIServiceName RMIPort");
			return;
		}

		File databaseDirectory = new File(args[1].trim());

		if(!databaseDirectory.exists()){
			System.out.println("A directoria " + databaseDirectory + " nao existe!");
			return;
		}

		if(!databaseDirectory.isDirectory()){
			System.out.println("O caminho " + databaseDirectory + " nao se refere a uma diretoria!");
			return;
		}

		if(!databaseDirectory.canWrite()){
			System.out.println("Sem permissoes de escrever na diretoria " + databaseDirectory + "!");
			return;
		}
		if(!DatabaseManager.connect(args[1])){
			System.out.println("Error while connecting to the Database.");
			return;
		}
		System.out.println("Connection to SQLite has been established.");
		ServerMain serverMain = new ServerMain(args);
		RmiManager rmiManager;
		try {
			rmiManager = new RmiManager(args[2], databaseDirectory, Integer.parseInt(args[3]), serverMain.getServerVariable());
			if(!rmiManager.registerService())
				throw new RemoteException();
			System.out.println("RMI Service is Online!");
		}catch (RemoteException e) {
			System.out.println("Error while creating the RMI manager: " + e);
			System.exit(1);
			return;
		} catch (SocketException e) {
			System.out.println("Error while connecting socket to Multicast Group." + e);
			System.exit(1);
			return;
		}
		serverMain.start();
		//DatabaseManager.connect("Database");
		SpringApplication.run(TpPdMeta2Application.class);
		try{
			serverMain.join();
			rmiManager.getRmiHeartBeatThread().join();
		}
		catch (InterruptedException ignored){}
	}*/

}
