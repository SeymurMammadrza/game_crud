#Game CRUD 

####A simple Game CRUD example utilizing ReactiveMongo on Play Framework. It demonstrates ;



- MongoDb Connection using reactiveMongoDb in Play

- BSONReader and BSONWriter Implementation

- CRUD using different data type

####This app use the following ;
- Play Framework 2.8.0
- Scala 2.13.3
- Reactive Scala Driver for MongoDB 0.20.13-play28
- MongoDB



####To use the app ;

- Operate the Docker
`docker-compose up`
- Run the project
`sbt run`

####Routes:
- GET     `http://localhost:9000/games`                
- GET     `http://localhost:9000/games/:id`
- POST    `http://localhost:9000/games`              
- PUT     `http://localhost:9000/games/:id`
- DELETE  `http://localhost:9000/games/:id`

####POST Request Body Example:

`curl --verbose --header "Content-Type: application/json" \
--request POST \
--data '{ "name":"Witcher 3", "genre":"RPG" }' \
http://localhost:9000/games`

####POST Response Example:
`
< HTTP/1.1 201 Created
< Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
< X-Frame-Options: DENY
< X-XSS-Protection: 1; mode=block
< X-Content-Type-Options: nosniff
< X-Permitted-Cross-Domain-Policies: master-only
< Date: Sun, 05 Feb 2021 15:31:02 GMT
< Content-Type: application/json
< Content-Length: 75
{
"name": "Witcher 3",
"genre": "RPG"
}*
`
####GET Query Example:
`curl --verbose --request GET http://localhost:9000/games`

####GET Query Response Example:
`< HTTP/1.1 200 OK
< Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
< X-Frame-Options: DENY
< X-XSS-Protection: 1; mode=block
< X-Content-Type-Options: nosniff
< X-Permitted-Cross-Domain-Policies: master-only
< Date: Sun, 05 Feb 2021 15:31:02 GMT
< Content-Type: application/json
< Content-Length: 213
[
{
{
"_id": "BSONObjectID(\"61fe5e71d01dabc1daec1082\")",
"_updateDate": "05/02/2022 15:31:02",
"name": "Witcher 3",
"genre": "RPG"
}
]*`