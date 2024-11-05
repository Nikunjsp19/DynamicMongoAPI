package com.np.DynamicMongoAPI.services;

import com.np.DynamicMongoAPI.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Slf4j
public class DynamicMongoAPIServiceImpl implements DynamicMongoAPIService{
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseEntity<List<Document>> find(String collectionName, Map<String, Object> filters) {

        Query query = new Query();
        filters.forEach((key, value) -> {
            if (value != null) {
                if(value instanceof List<?>) {
                    query.addCriteria(Criteria.where(StringUtils.camelCaseToMongo(key)).in((List<?>)value));
                } else{
                    query.addCriteria(Criteria.where(StringUtils.camelCaseToMongo(key)).is(value));
                }
            }
        });

        log.info("Search request for Collection:{} with Query:{}", StringUtils.camelCaseToMongo(collectionName), query);

        return ResponseEntity.ok(mongoTemplate.find(query, Document.class,StringUtils.camelCaseToMongo(collectionName)));
    }


    @Override
    public ResponseEntity<Document> add(String collectionName, Map<String, Object> fields) {
        Map<String, Object> transformedFields = new HashMap<>();

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String transformedKey = StringUtils.camelCaseToMongo(entry.getKey());
            transformedFields.put(transformedKey, entry.getValue());
        }

        Document document = new Document(transformedFields);
        mongoTemplate.insert(document, StringUtils.camelCaseToMongo(collectionName));
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }


    @Override
    public ResponseEntity<Document> update(String collectionName, Map<String, Object> fields) {

        String id = fields.get("id") != null ? (String) fields.remove("id") : (String) fields.remove("ID");
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Map<String, Object> transformedFields = new HashMap<>();

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String transformedKey = StringUtils.camelCaseToMongo(entry.getKey());
            transformedFields.put(transformedKey, entry.getValue());
        }

        Query query = new Query(Criteria.where("ID").is(id));
        Update update = new Update();
        transformedFields.forEach(update::set);

        Document updatedDocument = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Document.class, StringUtils.camelCaseToMongo(collectionName));
        if (updatedDocument == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedDocument);
    }

    @Override
    public ResponseEntity<Long> delete(String collectionName, Map<String, Object> filters) {

        Query query = new Query();
        filters.forEach((key, value) ->
                query.addCriteria(Criteria.where(StringUtils.camelCaseToMongo(key)).is(value))
        );

        // Return the number of documents deleted
        return ResponseEntity.ok(mongoTemplate.remove(query, StringUtils.camelCaseToMongo(collectionName)).getDeletedCount());
    }

}
