package com.np.DynamicMongoAPI.services;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                    query.addCriteria(Criteria.where(key).in((List<?>)value));
                } else{
                    query.addCriteria(Criteria.where(key).is(value));
                }
            }
        });

        log.info("Search request for Collection:{} with Query:{}", collectionName, query);

        return ResponseEntity.ok(mongoTemplate.find(query, Document.class,collectionName));
    }


    @Override
    public ResponseEntity<Document> add(String collectionName, Map<String, Object> fields) {

        Document document = new Document(fields);
        mongoTemplate.insert(document, collectionName);
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }


    @Override
    public ResponseEntity<Document> update(String collectionName, Map<String, Object> fields) {

        String id = fields.get("id") != null ? (String) fields.remove("id") : (String) fields.remove("ID");
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Query query = new Query(Criteria.where("ID").is(id));
        Update update = new Update();
        fields.forEach(update::set);

        Document updatedDocument = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Document.class, collectionName);
        if (updatedDocument == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedDocument);
    }

    @Override
    public ResponseEntity<Long> delete(String collectionName, Map<String, Object> filters) {

        Query query = new Query();
        filters.forEach((key, value) ->
                query.addCriteria(Criteria.where(key).is(value))
        );

        // Return the number of documents deleted
        return ResponseEntity.ok(mongoTemplate.remove(query, collectionName).getDeletedCount());
    }

    @Override
    public ResponseEntity<List<Document>> aggregate(String collectionName, List<Document> aggregationPipeline) {

        return ResponseEntity.ok(mongoTemplate.getCollection(collectionName)
                .aggregate(aggregationPipeline)
                .into(new ArrayList<>()));

    }

}
