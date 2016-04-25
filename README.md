# Booking app example

## Development prerequisites

* SBT 
* Java 1.8 
* MongoDb

## How to run app

Go to app directory and type in console `sbt run` or `sbt ~run` for live reloading

## To made it simpler I assume that:

* Room price is constant during year
* Price is integer
* Admin can't delete room with reservations

## TODO

* attach production database (mongoDB?)
* unit tests
* add authentication & authorization
* API docs
* guarantee operation atomicity
* include copy of reservation into room to accelerate search
* use joda Datetime instead java Date