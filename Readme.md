# AFS Project
[![N|Arvato](https://pbs.twimg.com/profile_images/1155782883652620288/E64nHD5Q_400x400.jpg)](https://itarvato.com)

## Description
A small project that is related to the invoice management per customer.
Creating new customers and adding new invoices to them to organize economic progress.


## Technologies

* [Spring Boot](http://projects.spring.io/spring-boot/) (Web, Data JDBC, Security)
* [Maven](http://maven.apache.org/)
* [H2 Database](https://www.h2database.com/)
* [Docker](https://www.docker.com/)
* [Docker Compose](https://docs.docker.com/compose/)
* [AngularJS](https://angularjs.org/)

# APIs

### Customers
```
POST ../customers
```
```
GET ../customers/{customerId}
```
```
PUT ../customers/{customerId}
```
```
DELETE ../customers/{customerId}
```
### Invoices
```
POST ../customers/{customerId}/invoices
```
```
GET ../customers/{customerId}/invoices
```
```
PUT ../customers/{customerId}/invoices/{invoiceId}
```
```
GET ../customers/{customerId}/invoices/{invoiceId}
```
```
DELETE ../customers/{customerId}/invoices/{invoiceId}
```

# Pre-requisite
Before deploying the project, your computer should have below dependencies;

- Docker 

## Deploy the project


### Build the project
```
mvn verify
```
###  Docker - build the images


- Go to the "invoice-api" file
```
docker build -t invoice-api .
```
- Go to the "invoice-app" file
```
docker build -t invoice-app .
```
### Docker - run the containers
```
docker run -p 8080:8080 invoice-api 

docker run -p 4200:4200 invoice-app 
```
### Just use docker-compose
```
docker-compose up
```

Please feel free to contact if you need any acquiry.

>> Nur Erktartal
