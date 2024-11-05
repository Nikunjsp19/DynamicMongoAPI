package com.np.DynamicMongoAPI.services;

import org.bson.Document;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


public interface DynamicMongoAPIService {

    ResponseEntity<List<Document>> find(String collectionName, Map<String, Object> filters);
    ResponseEntity<Document> add(String collectionName, Map<String, Object> fields);
    ResponseEntity<Document> update(String collectionName, Map<String, Object> fields);
    ResponseEntity<Long> delete(String collectionName, Map<String, Object> fields);
    ResponseEntity<List<Document>> aggregate(String collectionName, List<Document> aggregationPipeline);
}
