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

    public static final String AUTO_COMPLETE = "{\n" +
            "  \"settings\": {\n" +
            "    \"analysis\": {\n" +
            "      \"analyzer\": {\n" +
            "        \"text_anlyzer\": {\n" +
            "          \"tokenizer\": \"ik_max_word\",\n" +
            "          \"filter\": \"py\"\n" +
            "        },\n" +
            "        \"completion_analyzer\": {\n" +
            "          \"tokenizer\": \"ik_max_word\",\n" +
            "          \"filter\": \"py\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"filter\": {\n" +
            "        \"py\": {\n" +
            "          \"type\": \"pinyin\",\n" +
            "          \"keep_full_pinyin\": false,\n" +
            "          \"keep_joined_full_pinyin\": true,\n" +
            "          \"keep_original\": true,\n" +
            "          \"limit_first_letter_length\": 16,\n" +
            "          \"remove_duplicated_term\": true,\n" +
            "          \"none_chinese_pinyin_tokenize\": false\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"fileId\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"fileName\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"text_anlyzer\",\n" +
            "        \"search_analyzer\": \"ik_smart\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"fileType\":{\n" +
            "        \"type\": \"text\",\n" +
            "         \"analyzer\": \"text_anlyzer\",\n" +
            "        \"search_analyzer\": \"ik_smart\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"fileSize\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"updateTime\":{\n" +
            "        \"type\": \"date\",\n" +
            "        \"format\": \"yyyy-MM-dd HH:mm:ss\"\n" +
            "      },\n" +
            "      \"filePath\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"spaceId\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"isDelete\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"text_anlyzer\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"suggestion\":{\n" +
            "          \"type\": \"completion\",\n" +
            "          \"analyzer\": \"completion_analyzer\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

}
