package com.company.scaffold.generator.data;

import lombok.Data;

import java.util.List;

@Data
public class TableInfo {

    private String tableName;

    private String tableComment;

    private String className;

    private String moduleName;

    private String packageName;

    private List<ColumnInfo> columns;

    private ColumnInfo primaryKey;

    private boolean hasBigDecimal;
    private boolean hasDate;
    private boolean hasLocalDateTime;

    public String getEntityName() {
        return className;
    }

    public String getFirstLowerClassName() {
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }

    public String getMappingPath() {
        return "/" + moduleName + "/" + getFirstLowerClassName();
    }
}
