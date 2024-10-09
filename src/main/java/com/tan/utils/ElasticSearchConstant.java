package com.tan.utils;

public class ElasticSearchConstant {

    public static final String MAPPING_TEMP = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"fileId\": {\"type\": \"integer\"},\n" +
            "      \"fileName\": {\"type\": \"text\"},\n" +
            "      \"fileType\": {\"type\": \"keyword\"},\n" +
            "      \"userId\": {\"type\": \"integer\"},\n" +
            "      \"fileSize\": {\"type\": \"keyword\"},\n" +
            "      \"updateTime\": {\"type\": \"date\", \"format\": \"yyyy-MM-dd HH:mm:ss\"},\n" +
            "      \"filePath\": {\"type\": \"text\"},\n" +
            "      \"spaceId\": {\"type\": \"integer\"},\n" +
            "      \"isDelete\": {\"type\": \"integer\"}\n" +
            "    }\n" +
            "  }\n" +
            "}";

}
