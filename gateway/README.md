Spring Cloud Gateway demo
================================================================================

Spring Cloud Gateway demo


Requirements
--------------------------------------------------------------------------------

- Java 21
- Docker


How to start for development
--------------------------------------------------------------------------------

Start dependency servers

```bash
docker compose up
```

Start API server module

```bash
./mvnw clean spring-boot:run \
-pl api \
-Dspring-boot.run.jvmArguments="
-Dspring.cloud.gateway.routes[0].id=
-Dspring.cloud.gateway.routes[0].uri=
-Dspring.cloud.gateway.routes[0].predicates[0]=
-Dspring.cloud.gateway.routes[0].filters[0]=

-Dspring.datasource.username=postgres
-Dspring.datasource.password=postgres
-Dspring.jpa.defer-datasource-initialization=true
-Dspring.jpa.show-sql=false
-Dspring.jpa.hibernate.ddl-auto=create
-Dspring.sql.init.mode=always
-Dspring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8000/realms/master
-Dapp.security.idp=keycloak
-Dapp.security.cors.allowed-origins=http://localhost:3000
"
```

Start frontend module

```bash
(cd ui; \
npm install;
NEXT_PUBLIC_OIDC_AUTHORITY=http://localhost:8000/realms/master \
NEXT_PUBLIC_OIDC_CLIENT_ID=spring-nextjs \
NEXT_PUBLIC_OIDC_REDIRECT_URI=http://localhost:3000 \
NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI=http://localhost:3000 \
npm run build && npm run serve)
```

Start frontend module as hot-reload

```bash
(cd ui; \
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
