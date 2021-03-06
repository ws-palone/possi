package fr.istic.iodeman;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Application extends SpringApplication
{

    public static void main( String[] args )
    {
        System.out.println( "Starting Spring context..." );
        SpringApplication.run(Application.class, args);
    }

}
