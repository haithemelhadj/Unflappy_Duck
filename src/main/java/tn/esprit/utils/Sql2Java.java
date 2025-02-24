package tn.esprit.utils;

import java.util.Map;
import java.util.HashMap;

public class Sql2Java {


    private static final Map<String, String> TYPE_MAPPING = new HashMap<>();

    // Initialize the type mapping once
    static {
        // Numeric types
        TYPE_MAPPING.put("bit", "byte[]");
        TYPE_MAPPING.put("tinyint", "Integer");  // Or Boolean for TINYINT(1)
        TYPE_MAPPING.put("smallint", "Integer");
        TYPE_MAPPING.put("mediumint", "Integer");
        TYPE_MAPPING.put("int", "Integer");
        TYPE_MAPPING.put("integer", "Integer");
        TYPE_MAPPING.put("bigint", "Long");
        TYPE_MAPPING.put("float", "Float");
        TYPE_MAPPING.put("double", "Double");
        TYPE_MAPPING.put("decimal", "BigDecimal");
        TYPE_MAPPING.put("numeric", "BigDecimal");

        // Date/time types
        TYPE_MAPPING.put("date", "LocalDate");
        TYPE_MAPPING.put("datetime", "LocalDateTime");
        TYPE_MAPPING.put("timestamp", "LocalDateTime");
        TYPE_MAPPING.put("time", "LocalTime");
        TYPE_MAPPING.put("year", "Year");

        // String types
        TYPE_MAPPING.put("char", "String");
        TYPE_MAPPING.put("varchar", "String");
        TYPE_MAPPING.put("binary", "byte[]");
        TYPE_MAPPING.put("varbinary", "byte[]");
        TYPE_MAPPING.put("tinytext", "String");
        TYPE_MAPPING.put("text", "String");
        TYPE_MAPPING.put("mediumtext", "String");
        TYPE_MAPPING.put("longtext", "String");
        TYPE_MAPPING.put("enum", "String");
        TYPE_MAPPING.put("set", "String");

        // JSON
        TYPE_MAPPING.put("json", "String");  // Or specific JSON type if using library

        // Spatial types
        TYPE_MAPPING.put("geometry", "Object");
        TYPE_MAPPING.put("point", "Object");
        TYPE_MAPPING.put("linestring", "Object");
        TYPE_MAPPING.put("polygon", "Object");

        // BLOB types
        TYPE_MAPPING.put("tinyblob", "byte[]");
        TYPE_MAPPING.put("blob", "byte[]");
        TYPE_MAPPING.put("mediumblob", "byte[]");
        TYPE_MAPPING.put("longblob", "byte[]");
    }

    public static String mapToJavaType(String sqlType) {
        String lowerType = sqlType.toLowerCase();

        // Handle unsigned variants (common in MariaDB)
        if (lowerType.startsWith("int") && lowerType.contains("unsigned")) {
            return "Long";
        }

        // Handle TINYINT(1) as boolean
        if ("tinyint".equals(lowerType)) {
            // You might need additional logic here to check column size
            return "Boolean";  // Or keep as Integer if unsure
        }

        return TYPE_MAPPING.getOrDefault(lowerType, "Object");
    }
}
