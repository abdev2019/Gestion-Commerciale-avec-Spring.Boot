package gc.rec;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.dbcp.BasicDataSource; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
	public class DbConfig { 
	    @Bean
	    public  BasicDataSource dataSource() throws URISyntaxException {
			URI dbUri = new URI(("mysql://abdev2018:red0657563091@db4free.net:3306/mydb_gcrec2018"));
	
	        String username = dbUri.getUserInfo().split(":")[0];
	        String password = dbUri.getUserInfo().split(":")[1];
	        String dbUrl = "jdbc:mysql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()+"?useSSL=false";
	
	        BasicDataSource  basicDataSource = new BasicDataSource ();
	        basicDataSource.setUrl(dbUrl);
	        basicDataSource.setUsername(username);
	        basicDataSource.setPassword(password);
	         
	
	        return basicDataSource;
	    }
	}
