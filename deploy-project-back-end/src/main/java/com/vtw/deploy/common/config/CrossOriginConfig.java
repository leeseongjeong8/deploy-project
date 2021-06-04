package com.vtw.deploy.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*
 * @author 정진하
 */
@Configuration
public class CrossOriginConfig implements WebMvcConfigurer {//@CrossOrigin 전역 설정
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*");//모든 메서드 허용함
	}
}