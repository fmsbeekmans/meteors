# ☄️ Meteor challenge 

A small API that serves meteors.

## Usage
The application can be ran using `sbt run`.
Further configuration can be done through the environmment variables:
* `PORT` - The port the server runs on, default `8080`
* `HOST` - the host address for the server, default `0.0.0.0`
* `NEAR_EARTH_OBJECTS_BASE_URI` - The base uri of the NASA api to use, default `https://api.nasa.gov/neo/rest/v1`
* `NEAR_EARTH_OBJECTS_API_KEY` - Api key for the NASA api, default `DEMO_KEY`

## Endpoints
* `GET /browse` gives a list of unordered meteors
* `GET /details/:id` gives the details of the meteor with id `:id`
* `GET /search/:from/:to` Searches the meteors from `:from` to `:to`, expected format `YYYY-MM-DD`