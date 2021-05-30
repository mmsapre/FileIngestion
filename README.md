# File Batch Data Ingestion Module

* [Summary](#summary)
* [Prerequisites](#prerequisites)
* [Configuratoin](#configuration)  
* [Build](#build)
* [High Level Overview](#high-level-overview)
* [Run](#deploy)
* [Author](#author)

[img-jobbatchingest]: img/FileDataIngestion.jpeg

## Summary
+ File batch data ingestion module pushes records from csv file to kafka topic.On receipt of trigger on rabbitmq exchange 
  module pushes the read file to kafka topic partition.File records can be read in chunks before transformed to a canonical format
  json model.
  
## Prerequisites

To be able to run this module it is recommended to ensure following prerequisites are fulfilled:

+ Developer is aware of tools/technologies used here:
    - Core Java and basic MVC concept
    - Spring framework (Spring DI, Beans, REST) , Spring batch and Springboot
    - MongoDb
    - RabbitMq
    - Apache Camel  
    - Kafka  
    - REST API standards & JSON (un)marshalling
    - Eclipse, Junit, Maven
+ Necessary access/installation in place:
    - Eclipse (Oxygen)
    - Maven (3.5.2+)

Ensure local installation of the following softwares/tools:

* JDK - 1.8
* MongoDb - 4.0.x
* Kafka 1.x
* RabbitMq  
* Maven - 3.6.x

---
## Build
Open your command/shell terminal and navigate to your project's root directory

#### Execute the following command to build the jar without running test cases:
mvn -U clean compile -DskipTests

## High Level Overview

This is ingest module to read csv file formats from mount path or NFS path.FileData ingestion is triggered on receiving event on RabbitMq exchange.
Read records are transformed to a map structure json before pushing the records to configured kafka topic.Every file available event triggers new job launcher which
is logged to batch collections.

Diagram below, shows high level component communication for JobBatchAPI

![JobBatchFileDataIngestion][img-jobbatchingest]

+ File data ingestion triggering event
  - Format / sample event
```shell
  {
  "fileName": "blmbergnse.csv",
  "fileUri": "/testspool/blmbergnse.csv",
  "templateId": "BLMBERG"
  }
```
  - Event stored to kafka topic after conversion to standard format as below.
```shell
{
  "headers": {
    "templateId": "BLMBERG"
  },
  "payload": {
    "HIGH": "293",
    "TURNOVER": "4157.17",
    "CLOSE": "285.25",
    "source": "BLMBERG",
    "ISIN": "INE306L01010",
    "NAME": "QHEAL",
    "OPEN": "292.95",
    "MVAL": "4157.17",
    "TOTALQUANTITY": "1446384",
    "DATE": "2018-05-10",
    "LAST": "284.15",
    "LOW": "283.45",
    "timestamp": "2021-05-26T09:49:26.004Z",
    "fileId": "blmberg-1622022563682-1"
  }
}
```
+ MongoDB Collections :
    - BatchJobInstance : Maintains different jobs executed.
    - BatchJobExecution : Contains jobs execution details for the instance.
    - BatchStepExecution : Maintains steps executed as part of job execution.
    - BatchJobParameter : Maintains job execution parameters.


## Configuration
+ Kafka properties
````snakeyaml
Kafka:
  config:
    autooffsetreset: earliest
  BootstrapServers: localhost:9092 // kafka bootstrap server with port
  ZookeeperHost: localhost:2081 // kafka zookeeper address
  topic: filespool-queue // Topic on which events have to be published
  dlq:
    topic: dlq-filespool-queue //Dlq events
````
+ File processing configuartion properties
```snakeyaml
file:
  defaultFilePath: /testspool/sample.csv // default path
  templateConfig: // Mapping for every file considered is blmberg templateId
    blmberg:  // templateId
      prefifxIdentifier: BLMBERG // Identifier for file type
      delimiter: \\, // delimeter for csv file. Support to csv with comma is provided.
      useHeader: false // use header true will skip first line
      template: ISIN|NAME|MVAL|OPEN|HIGH|LAST|LOW|CLOSE|TURNOVER|TOTALQUANTITY|DATE //mapping of csv column 
      useTemplate: true // use template currently it will be always true.
      partition: 0 // partition of kafka config
```
+ RabbitMq properties
```snakeyaml
RabbitMQ:
  Host: localhost
  Port: 5672
  User: guest
  Password: guest
  VHost: /
  name: rabbitmq
  subscriber:
    queue: filespool.queue
    exchange: filespool.exchange
    routingKey: distribution.*.*
    topic: filespool.topic
    dlqExchange: dlq.filespool.exchange
    dlqQueue: dlq.filespool.queue
    dlqRoutingKey: distribution-dlq.*.*
    dlqExchangeType: topic
```

+ MongoDb configuration

```snakeyaml
spring:
  data:
    mongodb:
      #      uri: mongodb://myadmin:secret@localhost:27017/test?authSource=admin
      host: localhost
      port: 27017
      database: test
      username:
      password:
```
## Deploy

Clone locally
Execute standard maven deploy command to build and deploy library into Artifact repository.

```shell
# Build and deploy
mvn clean deploy
```
+ To run use sample file in directory [blmbergnse.csv (resources/sample/blmbergnse.csv)](/src/main/resources/sample/blmbergnse.csv).
## Author

* Repo owner - maheshsapre@gmail.com
