package study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import study.repositories.LinkInfoRepository;
import study.repositories.UserRepository;


@SpringBootApplication
public class UrlCutterApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(UrlCutterApplication.class, args);
        
    }
}
