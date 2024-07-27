# Address API SPEC

## Create Address

Endpoint: POST /api/contacts/{idContact}/addresses

Request Header:

X-API-TOKEN : Token (Mandatory)

Request Body:
```json
{
  "street": "Jalan Buah Batu II No. 3",
  "city": "Bandung",
  "province": "Jawa Barat",
  "country": "Indonesia",
  "postalCode": "14177"
}
```

Response Body (success):
```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "id": "random string",
      "street": "Jalan Buah Batu II No. 3",
      "city": "Bandung",
      "province": "Jawa Barat",
      "country": "Indonesia",
      "postalCode": "14177"
    }]
  }
}
```

Response Body (Failed):
```json
{
  "success": false,
  "data": {
    "errors": "Unauthorized,data invalid, contact is not found esc",
    "lst": []
  }
}
```

## Update Address
Endpoint: PUT /api/contacts/{idContact}/addresses/{idAddress}

Request Header:
X-API-TOKEN : Token (Mandatory)

Request Body:
```json
{
  "street": "Jalan Buah Batu II No. 3",
  "city": "Bandung",
  "province": "Jawa Barat",
  "country": "Indonesia",
  "postalCode": "14177"
}
```

Response Body (success):
```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "id": "random string",
      "street": "Jalan Buah Batu II No. 3",
      "city": "Bandung",
      "province": "Jawa Barat",
      "country": "Indonesia",
      "postalCode": "14177"
    }]
  }
}
```

Response Body (Failed):
```json
{
  "success": false,
  "data": {
    "errors": "Unauthorized,data invalid, contact is not found esc",
    "lst": []
  }
}
```

## Get Address
Endpoint: GET /api/contacts/{idContact}/addresses/{idAddress}

Request Header:
X-API-TOKEN : Token (Mandatory)

Response Body (success):
```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "id": "random string",
      "street": "Jalan Buah Batu II No. 3",
      "city": "Bandung",
      "province": "Jawa Barat",
      "country": "Indonesia",
      "postalCode": "14177"
    }]
  }
}
```

Response Body (Failed):
```json
{
  "success": false,
  "data": {
    "errors": "Unauthorized,data invalid, contact is not found esc",
    "lst": []
  }
}
```

## Remove Address
Endpoint: DELETE /api/contacts/{idContact}/addresses/{idAddress}

Request Header:
X-API-TOKEN : Token (Mandatory)

Response Body (success):
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
    "errors": "Unauthorized,data invalid, contact is not found esc",
    "lst": []
  }
}
```

## List Address
Endpoint: GET /api/contacts/{idContact}/addresses

Request Header:
X-API-TOKEN : Token (Mandatory)

Response Body (success):
```json
{
  "success": true,
  "data": {
    "errors": "",
    "lst": [{
      "id": "random string",
      "street": "Jalan Buah Batu II No. 3",
      "city": "Bandung",
      "province": "Jawa Barat",
      "country": "Indonesia",
      "postalCode": "14177"
    }]
  }
}
```

Response Body (Failed):
```json
{
  "success": false,
  "data": {
    "errors": "Unauthorized,data invalid, contact is not found esc",
    "lst": []
  }
}
```
