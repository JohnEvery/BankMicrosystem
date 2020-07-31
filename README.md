<p align="center">
  <img src="https://seeklogo.com/images/S/spring-logo-9A2BC78AAF-seeklogo.com.png" width="140">
</p>



# BankMicrosystem

Java RESTful API for money transfers between customers account bills.

### Technologies
- Spring Boot
- PostrgreSQL
- RabbitMq


### How to run
```sh
To launch classes Application.java in servecies customerService, accountService, 
billService and notificationService.
```

Services billServise, accountService, customerService and notificationService   start on localhost ports 8081, 8082, 9090, 9999 respectively. 
PostgreSQL database and RabbitMQ broker initialized with some sample user. 


### Available Services

## Сustomer Service (context-path: /customers)
| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /{id} | get customer by id | 
| POST | / | create new customer |
| POST | /deposit | make deposit |
| GET | /deposit/{id} | get deposit by id |
| GET | /deposit/bill/{billId} | get all deposits by bill id |
| GET | /deposit/account/{accountEmail} | get all deposits by email |
| POST | /withdraw | make withdraw |
| GET | /withdraw/{id} | get withdraw by id |
| GET | /withdraw/bill/{billId} | get all withdraws by bill id |
| GET | /withdraw/account/{accountEmail} | get all withdraws by email |
| POST | /transfer | make transfer |
| GET | /transfer/{id} | get transfer by id |
| GET | /transfer/bill/{billId} | get all transfers by bill id |
| GET | /transfer/account/{accountEmail} | get all transfers by email |

### Sample JSON for Сustomer Service
##### Create customer : 
```sh
{
    "accountRequestDTO": {
        "name": "John Every",
        "email": "email@example.xyz",
        "phone": "+1234500"
    },
    "billRequestDTO": [
        {
            "isDefault": false,
            "amount": 100000,
            "overdraftEnabled": false
        },
        {
            "isDefault": true,
            "amount": 300000,
            "overdraftEnabled": true
        }
    ]
}
```

## Account Service (context-path: /accounts)
| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /{id} | get account by id | 
| GET | / |  get all accounts | 
| GET | /email/{email} |  get account by email| 
| POST | / | create account |
| PUT | /{id} | update account by id | 
| DEL | /{id} | delete account by id | 

### Sample JSON for Account Service
##### Create account : 
```sh
    {
        "name": "John Every",
        "email": "email@example.xyz",
        "phone": "+1234500"
    } 
```

### Sample JSON for Deposit
##### Commit deposit for one customer : 
```sh
{
    "billId": 2,
    "amount": 1000
} 
```

##### Commit transfer for two customers : 
```sh
{
    "accountIdFrom": 1,
    "accountIdTo": 3,
    "amount": 20000
    //It is enough to specify only the account id, to search for default bills for accounts
}
```
