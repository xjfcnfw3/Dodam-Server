package com.example.dodam.utils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.util.ObjectUtils;

public class QueryGenerator<T> {

    private static final String UPDATE = "UPDATE ";
    private static final String SET = " SET ";

    private ObjectFields objectFields;

    public QueryGenerator(T data) {
        this.objectFields = getObjectFieldsByDataClass(data);
    }

    public void setObjectFields(T data) {
        this.objectFields = getObjectFieldsByDataClass(data);
    }

    public String generateDynamicUpdateQuery(String table, String condition) {
        return new StringBuilder(UPDATE).append(table)
            .append(SET)
            .append(generatedJdbcTemplateUpdateQuerySetFields(objectFields.getFieldNames()))
            .append(condition).toString();
    }

    public Map<String,Object> generateParams() {
        return this.objectFields.getObjectFields();
    }

    private ObjectFields getObjectFieldsByDataClass(T data) {
        try {
            return retrieveFields(data);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("insert wrong value");
        }
    }

    private ObjectFields retrieveFields(T data) throws IllegalAccessException {
        Map<String, Object> fields = new LinkedHashMap<>();
        for(Field field : data.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if(!ObjectUtils.isEmpty(field.get(data))){
                fields.put(field.getName(), field.get(data));
            }
        }
        return new ObjectFields(fields);
    }

    private StringBuilder generatedJdbcTemplateUpdateQuerySetFields(Set<String> fields) {
        StringBuilder fieldNames = new StringBuilder();
        for (String key : fields) {
            fieldNames.append(key).append("=:").append(key).append(", ");
        }
        return fieldNames.deleteCharAt(fieldNames.length() - 2);
    }

    static class ObjectFields {
        private final Map<String, Object> objectFields;

        public ObjectFields(Map<String, Object> objectFields) {
            this.objectFields = objectFields;
        }

        public Map<String, Object> getObjectFields() {
            return objectFields;
        }

        public Set<String> getFieldNames() {
            return objectFields.keySet();
        }
    }
}
