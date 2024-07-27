# Contact API SPEC

## Create Contact
Endpoint: POST /api/contacts

Request Header:
X-API-TOKEN : Token (Mandatory)

Request Body:

```json
{
  "firstName": "Edi",
  "lastName": "Yasa",
  "email": "ediyasa@mail.com",
  "phone": "081911797878"
}
```

Response Body (Success):
```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "id": "random string",
      "firstName": "Edi",
      "lastName": "Yasa",
      "email": "ediyasa@mail.com",
      "phone": "081911797878"
    }]
  }
}
```

Response Body (Failed):
```json
{
  "success": false,
  "data": {
    "errors": "Unauthorized, Email format invalid, esc",
    "lst": []
  }
}
```

## Update Contact
Endpoint: PUT /api/contacts/{idContact}

Request Header:
X-API-TOKEN : Token (Mandatory)

Request Body:
```json
{
  "firstName": "Edi",
  "lastName": "Yasa",
  "email": "ediyasa@mail.com",
  "phone": "081911797878"
}
```

Response Body (Success):
```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "id": "random string",
      "firstName": "Edi",
      "lastName": "Yasa",
      "email": "ediyasa@mail.com",
      "phone": "081911797878"
    }]
  }
}
```

Response Body (Failed):
```json
{
  "success": false,
  "data": {
    "errors": "Unauthorized, Email format invalid, esc",
    "lst": []
  }
}
```

## Get Contact
Endpoint: GET /api/contacts/{idContact}

Request Header:
X-API-TOKEN : Token (Mandatory)

Response Body (Success):
```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "id": "random string",
      "firstName": "Edi",
      "lastName": "Yasa",
      "email": "ediyasa@mail.com",
      "phone": "081911797878"
    }]
  }
}
```

Response Body (Failed, 404):
```json
{
  "success": false,
  "data": {
    "errors": "Contact is Not Found",
    "lst": []
  }
}
```

## Remove Contact
Endpoint: DELETE /api/contacts/{idContact}

Request Header:
X-API-TOKEN : Token (Mandatory)

Response Body (Success):
```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": []
  }
}
```
Response Body (Failed):
```json
{
  "success": false,
  "data": {
    "errors": "Contact is Not Found, Unauthorized",
    "lst": []
  }
}
```

## Search Contact
Endpoint: GET /api/contacts

Query Param:
- name : String, contact firstname or lastname, using like query, optional
- phone : String, contact phone, using like query, optional
- email : String, contact email, using like query, optional
- page : Integer, start from 1, default 1
- size : Integer, default 10

Request Header:
X-API-TOKEN : Token (Mandatory)

Response Body (Success):

```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [
      {
        "id": "random string",
        "firstName": "Edi",
        "lastName": "Yasa",
        "email": "ediyasa@mail.com",
        "phone": "081911797878"
      }
    ],
    "paging": {
      "currentPage": 1,
      "totalPage": 10,
      "size": 10
    }
  }
}
```

Response Body (Failed):
```json
{
  "success": false,
  "data": {
    "errors": "Unauthorized",
    "lst": []
  }
}
```
