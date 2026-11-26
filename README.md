# Blog Post REST API with Nested Comments
Java 17+, Spring Boot, Spring Data JPA, H2, Maven.

## Run
Open the folder containing pom.xml in IntelliJ IDEA, load Maven dependencies and run BlogApplication, or run `mvn spring-boot:run` in the terminal. H2 creates its database automatically; no MySQL setup required.

## Endpoints
- POST /api/posts — create post with nested comments (201)
- GET /api/posts — list posts (200)
- GET /api/posts/{id} — read post (200)
- PUT /api/posts/{id} — update title and content (200)
- DELETE /api/posts/{id} — delete post and comments (204)
- POST /api/posts/{id}/comments — add comment (201)
- DELETE /api/posts/{id}/comments/{cid} — remove comment (204)

Example POST body:
```json
{"title":"My blog","content":"Hello world","comments":[{"author":"Alex","text":"Great post"}]}
```
PUT requires title and content. Omit comments to preserve them; provide an array to replace them. An empty array removes all comments. Invalid fields return 400; missing resources return 404. CascadeType.ALL persists and deletes nested comments; orphanRemoval removes detached comments. JsonIgnore prevents recursive serialization.

## Testing
Import Blog_API.postman_collection.json into Postman and run requests in order using Collection Runner. IDs are captured automatically. Expected HTTP status assertions are included. Tests require the application to be running locally.
