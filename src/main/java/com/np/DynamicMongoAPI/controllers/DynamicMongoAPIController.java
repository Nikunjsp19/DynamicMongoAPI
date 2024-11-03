package com.np.DynamicMongoAPI.controllers;

import com.np.DynamicMongoAPI.services.DynamicMongoAPIService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DynamicMongoAPIController {

    @Autowired
    DynamicMongoAPIService service;

    @PostMapping(value = "/{collectionName}")
    public ResponseEntity<List<Document>> find(@PathVariable String collectionName, @RequestBody  Map<String, Object> filters) {
       return service.find(collectionName, filters);
    }


    @PostMapping(value = "/{collectionName}/add")
    public ResponseEntity<Document> add(@PathVariable String collectionName, @RequestBody  Map<String, Object> fields) {
        return service.add(collectionName, fields);
    }

    @PutMapping(value = "/{collectionName}/update")
    public ResponseEntity<Document> update(@PathVariable String collectionName, @RequestBody  Map<String, Object> fields) {
        return service.update(collectionName, fields);
    }

    @DeleteMapping(value = "/{collectionName}/delete")
    public ResponseEntity<Long> delete(@PathVariable String collectionName, @RequestBody  Map<String, Object> fields) {
        return service.delete(collectionName, fields);
    }
}