package workflow.configuracion;



import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class DataUserConfiguration {

   @Bean
  public UserDetailsManager usersCustom(DataSource dataSource) {
    JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
      users.setUsersByUsernameQuery("SELECT email, password, enabled FROM usuarios u WHERE email =?");
      users.setAuthoritiesByUsernameQuery("SELECT email, rol FROM usuarios WHERE email = ?");
     
       return users;
  }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/login", "/logout","usuarios/registro/**", "/public/**","/auth/**","/session-status").permitAll()
            		.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            		.requestMatchers("/usuarios/vacantes/**").permitAll()
            		.requestMatchers("/usuarios/**").permitAll()
            		.requestMatchers("/admin/**").permitAll()
            		.requestMatchers("/categorias/**").permitAll()
            		.requestMatchers("/vacantes/**").permitAll()
            		.requestMatchers("/solicitudes/**").permitAll()
            		.requestMatchers("/empresas/**").permitAll()
            		
            		
    
            		

            		.anyRequest().authenticated()
            		
            		)
            		.logout(logout -> logout
            				 .logoutUrl("/logout")
            		            .invalidateHttpSession(true) 
            		            .deleteCookies("JSESSIONID") 
            		            .logoutSuccessHandler((request, response, authentication) -> {
            		                response.setStatus(HttpServletResponse.SC_OK);
            		            })
            				);
            		
            				
        	return http.build();
    	}
    
    //Encriptar las contraseñas , password en bbdd de varchar de 100 o +
    @Bean
   public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
    
   // @Bean
   // public PasswordEncoder passwordEncoder() {
       // return NoOpPasswordEncoder.getInstance(); // ¡NO usar en producción!
   // }
    
    
    
    
    
}
