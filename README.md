# Project Title
Nemisis : Apache Camel Spring Boot Project

## Getting Started



### Prerequisites

- Java 8
- PostgresSql
- Knowledge of Spring Boot
 
 ## Application
-  jdbc -  Postgres DB configuration
-  Exception handling
-  Application Health Check Configuration.


### PostgresSql
```
--------------------------------------------------------------------------
CREATE TABLE ITEMS (
     ITEM_I SERIAL,
     SKU TEXT NOT NULL,
     ITEM_DESCRIPTION TEXT DEFAULT NULL,
     PRICE NUMERIC (5,2),
     CRTE_TS TIMESTAMPTZ NULL DEFAULT current_timestamp
);

select * from items;

--------------------------------------------------------------------------
```
-------------------------------------------------------------------------
```
Health endpoint: http://localhost:8080/health

{
  "status": "UP",
     "camel": {
       "status": "UP",
       "name": "camel-1",
       "version": "2.20.1",
       "contextStatus": "Started"
   },
"camel-health-checks": {
    "status": "UP",
    "route:route1": "UP"
},
"diskSpace": {
    "status": "UP",
    "total": 499963174912,
    "free": 444060545024,
    "threshold": 10485760
},
"db": {
     "status": "UP",
     "database": "PostgreSQL",
     "hello": 1
  }
}
---------------
```
