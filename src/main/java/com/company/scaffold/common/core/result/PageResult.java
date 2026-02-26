package com.company.scaffold.common.core.result;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

@Schema(description = "分页结果")
public record PageResult<T>(
        @Schema(description = "数据列表")
        List<T> records,
        @Schema(description = "总记录数")
        long total,
        @Schema(description = "当前页码")
        long current,
        @Schema(description = "每页大小")
        long size
) implements Serializable {

    public static <T> PageResult<T> of(List<T> records, long total, long current, long size) {
        return new PageResult<>(records, total, current, size);
    }

    public static <T> PageResult<T> empty(long current, long size) {
        return new PageResult<>(List.of(), 0, current, size);
    }
}
