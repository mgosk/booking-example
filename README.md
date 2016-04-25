# Booking app example

## Development prerequisites

* SBT 
* Java 1.8 
* MongoDb

## How to run app

Go to app directory and type in console `sbt run` or `sbt ~run` for live reloading

## Developer notes

* Objects identifiers are implemented as UUID's because we don't know further DB implementation

## To made it simple I assume that:

* Room price is constant during year
* Price is integer

## TODO

* attach production database (mongoDB?)
* unit tests
* add authentication & authorization
* API docs
* guarantee operation atomicity
* include copy of reservation into room to accelerate search
* use joda Datetime instead java Date