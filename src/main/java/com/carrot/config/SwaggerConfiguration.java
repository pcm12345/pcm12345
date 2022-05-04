package com.carrot.config;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.carrot.common.CommonEnum;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfiguration {
	
	@Bean
	public Docket api() throws Exception {
		
		InetAddress ip = InetAddress.getLocalHost();
		String ipAddress = ip.getHostAddress();
		log.info("#### swagger current ip... : " + ipAddress);
		
		Docket doc = new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()));
		
		// 해당 부분은 test stage(개발테스트), pord(운영) 모드에서 IP를 host 해주는 부분
		if(ipAddress.equals(CommonEnum.ipStage.getValue()) ) {
			doc.host(CommonEnum.publicIpStage.getValue());
		} else if (ipAddress.equals(CommonEnum.ipProd.getValue()) ) {
			doc.host(CommonEnum.publicIpProd.getValue());
		}
		
		return doc.select()
				//.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)) // RestController 어노테이션이 붙은 클래스들
				.apis(RequestHandlerSelectors.basePackage("com.carrot"))
				.paths(PathSelectors.any())
				//.apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 추출
				//.paths(PathSelectors.ant("/rest/**")) // 그 중 해당경로 인 URL만 필터링
				.build()
				.globalResponseMessage(RequestMethod.GET, getArrayList())
				.useDefaultResponseMessages(false);
	}

	private ApiKey apiKey() {
		return new ApiKey("accessToken", "accessToken", "header");
	}

	private SecurityContext securityContext() {
		return springfox
				.documentation
				.spi.service
				.contexts
				.SecurityContext
				.builder()
				.securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("accessToken", authorizationScopes));
	}

	private ArrayList<ResponseMessage> getArrayList() {
		ArrayList<ResponseMessage> lists = new ArrayList<ResponseMessage>();
		
		lists.add(new ResponseMessageBuilder().code(100).message("ID중복").build());
		lists.add(new ResponseMessageBuilder().code(104).message("사용자없음").build());
		lists.add(new ResponseMessageBuilder().code(140).message("잘못된 비밀번호 입력").build());
		lists.add(new ResponseMessageBuilder().code(403).message("권한없음").build()); 
		
		return lists;

	}
	
	private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Template API")
                .description("[Template] API")
                .contact(new Contact("Template Swagger", "http://www.carrotians.net/intranet_snew/intro.html", "carrotsds@carrotglobal.com")) // 두번째는 see more at "" , 세번째는 Contact the developer 에 메일주소
                .version("1.0")
                .build();
    }

}
