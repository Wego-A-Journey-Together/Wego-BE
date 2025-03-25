package com.example.wegobe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// RestTemplate을 Bean으로 등록하는 설정 클래스
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 스프링 컨테이너에 등록된 RestTemplate 인스턴스를 사용 가능하게 함
        return new RestTemplate();
    }
}
