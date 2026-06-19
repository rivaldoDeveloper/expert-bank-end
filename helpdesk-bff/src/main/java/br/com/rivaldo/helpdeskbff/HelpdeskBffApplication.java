package br.com.rivaldo.helpdeskbff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class HelpdeskBffApplication {

	public static void main(String[] args) {
        System.setProperty("spring.application.name", "helpdesk-bff");
		SpringApplication.run(HelpdeskBffApplication.class, args);
	}

}
