# Booking app endpoints

API docs are written manually because swagger don't work with DI

#### POST `/hotels`

Create new hotel

Headers:
* `Content-Type => application/json`

Request body:

    {
        "name":"Hilton",
        "city":"Warsaw"
    }

#### GET `/hotels/:id`

Return information about hotel


#### POST `/hotels/:id/room`

Create new room in hotel

Headers:
* `Content-Type => application/json`

Request body:

    {
        "number": 666,
        "price": 15
    }

####  DELETE `/hotels/:id/room/:roomNr`

Delete room in hotel


#### POST `/rooms/make-reservation`

Make new reservation if possible

Headers:
* `Content-Type => application/json`

Request body:

    {
        "login": "Bob",
        "hotelId": "f33cc95e-5070-4b95-bd0e-28a5b46a91d7",
        "roomNr":101,
        "dateFrom" : "2016-02-03",
        "dateTo" : "2016-02-15"
    }

#### GET `/rooms?city=warsaw&dateFrom=2016-03-01&dateTo=2016-03-15&maxPrice=1000&minPrice=1`

Return available rooms `maxPrice` and `minPrice` are only optional parameters

#### POST `/customers`

Create new customer

Headers:
* `Content-Type => application/json`

Request body:

    {
        "login":"Bob"
    }


#### GET `/cutomers/:login/reservations`

Return user reservations
