# User API Spec

## Register User
Endpoint : POST /api/users

Request Body:

```json
{
  "username": "yassa_edi",
  "password": "rahasia",
  "name": "Edi Yasa"
}
```

Response Body (Success):

```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "username": "yassaedi",
      "name": "Edi Yasa"
    }]
  }
}
```

Response Body (Failed):

```json
{
  "success": false,
  "data": {
    "errors": "username must not blank, ...",
    "lst": []
  }
}
```

## Login User
Endpoint : POST /api/auth/login

Request Body:

```json
{
  "username": "yassa_edi",
  "password": "rahasia"
}
```

Response Body (Success):

```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "token": "token",
      "expireAt": 635379282773 //milliseconds
    }]
  }
}
```

Response Body (Failed):

```json
{
  "success": false,
  "data": {
    "errors": "username or password wrong",
    "lst": []
  }
}
```

## Get User
Endpoint : GET /api/users/current

Request Header:
X-API-TOKEN : Token (Mandatory)

Response Body (Success):

```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": {
      "username": "yassa_edi",
      "name": "Edi Yasa"
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

## Update User
Endpoint : PATCH /api/users/current

Request Header:
X-API-TOKEN : Token (Mandatory)

Request Body:

```json
{
  "name": "Edi Yasa", // put if only want to update name
  "password": "new password" // put if only want to update password
}
```

Response Body (Success):

```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "name": "Edi Yasa"
    }]
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

## Logout User
Endpoint : DELETE /api/auth/logout

Request Header:
X-API-TOKEN : Token (Mandatory)

Response Body (Success):

```json
{
  "success": true,
  "data": [{
    "errors": "",
    "lst": []
  }]
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