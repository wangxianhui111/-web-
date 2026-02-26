package com.company.scaffold.generator.controller;

import com.company.scaffold.common.core.result.Result;
import com.company.scaffold.generator.config.GeneratorProperties;
import com.company.scaffold.generator.core.CodeGenerator;
import com.company.scaffold.generator.core.TableInfoReader;
import com.company.scaffold.generator.data.TableInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generator")
@RequiredArgsConstructor
@Tag(name = "代码生成器", description = "根据数据库表结构自动生成代码")
public class GeneratorController {

    private final TableInfoReader tableInfoReader;
    private final CodeGenerator codeGenerator;
    private final GeneratorProperties generatorProperties;

    @GetMapping("/tables")
    @Operation(summary = "获取数据库表列表")
    public Result<List<TableInfo>> getTables() {
        List<TableInfo> tables = tableInfoReader.readTables();
        return Result.success(tables);
    }

    @GetMapping("/table/{tableName}")
    @Operation(summary = "获取表详情")
    public Result<TableInfo> getTable(
            @Parameter(description = "表名") @PathVariable String tableName) {
        TableInfo tableInfo = tableInfoReader.readTable(tableName);
        return Result.success(tableInfo);
    }

    @PostMapping("/generate")
    @Operation(summary = "生成代码", description = "根据表名生成后端和前端代码")
    public Result<Void> generate(
            @Parameter(description = "表名") @RequestParam String tableName,
            @Parameter(description = "是否生成Entity") @RequestParam(defaultValue = "true") Boolean generateEntity,
            @Parameter(description = "是否生成Mapper") @RequestParam(defaultValue = "true") Boolean generateMapper,
            @Parameter(description = "是否生成Service") @RequestParam(defaultValue = "true") Boolean generateService,
            @Parameter(description = "是否生成Controller") @RequestParam(defaultValue = "true") Boolean generateController,
            @Parameter(description = "是否生成Vue") @RequestParam(defaultValue = "true") Boolean generateVue) {

        generatorProperties.setGenerateEntity(generateEntity);
        generatorProperties.setGenerateMapper(generateMapper);
        generatorProperties.setGenerateService(generateService);
        generatorProperties.setGenerateController(generateController);
        generatorProperties.setGenerateVue(generateVue);

        TableInfo tableInfo = tableInfoReader.readTable(tableName);
        if (tableInfo == null) {
            return Result.fail("表不存在: " + tableName);
        }

        codeGenerator.generate(tableInfo);
        return Result.success();
    }
}
