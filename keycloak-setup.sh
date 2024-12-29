#!/bin/bash

KEEP_ENV=$(cat /tmp/.env | grep -v 'READY=')
echo "${KEEP_ENV}" > /tmp/.env

KEYCLOAK_URL_BASE=${KEYCLOAK_URL_BASE:-"http://localhost:8080"}
KEYCLOAK_REALM=${KEYCLOAK_REALM:-"master"}
KEYCLOAK_CLIENT_ID_API=${KEYCLOAK_CLIENT_ID_API:-"spring-nextjs"}
KEYCLOAK_CLIENT_ID_GATEWAY=${KEYCLOAK_CLIENT_ID_GATEWAY:-"spring-gateway"}
KEYCLOAK_CLIENT_SECRET_GATEWAY=${KEYCLOAK_CLIENT_SECRET_GATEWAY:-"ovJAxSmla3LXFiLfwi81H2IPDXKZjsCX"}
KEYCLOAK_ADMIN=${KEYCLOAK_ADMIN:-"admin"}
KEYCLOAK_ADMIN_PASSWORD=${KEYCLOAK_ADMIN_PASSWORD:-"admin"}

RETRY_SEC=1
RETRY_SEC_MAX=300
while [[ -z "$(curl -sI ${KEYCLOAK_URL_BASE}/health | head -n1 | grep 200)" ]]; do
  if [[ $RETRY_SEC -gt $RETRY_SEC_MAX ]]; then
    echo "Timeout: ${RETRY_SEC} > ${RETRY_SEC_MAX}"
    exit 1
  fi

  echo "Sleep: ${RETRY_SEC}"
  sleep $RETRY_SEC
  RETRY_SEC=$(expr $RETRY_SEC + $RETRY_SEC)
done


# Re-Authenticate
KC_ACCESS_TOKEN=$(
curl -s \
  -d "client_id=admin-cli" \
  -d "username=${KEYCLOAK_ADMIN}" \
  -d "password=${KEYCLOAK_ADMIN_PASSWORD}" \
  -d "grant_type=password" \
  "${KEYCLOAK_URL_BASE}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token" \
  | jq -r ".access_token" \
)

# Create client for gateway client
curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
    \"clientId\": \"${KEYCLOAK_CLIENT_ID_GATEWAY}\",
    \"secret\": \"${KEYCLOAK_CLIENT_SECRET_GATEWAY}\",
    \"clientAuthenticatorType\": \"client-secret\",
    \"serviceAccountsEnabled\": true,
    \"standardFlowEnabled\": true,
    \"directAccessGrantsEnabled\": true,
    \"implicitFlowEnabled\": false,
    \"publicClient\": false,
    \"redirectUris\": [\"*\"],
    \"webOrigins\": [\"*\"]
  }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients" \

KC_CLIENT_RESP=$(
curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients?clientId=${KEYCLOAK_CLIENT_ID_GATEWAY}" \
)

KC_CLIENT_ID=$(
echo "${KC_CLIENT_RESP}" \
  | jq -r '.[0].id' \
)

KC_USER_ID=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/service-account-user" \
  | jq -r '.id' \
)

KC_ROLE_JSON=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/users/${KC_USER_ID}/role-mappings/realm/available?first=0&max=21" \
  | jq '.[] | select(.name | test("admin"))' \
)

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/users/${KC_USER_ID}/role-mappings/realm" \
  -d "[${KC_ROLE_JSON}]" \


# Re-Authenticate
KC_ACCESS_TOKEN=$(
curl -s \
  -d "client_id=admin-cli" \
  -d "username=${KEYCLOAK_ADMIN}" \
  -d "password=${KEYCLOAK_ADMIN_PASSWORD}" \
  -d "grant_type=password" \
  "${KEYCLOAK_URL_BASE}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token" \
  | jq -r ".access_token" \
)

# Create client and get client-secret ** but UI is not required secret because by "public" client.
curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
    \"clientId\": \"${KEYCLOAK_CLIENT_ID_API}\",
    \"implicitFlowEnabled\": true,
    \"publicClient\": true,
    \"redirectUris\": [\"*\"],
    \"webOrigins\": [\"*\"]
  }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients" \

