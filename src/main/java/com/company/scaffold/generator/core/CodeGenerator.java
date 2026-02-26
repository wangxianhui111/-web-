package com.company.scaffold.generator.core;

import cn.hutool.core.util.StrUtil;
import com.company.scaffold.generator.config.GeneratorProperties;
import com.company.scaffold.generator.data.ColumnInfo;
import com.company.scaffold.generator.data.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CodeGenerator {

    private final GeneratorProperties properties;

    private final Map<String, String> templateCache = new HashMap<>();

    public void generate(TableInfo tableInfo) {
        log.info("开始生成代码，表名: {}", tableInfo.getTableName());

        VelocityEngine velocityEngine = initVelocityEngine();

        if (properties.getGenerateEntity()) {
            generateEntity(velocityEngine, tableInfo);
        }

        if (properties.getGenerateMapper()) {
            generateMapper(velocityEngine, tableInfo);
            generateMapperXml(velocityEngine, tableInfo);
        }

        if (properties.getGenerateService()) {
            generateService(velocityEngine, tableInfo);
            generateServiceImpl(velocityEngine, tableInfo);
        }

        if (properties.getGenerateController()) {
            generateController(velocityEngine, tableInfo);
        }

        if (properties.getGenerateVue()) {
            generateVue(velocityEngine, tableInfo);
        }

        if (properties.getGenerateReact()) {
            generateReact(velocityEngine, tableInfo);
        }

        log.info("代码生成完成，表名: {}", tableInfo.getTableName());
    }

    private VelocityEngine initVelocityEngine() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty("input.encoding", "UTF-8");
        props.setProperty("output.encoding", "UTF-8");

        VelocityEngine velocityEngine = new VelocityEngine(props);
        velocityEngine.init();
        return velocityEngine;
    }

    private VelocityContext createContext(TableInfo tableInfo) {
        VelocityContext context = new VelocityContext();

        context.put("tableName", tableInfo.getTableName());
        context.put("tableComment", tableInfo.getTableComment());
        context.put("className", tableInfo.getClassName());
        context.put("moduleName", tableInfo.getModuleName());
        context.put("packageName", tableInfo.getPackageName());
        context.put("entityName", tableInfo.getEntityName());
        context.put("mappingPath", tableInfo.getMappingPath());
        context.put("firstLowerClassName", tableInfo.getFirstLowerClassName());

        context.put("columns", tableInfo.getColumns());
        context.put("primaryKey", tableInfo.getPrimaryKey());
        context.put("hasBigDecimal", tableInfo.isHasBigDecimal());
        context.put("hasDate", tableInfo.isHasDate());
        context.put("hasLocalDateTime", tableInfo.isHasLocalDateTime());

        context.put("author", properties.getAuthor());
        context.put("basePackage", properties.getBasePackage());
        context.put("modulePackage", properties.getBasePackage() + ".module." + properties.getModuleName());
        context.put("date", new Date());
        context.put("strUtil", new StrUtil());

        return context;
    }

    private void generateEntity(VelocityEngine ve, TableInfo tableInfo) {
        String templateName = "templates/java/entity.java.vm";
        String fileName = getJavaFilePath(tableInfo.getPackageName(), "entity", tableInfo.getClassName() + ".java");

        generateFile(ve, templateName, tableInfo, fileName);
    }

    private void generateMapper(VelocityEngine ve, TableInfo tableInfo) {
        String templateName = "templates/java/mapper.java.vm";
        String fileName = getJavaFilePath(tableInfo.getPackageName(), "mapper",
                tableInfo.getClassName() + "Mapper.java");

        generateFile(ve, templateName, tableInfo, fileName);
    }

    private void generateMapperXml(VelocityEngine ve, TableInfo tableInfo) {
        String templateName = "templates/xml/mapper.xml.vm";
        String fileName = getResourcesFilePath("mapper/" + properties.getModuleName(),
                tableInfo.getClassName() + "Mapper.xml");

        generateFile(ve, templateName, tableInfo, fileName);
    }

    private void generateService(VelocityEngine ve, TableInfo tableInfo) {
        String templateName = "templates/java/service.java.vm";
        String fileName = getJavaFilePath(tableInfo.getPackageName(), "service",
                "I" + tableInfo.getClassName() + "Service.java");

        generateFile(ve, templateName, tableInfo, fileName);
    }

    private void generateServiceImpl(VelocityEngine ve, TableInfo tableInfo) {
        String templateName = "templates/java/serviceImpl.java.vm";
        String fileName = getJavaFilePath(tableInfo.getPackageName(), "service/impl",
                tableInfo.getClassName() + "ServiceImpl.java");

        generateFile(ve, templateName, tableInfo, fileName);
    }

    private void generateController(VelocityEngine ve, TableInfo tableInfo) {
        String templateName = "templates/java/controller.java.vm";
        String fileName = getJavaFilePath(tableInfo.getPackageName(), "controller",
                tableInfo.getClassName() + "Controller.java");

        generateFile(ve, templateName, tableInfo, fileName);
    }

    private void generateVue(VelocityEngine ve, TableInfo tableInfo) {
        String templateName = "templates/vue3/api.js.vm";
        String apiDir = properties.getFrontendOutputPath() + "/api/" + properties.getModuleName();
        String apiFileName = apiDir + "/" + tableInfo.getFirstLowerClassName() + ".js";

        generateFile(ve, templateName, tableInfo, apiFileName);

        String listTemplateName = "templates/vue3/list.vue.vm";
        String viewDir = properties.getFrontendOutputPath() + "/views/" + properties.getModuleName();
        String listFileName = viewDir + "/" + tableInfo.getFirstLowerClassName() + "/index.vue";

        generateFile(ve, listTemplateName, tableInfo, listFileName);
    }

    private void generateReact(VelocityEngine ve, TableInfo tableInfo) {
        String templateName = "templates/react/api.ts.vm";
        String apiDir = properties.getFrontendOutputPath() + "/api/" + properties.getModuleName();
        String apiFileName = apiDir + "/" + tableInfo.getFirstLowerClassName() + ".ts";

        generateFile(ve, templateName, tableInfo, apiFileName);
    }

    private void generateFile(VelocityEngine ve, String templateName, TableInfo tableInfo, String fileName) {
        try {
            Template template = ve.getTemplate(templateName, "UTF-8");
            VelocityContext context = createContext(tableInfo);

            StringWriter sw = new StringWriter();
            template.merge(context, sw);
            String content = sw.toString();

            File file = new File(fileName);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            Files.writeString(Paths.get(fileName), content);
            log.info("生成文件: {}", fileName);

        } catch (Exception e) {
            log.error("生成文件失败: {} - {}", fileName, e.getMessage());
        }
    }

    private String getJavaFilePath(String packageName, String subPackage, String fileName) {
        String basePath = System.getProperty("user.dir");
        String packagePath = packageName.replace(".", "/");
        return basePath + properties.getJavaOutputPath() + "/" + packagePath + "/" + subPackage + "/" + fileName;
    }

    private String getResourcesFilePath(String subPackage, String fileName) {
        String basePath = System.getProperty("user.dir");
        return basePath + properties.getResourcesOutputPath() + "/" + subPackage + "/" + fileName;
    }
}
