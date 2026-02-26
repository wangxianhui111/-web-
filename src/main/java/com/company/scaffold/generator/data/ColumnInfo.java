package com.company.scaffold.generator.data;

import lombok.Data;

@Data
public class ColumnInfo {

    private String columnName;

    private String columnComment;

    private String attrName;

    private String attrType;

    private String jdbcType;

    private boolean primaryKey;

    private boolean nullable;

    private String defaultValue;

    private Integer columnSize;

    private Integer decimalDigits;

    public String getCapitalName() {
        if (attrName == null || attrName.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(attrName.charAt(0)) + attrName.substring(1);
    }

    public String getJavaType() {
        if (attrType == null) {
            return "String";
        }
        return switch (attrType.toLowerCase()) {
            case "bigdecimal" -> "BigDecimal";
            case "tinyint", "smallint", "int", "integer" -> "Integer";
            case "bigint" -> "Long";
            case "float", "double" -> "Double";
            case "decimal" -> "BigDecimal";
            case "date", "datetime", "timestamp", "time" -> "LocalDateTime";
            case "varchar", "char", "text", "longtext", "mediumtext", "tinytext" -> "String";
            case "boolean", "bit" -> "Boolean";
            case "blob", "binary", "varbinary" -> "byte[]";
            default -> "String";
        };
    }
}