KC_CLIENT_RESP=$(
curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients?clientId=${KEYCLOAK_CLIENT_ID_API}" \
)

KC_CLIENT_ID=$(
echo "${KC_CLIENT_RESP}" \
  | jq -r '.[0].id' \
)

KC_CLIENT_SECRET=$(
echo "${KC_CLIENT_RESP}" \
  | jq -r '.[0].secret' \
)

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"item:read\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"item:write\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"itemInventory:read\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"itemInventory:write\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"weapon:read\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"weapon:write\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"weaponRemodel:read\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"weaponRemodel:write\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"userProfile:read\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
      \"name\": \"userProfile:write\",
      \"description\": \"\",
      \"attributes\": {}
    }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/clients/${KC_CLIENT_ID}/roles" \

KC_CLIENT_ROLE_ID=$(
echo "${KC_CLIENT_RESP}" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_ITEM_READ=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/item:read" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_ITEM_WRITE=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/item:write" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_ITEMINVENTORY_READ=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/itemInventory:read" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_ITEMINVENTORY_WRITE=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/itemInventory:write" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_WEAPON_READ=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/weapon:read" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_WEAPON_WRITE=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/weapon:write" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_WEAPONREMODEL_READ=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/weaponRemodel:read" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_WEAPONREMODEL_WRITE=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/weaponRemodel:write" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_USERPROFILE_READ=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/userProfile:read" \
  | jq -r '.id' \
)

KC_CLIENT_ROLE_ID_USERPROFILE_WRITE=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/master/clients/${KC_CLIENT_ID}/roles/userProfile:write" \
  | jq -r '.id' \
)


# Re-Authenticate
KC_ACCESS_TOKEN=$(
curl -s \
  -d "client_id=admin-cli" \
  -d "username=${KEYCLOAK_ADMIN}" \
  -d "password=${KEYCLOAK_ADMIN_PASSWORD}" \
  -d "grant_type=password" \
  "${KEYCLOAK_URL_BASE}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token" \
  | jq -r ".access_token" \
)

# Create user - squall
curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
    \"username\": \"squall\",
    \"email\": \"squall@localhost\",
    \"emailVerified\": true,
    \"firstName\": \"Squall\",
    \"lastName\": \"Leonhart\",
    \"enabled\": true,
    \"credentials\": [{
      \"type\": \"password\",
      \"temporary\": false,
      \"value\": \"Squall-1234\"
    }]
  }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users" \

KC_USER_ID=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users?exact=true&username=squall" \
  | jq -r '.[0].id' \
)

#KC_ADMIN_ROLE_JSON=$(curl -s \
#  -X GET \
#  -H "Content-Type: application/json" \
#  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
#  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/realm/available?first=0&max=11" \
#  | jq -r '.[] | select(.name == "admin")' \
#)

#curl -s \
#  -X POST \
#  -H "Content-Type: application/json" \
#  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
#  -d "[${KC_ADMIN_ROLE_JSON}]" \
#  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/realm" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_ITEM_WRITE}\",
    \"name\":\"item:write\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_ITEMINVENTORY_WRITE}\",
    \"name\":\"itemInventory:write\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_WEAPON_WRITE}\",
    \"name\":\"weapon:write\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_WEAPONREMODEL_WRITE}\",
    \"name\":\"weaponRemodel:write\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_USERPROFILE_WRITE}\",
    \"name\":\"userProfile:write\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

# Re-Authenticate
KC_ACCESS_TOKEN=$(
curl -s \
  -d "client_id=admin-cli" \
  -d "username=${KEYCLOAK_ADMIN}" \
  -d "password=${KEYCLOAK_ADMIN_PASSWORD}" \
  -d "grant_type=password" \
  "${KEYCLOAK_URL_BASE}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token" \
  | jq -r ".access_token" \
)

# Create user - zell
curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
    \"username\": \"zell\",
    \"email\": \"zell@localhost\",
    \"emailVerified\": true,
    \"firstName\": \"Zell\",
    \"lastName\": \"Dincht\",
    \"enabled\": true,
    \"credentials\": [{
      \"type\": \"password\",
      \"temporary\": false,
      \"value\": \"Zell-1234\"
    }]
  }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users" \

KC_USER_ID=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users?exact=true&username=zell" \
  | jq -r '.[0].id' \
)

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_ITEM_READ}\",
    \"name\":\"item:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_ITEMINVENTORY_READ}\",
    \"name\":\"itemInventory:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_WEAPON_READ}\",
    \"name\":\"weapon:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_WEAPONREMODEL_READ}\",
    \"name\":\"weaponRemodel:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_USERPROFILE_READ}\",
    \"name\":\"userProfile:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

