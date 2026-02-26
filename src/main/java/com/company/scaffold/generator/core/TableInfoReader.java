package com.company.scaffold.generator.core;

import cn.hutool.core.util.StrUtil;
import com.company.scaffold.generator.config.GeneratorProperties;
import com.company.scaffold.generator.data.ColumnInfo;
import com.company.scaffold.generator.data.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableInfoReader {

    private final DataSource dataSource;
    private final GeneratorProperties properties;

    public List<TableInfo> readTables() {
        List<TableInfo> tableInfoList = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String schema = getSchema(conn);

            ResultSet rs = metaData.getTables(null, schema, "%", new String[]{"TABLE"});

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String tableComment = rs.getString("REMARKS");

                if (tableName.startsWith(properties.getTablePrefix())) {
                    TableInfo tableInfo = readTableInfo(metaData, schema, tableName, tableComment);
                    tableInfoList.add(tableInfo);
                }
            }
            rs.close();

        } catch (SQLException e) {
            log.error("读取数据库表信息失败", e);
        }

        return tableInfoList;
    }

    public TableInfo readTable(String tableName) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String schema = getSchema(conn);

            ResultSet rs = metaData.getTables(null, schema, tableName, new String[]{"TABLE"});
            String tableComment = "";
            if (rs.next()) {
                tableComment = rs.getString("REMARKS");
            }
            rs.close();

            return readTableInfo(metaData, schema, tableName, tableComment);

        } catch (SQLException e) {
            log.error("读取表信息失败: {}", tableName, e);
            return null;
        }
    }

    private TableInfo readTableInfo(DatabaseMetaData metaData, String schema, String tableName, String tableComment) throws SQLException {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setTableComment(StrUtil.blankToDefault(tableComment, tableName));
        tableInfo.setModuleName(properties.getModuleName());
        tableInfo.setPackageName(properties.getBasePackage() + ".module." + properties.getModuleName());
        tableInfo.setClassName(convertClassName(tableName));

        List<ColumnInfo> columns = new ArrayList<>();
        ColumnInfo primaryKey = null;

        ResultSet rs = metaData.getColumns(null, schema, tableName, "%");
        while (rs.next()) {
            ColumnInfo column = new ColumnInfo();
            column.setColumnName(rs.getString("COLUMN_NAME"));
            column.setColumnComment(rs.getString("REMARKS"));
            column.setAttrName(convertAttrName(column.getColumnName()));
            column.setAttrType(column.getJavaType());
            column.setJdbcType(rs.getString("TYPE_NAME"));
            column.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
            column.setDefaultValue(rs.getString("COLUMN_DEF"));
            column.setColumnSize(rs.getInt("COLUMN_SIZE"));
            column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));

            if ("YES".equals(rs.getString("PK"))) {
                column.setPrimaryKey(true);
                primaryKey = column;
            }

            if ("BigDecimal".equals(column.getAttrType())) {
                tableInfo.setHasBigDecimal(true);
            }
            if ("LocalDateTime".equals(column.getAttrType())) {
                tableInfo.setHasLocalDateTime(true);
            }

            columns.add(column);
        }
        rs.close();

        tableInfo.setColumns(columns);
        tableInfo.setPrimaryKey(primaryKey);

        return tableInfo;
    }

    private String getSchema(Connection conn) throws SQLException {
        String schema = null;
        DatabaseMetaData metaData = conn.getMetaData();
        if (metaData.getDatabaseProductName().equalsIgnoreCase("MySQL")) {
            schema = conn.getCatalog();
        }
        return schema;
    }

    private String convertClassName(String tableName) {
        if (tableName.startsWith(properties.getTablePrefix())) {
            tableName = tableName.substring(properties.getTablePrefix().length());
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : tableName.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }

    private String convertAttrName(String columnName) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : columnName.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }
}
