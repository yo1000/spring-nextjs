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

Build frontend module for development

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
