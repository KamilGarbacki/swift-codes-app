package com.example.swiftCodesApp.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.util.List;

public class CsvUtil {
    public static <T> List<T> loadCsvFile(String resourcePath, Class<T> clazz, List<String> columns) throws IOException {
        return loadCsvFile(resourcePath, clazz, columns, true);
    }

    public static <T> List<T> loadCsvFile(String resourcePath, Class<T> clazz, List<String> columns, boolean withHeader) throws IOException {
        String file = ResourceUtil.readFile(resourcePath);
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);

        CsvSchema schema = CsvSchema.builder()
                .addColumns(columns, CsvSchema.ColumnType.NUMBER_OR_STRING)
                .build();

        if (withHeader) {
            schema = schema.withHeader();
        }

        MappingIterator<T> it = csvMapper
                .readerFor(clazz)
                .with(schema)
                .readValues(file);

        return it.readAll();
    }
}
