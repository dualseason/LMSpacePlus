package edu.lingnan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerUIConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.lingnan.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置项目基本信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("岭梦空间接口文档")
                .description("岭梦空间设在第五教学楼4、5楼的南侧的课室，共有18间课室。目前还没投入使用，还在建设中。")
                .termsOfServiceUrl("https://blog.dualseason.com/")
                //请填写项目联系人信息（名称、网址、邮箱）
                .contact(new Contact("陈世杰", "https://blog.dualseason.com/", "dualseason@qq.com"))
                //请填写项目版本号
                .version("1.0")
                .build();
    }
}
