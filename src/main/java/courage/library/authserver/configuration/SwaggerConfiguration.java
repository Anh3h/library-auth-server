package courage.library.authserver.configuration;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {

        List<ResponseMessage> list = new ArrayList<>();
        list.add(new ResponseMessageBuilder().code(401).message("401 - Unauthorized").build());
        list.add(new ResponseMessageBuilder().code(403).message("403 - Forbidden").build());
        list.add(new ResponseMessageBuilder().code(404).message("404 - Not found").build());
        list.add(new ResponseMessageBuilder().code(200).message("200 - Ok").build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .globalResponseMessage(RequestMethod.PUT, list);
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


}