# Create user - irvine
curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
    \"username\": \"irvine\",
    \"email\": \"irvine@localhost\",
    \"emailVerified\": true,
    \"firstName\": \"Irvine\",
    \"lastName\": \"Kinneas\",
    \"enabled\": true,
    \"credentials\": [{
      \"type\": \"password\",
      \"temporary\": false,
      \"value\": \"Irvine-1234\"
    }]
  }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users" \

KC_USER_ID=$(curl -s \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users?exact=true&username=irvine" \
  | jq -r '.[0].id' \
)

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_ITEM_READ}\",
    \"name\":\"item:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_ITEMINVENTORY_READ}\",
    \"name\":\"itemInventory:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_WEAPON_READ}\",
    \"name\":\"weapon:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_WEAPONREMODEL_READ}\",
    \"name\":\"weaponRemodel:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "[{
    \"id\":\"${KC_CLIENT_ROLE_ID_USERPROFILE_READ}\",
    \"name\":\"userProfile:read\",
    \"description\":\"\"
  }]" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users/${KC_USER_ID}/role-mappings/clients/${KC_CLIENT_ID}" \

# Create user - quistis
curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
    \"username\": \"quistis\",
    \"email\": \"quistis@localhost\",
    \"emailVerified\": true,
    \"firstName\": \"Quistis\",
    \"lastName\": \"Trepe\",
    \"enabled\": true,
    \"credentials\": [{
      \"type\": \"password\",
      \"temporary\": false,
      \"value\": \"Quistis-1234\"
    }]
  }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users" \

# Create user - quistis
curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
    \"username\": \"quistis\",
    \"email\": \"quistis@localhost\",
    \"emailVerified\": true,
    \"firstName\": \"Quistis\",
    \"lastName\": \"Trepe\",
    \"enabled\": true,
    \"credentials\": [{
      \"type\": \"password\",
      \"temporary\": false,
      \"value\": \"Quistis-1234\"
    }]
  }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users" \

# Create user - rinoa
curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
    \"username\": \"rinoa\",
    \"email\": \"rinoa@localhost\",
    \"emailVerified\": true,
    \"firstName\": \"Rinoa\",
    \"lastName\": \"Heartilly\",
    \"enabled\": true,
    \"credentials\": [{
      \"type\": \"password\",
      \"temporary\": false,
      \"value\": \"Rinoa-1234\"
    }]
  }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users" \

# Create user - selphie
curl -s \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: bearer ${KC_ACCESS_TOKEN}" \
  -d "{
    \"username\": \"selphie\",
    \"email\": \"selphie@localhost\",
    \"emailVerified\": true,
    \"firstName\": \"Selphie\",
    \"lastName\": \"Tilmitt\",
    \"enabled\": true,
    \"credentials\": [{
      \"type\": \"password\",
      \"temporary\": false,
      \"value\": \"Selphie-1234\"
    }]
  }" \
  "${KEYCLOAK_URL_BASE}/admin/realms/${KEYCLOAK_REALM}/users" \

echo "User created

| ID | Username | Password     | Email             | Role                               |
|----|----------|--------------|-------------------|------------------------------------|
| admin    | admin        |                   | admin                              |
| squall   | Squall-1234  | squall@localhost  | item:write, itemInventory:write,   |
|          |              |                   | weapon:write, weaponRemodel:write, |
|          |              |                   | userProfile:write                  |
| zell     | Zell-1234    | zell@localhost    | item:read, itemInventory:read,     |
|          |              |                   | weapon:read, weaponRemodel:read,   |
|          |              |                   | userProfile:read                   |
| irvine   | Irvine-1234  | irvine@localhost    | item:read, itemInventory:read,     |
|          |              |                   | weapon:read, weaponRemodel:read,   |
|          |              |                   | userProfile:read                   |
| irvine   | Irvine-1234  | irvine@localhost  |                                    |
| quistis  | Quistis-1234 | quistis@localhost |                                    |
| rinoa    | Rinoa-1234   | rinoa@localhost   |                                    |
| selphie  | Selphie-1234 | selphie@localhost |                                    |
"

# for Debug
#while :; do sleep 10; done
