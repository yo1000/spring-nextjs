Spring Next.js
================================================================================

Spring x Next.js demo


Requirements
--------------------------------------------------------------------------------

- Java 21
- Docker


How to build
--------------------------------------------------------------------------------

Build server module for release

```bash
APP_VERSION=1.0.0

./mvnw -Dapp.version="${APP_VERSION}" clean package
```

Build server module for development

```bash
./mvnw clean package
```


How to run
--------------------------------------------------------------------------------

Run dependency servers

```bash
docker compose up
```

Run API server module for development

```bash
./mvnw clean spring-boot:run \
-pl spring-nextjs-server \
-Dspring-boot.run.jvmArguments="
-Dspring.datasource.url=jdbc:postgresql://localhost:5432/spring_nextjs
-Dspring.datasource.username=postgres
-Dspring.datasource.password=postgres
-Dspring.jpa.defer-datasource-initialization=true
-Dspring.jpa.show-sql=false
-Dspring.jpa.hibernate.ddl-auto=create
-Dspring.sql.init.mode=always
-Dspring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8000/realms/master
-Dapp.security.cors.allowed-origins=http://localhost:3000
"
```

Run frontend module for development

```bash
(cd spring-nextjs-server/src/main/resources/frontend/; \
npm install;
NEXT_PUBLIC_OIDC_AUTHORITY=http://localhost:8000/realms/master \
NEXT_PUBLIC_OIDC_CLIENT_ID=spring-nextjs \
NEXT_PUBLIC_OIDC_REDIRECT_URI=http://localhost:3000 \
NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI=http://localhost:3000 \
npm run dev)
```


How to use main APIs
--------------------------------------------------------------------------------

List items

```bash
curl -XGET \
-H'Content-Type: application/json' \
localhost:8080/items?page=0
```

List item inventories

```bash
curl -XGET \
-H'Content-Type: application/json' \
localhost:8080/itemInventories?page=0
```

Create item inventory

```bash
curl -XPOST \
-H'Content-Type: application/json' \
-d '{
    "id": 1,
    "item": {
        "id": 1
    },
    "quantity": 10
}' \
localhost:8080/itemInventories
```

Update item inventory differentially

```bash
curl -XPATCH \
-H'Content-Type: application/merge-patch+json' \
-d '{
    "quantity": 20
}' \
localhost:8080/itemInventories/1
```
