package gc.rec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@ServletComponentScan
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	

	
	@Configuration
	static class ConfigFormatter extends WebMvcConfigurerAdapter {
		@Autowired private gc.rec.imetier.IClientMetier mc;
		@Autowired private gc.rec.imetier.IFournisseurMetier mf;
	    @Override
	    public void addFormatters(FormatterRegistry registry) { 
	    	registry.addConverter(new gc.rec.conversions.ClientConverter(mc));
	    	registry.addConverter(new gc.rec.conversions.FournisseurConverter(mf));
	    }
	     

	    /*@Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/resources/**")
	            .addResourceLocations("/resources/**");
	    }*/
	}

}
