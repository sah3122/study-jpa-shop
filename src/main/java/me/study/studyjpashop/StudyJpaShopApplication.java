package me.study.studyjpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StudyJpaShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyJpaShopApplication.class, args);
    }

    @Bean // Hibernate5Module은 Lazy로 설정되어 있는 필드는 조회하지 않는다.
    public Hibernate5Module hibernate5Module() { // 일반적으로 사용하지 않는다.
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        //hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true); // Lazy로딩으로 되어 있는 필드를 강제로 조회 하도록
        return hibernate5Module;
    }
}
