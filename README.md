
# DynamicMongoAPI

## Project Overview

**DynamicMongoAPI** is a Spring Boot application that provides a dynamic and flexible interface for interacting with MongoDB. Users can easily run the application in a Docker container using a pre-built image, allowing for quick deployment and easy configuration via environment variables.

## Features

- **Dynamic Collection Handling**: Automatically connects to different MongoDB collections based on user input.
- **CRUD Operations**: Supports Create, Read, Update, and Delete operations for any specified collection.
- **Environment Configuration**: Simple configuration via environment variables for MongoDB connection strings.
- **Docker Support**: Easily deployable via a pre-built Docker image.

## Getting Started

### Prerequisites

- Docker installed on your machine.
- A running MongoDB instance.

### Running the Application

You can run the application using the pre-built Docker image available on Docker Hub. Use the following command, replacing `your_mongodb_connection_string` with your actual MongoDB URI:

```bash
docker run -e SPRING_DATA_MONGODB_URI="your_mongodb_connection_string" -p 8080:8080 nikunjsp19/dynamicmongoapi
```

- **Environment Variables**:
  - **SPRING_DATA_MONGODB_URI**: The connection string for your MongoDB database. This should include the database name and authentication details if needed.

### API Endpoints

The application exposes the following API endpoints for CRUD operations:

- **Find Documents**: 
  - `POST /{collectionName}`
  - Request Body: JSON object containing filters for searching documents.

- **Add Document**: 
  - `POST /{collectionName}/add`
  - Request Body: JSON object with fields to add.

- **Update Document**: 
  - `PUT /{collectionName}/update`
  - Request Body: JSON object with fields to update.

- **Delete Document**: 
  - `DELETE /{collectionName}/delete`
  - Request Body: JSON object specifying which documents to delete.

### Example Usage

To insert a new document into a collection named `GarageInventory`, you can use the following curl command:

```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "PART_NAME": "Brake Pads",
  "PART_NUMBER": "BP123",
  "QUANTITY": 25,
  "COST": 30.00,
  "SUPPLIER": "PartsRUs",
  "LAST_RESTOCKED": "2023-11-02"
}' http://localhost:8080/GarageInventory/add
```

To find documents in the `GarageInventory` collection, use:

```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "PART_NAME": "Brake Pads"
}' http://localhost:8080/GarageInventory
```

### Testing the API

You can test the API using Postman or curl commands as shown above. Ensure your MongoDB instance is running and accessible at the provided connection string.

