package workflow.configuracion;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
	
	/**Necesario porque el front y el back tienen distintos puertos , aunque ya tengamos lo del CrossOrigin*/
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            	public void addCorsMappings(CorsRegistry registry) {
            		registry.addMapping("/**")
            		  .allowedOrigins("http://localhost:4200") // Angular en VSC,
                        .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
                        .allowedHeaders("*")
                       .allowCredentials(true);//permitir cookies de sesiones
            		
            }
        };
    }



}
