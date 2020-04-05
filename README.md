
Project:Nemisis 

Application: Spring Boot Camel

Health endpoint: http://localhost:8080/health

 
-   jdbc -  Postgres DB configuration
-  Exception handling
-   Application Health Check Configuration.

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