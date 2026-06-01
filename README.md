Spring Next.js
================================================================================

Spring Boot x Next.js demo


Requirements
--------------------------------------------------------------------------------

- Java 21
- Node.js 22
- Docker


Technology Stacks
--------------------------------------------------------------------------------

- API server: Java, Spring Boot, Spring Security, Spring Data JPA
- Gateway server: Java, Spring Boot, Spring Security, Spring Cloud Gateway
- Frontend: TypeScript, Next.js, oidc-client-ts, Tailwind CSS


How to Run
----------------------------------------------------------------------------------------------------

```bash
docker compose down && docker compose up --build  
```


How to Run for Dev
----------------------------------------------------------------------------------------------------

### Preparation

```bash
docker compose up
```

### API (Java - Spring Boot)

```bash
./mvnw clean spring-boot:run \
-pl api \
-Dspring-boot.run.arguments="
  --spring.datasource.url=jdbc:postgresql://localhost:5432/postgres?currentSchema=eight
  --spring.datasource.username=postgres
  --spring.datasource.password=postgres
  --spring.jpa.defer-datasource-initialization=true
  --spring.jpa.show-sql=false
  --spring.jpa.hibernate.ddl-auto=create
  --spring.jpa.properties.hibernate.default_schema=eight
  --spring.sql.init.mode=always
  --spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8000/realms/master
  --app.security.idp=keycloak
  --app.security.cors.allowed-origins=http://localhost:3000
"
```

### Gateway (Java - Spring Boot)

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

### UI (TypeScript - Next.js, Auth.js)

```bash
(cd ui_authjs; \
export DATABASE_URL="postgresql://postgres:postgres@localhost:5432/postgres?schema=authjs" \
export KEYCLOAK_ISSUER="http://localhost:8000/realms/master" \
export KEYCLOAK_CLIENT_ID="spring-nextjs" \
export KEYCLOAK_CLIENT_SECRET="Secret-1234567890-Secret" \
export NEXTAUTH_URL="http://localhost:3000" \
export NEXTAUTH_SECRET="replace-with-a-strong-random-string" \
export AUTH_TRUST_HOST=true \
export NEXT_PUBLIC_API_BASE_URI="http://localhost:8080" \
npm install; \
npm exec prisma migrate dev -- --name init; \
npm run dev)
```

### UI (TypeScript - Next.js, oidc-client-ts)

```bash
(cd ui_oidc-client-ts; \
npm install;
NEXT_PUBLIC_API_BASE_URI=http://localhost:8080 \
NEXT_PUBLIC_OIDC_AUTHORITY=http://localhost:8000/realms/master \
NEXT_PUBLIC_OIDC_CLIENT_ID=spring-nextjs \
NEXT_PUBLIC_OIDC_REDIRECT_URI=http://localhost:3000 \
NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI=http://localhost:3000 \
npm run dev)
```


How to test
--------------------------------------------------------------------------------

Test backend (Java) modules

```bash
./mvnw clean test
```

Test frontend (Node.js) module

```bash
(cd ui;
npm install;
npm run test)
```

Test frontend (Node.js) module with `watch` option

```bash
(cd ui;
npm install;
npm run test-watch)
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
