package com.company.scaffold.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    private String author = "scaffold";

    private String basePackage = "com.company.scaffold";

    private String moduleName = "system";

    private String tablePrefix = "sys_";

    private String javaOutputPath = "/src/main/java";

    private String resourcesOutputPath = "/src/main/resources";

    private String frontendOutputPath = "D:/workspace/frontend/src";

    private Boolean generateController = true;

    private Boolean generateService = true;

    private Boolean generateMapper = true;

    private Boolean generateEntity = true;

    private Boolean generateVue = true;

    private Boolean generateReact = false;
}
