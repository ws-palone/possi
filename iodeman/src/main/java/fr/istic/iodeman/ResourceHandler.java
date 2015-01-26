package fr.istic.iodeman;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ResourceHandler extends WebMvcConfigurerAdapter {

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
			registry.addResourceHandler("/**").addResourceLocations(new String[] {
						"classpath:/META-INF/resources/", "classpath:/resources/",
						"classpath:/static/", "classpath:/public/" 
			});
	}
	
}
