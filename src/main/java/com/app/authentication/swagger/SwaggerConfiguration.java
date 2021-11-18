package com.app.authentication.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfiguration implements WebMvcConfigurer {

  @Bean
  public Docket api(){
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .securityContexts(Arrays.asList(securityContext()))
            .securitySchemes(Arrays.asList(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build();
  }


  private ApiInfo apiInfo() {
    return new ApiInfo(
            "Tutorial JWT",
            "Descripcion",
            "1.0",
            "Terminos y condiciones",
            new Contact("Luigi code", "luigi.com", "roberthdrums@gmail.com"),
            "Licencia",
            "apilicencia.com",
            //Collections.EMPTY_LIST
            new ArrayList<VendorExtension>()
    );
  }


  private ApiKey apiKey(){
    return new ApiKey("JWT", "Authorization", "header");
  }


  private SecurityContext securityContext(){
    return SecurityContext.builder().securityReferences(defaultAuth()).build();
  }


  private List<SecurityReference> defaultAuth(){
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
  }


}
