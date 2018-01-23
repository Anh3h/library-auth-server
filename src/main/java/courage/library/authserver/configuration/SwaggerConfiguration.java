package courage.library.authserver.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Bean
    public Docket api() {

        List<ResponseMessage> list = new ArrayList<>();
        list.add(new ResponseMessageBuilder().code(401).message("401 - Unauthorized").build());
        list.add(new ResponseMessageBuilder().code(403).message("403 - Forbidden").build());
        list.add(new ResponseMessageBuilder().code(404).message("404 - Not found").build());
        list.add(new ResponseMessageBuilder().code(200).message("200 - Ok").build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("courage.library.authserver.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .globalResponseMessage(RequestMethod.PUT, list);
                //.securitySchemes(Collections.singletonList(securitySchema()))
                //.securityContexts(Collections.singletonList(securityContext()));
    }

    private OAuth securitySchema() {

        final TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint("http://server:port/oauth/token", clientId, clientSecret);
        final TokenEndpoint tokenEndpoint = new TokenEndpoint("http://server:port/oauth/token", "access_token");
        final GrantType grantType = new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint);
        final List<AuthorizationScope> scopes = new ArrayList<>();
        scopes.add(new AuthorizationScope("global", "access all"));
        return new OAuth("oauth2", scopes, Collections.singletonList(grantType));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Authentication Server")
                .description("")
                .termsOfServiceUrl("Terms of serivce")
                .contact(new Contact("Courage Angeh", "", "courageangeh@gmaicom"))
                .license("Open Source")
                .licenseUrl("")
                .version("API V1.0.0")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.ant("/users/**"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {

        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[2];
        authorizationScopes[0] = new AuthorizationScope("read", "read all");
        authorizationScopes[1] = new AuthorizationScope("write", "write all");

        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
    }

}
