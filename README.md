# Manage accounts API

A Java RESTful API for managing bank accounts in DDD style.
To store accounts used in-memory key-value storage

### Technologies
- Spring (Boot, Data, WEB, WebFlux, AOP, Test)
- Tomcat
- Logback
- Lombok
- MapStruct
- Hibernate Validation
- Reactor


### How to run
```sh
mvn exec:java
```

Application starts a tomcat server on localhost:8080 

### Available Services

| METHOD | PATH | DESCRIPTION |
| -----------| ------ | ------ |
| GET | /accounts/{accountId} | Get account by accountId | 
| GET | /accounts/ | Get all accounts | 
| POST | /accounts/ | Create a new account
| DELETE | /accounts/{accountId} | Remove account by accountId | 
| POST | /deposits/ | Deposit money to account | 
| POST | /withdraws/ | Withdraw money from account | 
| POST | /transfers/ | Transfer between 2 accounts | 

### Http Status
- 200 OK: Successful
- 400 Bad Request: Invalid request 
- 404 Not Found: Resource cannot be found
- 500 Internal Server Error: Unexpected server error

### Sample JSON 
#### Account : 
```json
{  
  "id": 1,
  "amount": 100.0,
  "currencyCode": "RUB"
} 
```
#### Deposit/Withdraw: : 

```json
{  
   "accountId": 10,
   "amount": 15.0,
   "currencyCode": "EUR"
} 
```

#### Transfer:
```json
{  
   "sourceAccountId": 18,
   "targetAccountId": 21,
   "amount": 100000.00,
   "currencyCode": "USD"
}
```
