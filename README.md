Spring Next.js
================================================================================

Spring x Next.js demo


Requirements
--------------------------------------------------------------------------------

- Java 21
- Node.js 22
- Docker


How to build
--------------------------------------------------------------------------------

Build server modules as release version

```bash
APP_VERSION=1.0.0

./mvnw -Dapp.version="${APP_VERSION}" clean package
```

Build server modules as development version

```bash
./mvnw clean package
```

Build frontend module

```bash
(cd ui; \
npm install;
NEXT_PUBLIC_OIDC_AUTHORITY=${OIDC_ISSUER_URI} \
NEXT_PUBLIC_OIDC_CLIENT_ID=${OIDC_CLIENT_ID} \
NEXT_PUBLIC_OIDC_REDIRECT_URI=${OIDC_POST_SIGNIN_REDIRECT_URI} \
NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI=${OIDC_POST_SIGNOUT_URI} \
npm run build)
```


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
-Dspring-boot.run.arguments="
  --spring.datasource.url=jdbc:postgresql://localhost:5432/spring_nextjs
  --spring.datasource.username=postgres
  --spring.datasource.password=postgres
  --spring.jpa.defer-datasource-initialization=true
  --spring.jpa.show-sql=false
  --spring.jpa.hibernate.ddl-auto=create
  --spring.sql.init.mode=always
  --spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8000/realms/master
  --app.security.idp=keycloak
  --app.security.cors.allowed-origins=http://localhost:3000
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

Start gateway server module

```bash
./mvnw clean spring-boot:run \
-pl gateway \
-Dspring-boot.run.arguments="
  --spring.application.name=spring-nextjs-gateway 
  --spring.security.oauth2.client.registration.relying-party.provider=identity-provider 
  --spring.security.oauth2.client.registration.relying-party.client-id=spring-gateway 
  --spring.security.oauth2.client.registration.relying-party.client-secret=ovJAxSmla3LXFiLfwi81H2IPDXKZjsCX 
  --spring.security.oauth2.client.registration.relying-party.authorization-grant-type=client_credentials 
  --spring.security.oauth2.client.registration.relying-party.scope=openid,profile,email 
  --spring.security.oauth2.client.provider.identity-provider.issuer-uri=http://localhost:8000/realms/master 
  --spring.cloud.gateway.routes[0].id=api_route 
  --spring.cloud.gateway.routes[0].uri=http://localhost:8080 
  --spring.cloud.gateway.routes[0].predicates[0]=Path=/api/** 
  --spring.cloud.gateway.routes[0].filters[0]=RewritePath=/api/(?<segment>.*),/\${segment}
"
```


How to use main APIs
--------------------------------------------------------------------------------

List items (Public API)

```bash
curl -XGET \
-H"Content-Type: application/json" \
localhost:8080/items?page=0
```

List item inventories

```bash
curl -XGET \
-H"Content-Type: application/json" \
-H"Authorization: Bearer ${ACCESS_TOKEN}" \
localhost:8080/itemInventories?page=0
```

Create item inventory

```bash
curl -XPOST \
-H"Content-Type: application/json" \
-H"Authorization: Bearer ${ACCESS_TOKEN}" \
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
-H"Content-Type: application/merge-patch+json" \
-H"Authorization: Bearer ${ACCESS_TOKEN}" \
-d '{
    "quantity": 20
}' \
localhost:8080/itemInventories/1
```


How to use the main API (via Gateway)
--------------------------------------------------------------------------------

List items (Public API)

```bash
curl -XGET \
-H"Content-Type: application/json" \
localhost:8090/api/items?page=0
```

List item inventories

```bash
curl -XGET \
-H"Content-Type: application/json" \
localhost:8090/api/itemInventories?page=0
```

Create item inventory

```bash
curl -XPOST \
-H"Content-Type: application/json" \
-d '{
    "id": 1,
    "item": {
        "id": 1
    },
    "quantity": 10
}' \
localhost:8090/api/itemInventories
```

Update item inventory differentially

```bash
curl -XPATCH \
-H"Content-Type: application/merge-patch+json" \
-d '{
    "quantity": 20
}' \
localhost:8090/api/itemInventories/1
```
