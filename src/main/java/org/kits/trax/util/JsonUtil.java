package org.kits.trax.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    public static String toJson(Object entity) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Entity " + entity);
        }

        String json = null;
        if (entity instanceof Map) {
            json = new JSONSerializer().exclude("*.class").include("values.values")
                    .deepSerialize(entity);
        } else {
            json = new JSONSerializer().exclude("*.class").deepSerialize(entity);
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("JSON " + json);
        }

        return json;
    }

    public static <T> String toJsonArray(Collection<T> entities) {

        String json = new JSONSerializer().exclude("*.class").include("values")
                .deepSerialize(entities);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("JSON Array " + json);
        }

        return json;
    }

    public static <T> T fromJson(Class<T> aClass, String json) {

        T entity = new JSONDeserializer<T>().use(null, aClass).deserialize(json);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Entity " + entity);
        }

        return entity;
    }

    public static <T> List<T> fromJsonArray(Class<T> aClass, String json) {

    	LOGGER.info("JSON " + json);
        List<T> entities = new JSONDeserializer<List<T>>().use("values", aClass).deserialize(json,
                List.class);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Entity List " + entities);
        }

        return entities;
    }
}
