# Game of Shutl

For our tech test, we'd like you to take stripped-down version of our quoting engine, and then add some features. This is a RESTful service endpoint that takes a few details and works out the price for a delivery.

Throughout the test we're looking for great java style, driving your code through tests (and refactoring) and at all times doing the bare minimum possible to get the job done. If you don't like the code or tests that are there already, feel free to refactor as you add features.

Please don't spend more than 90 minutes on the test - it's fine not to complete all the features - just make sure that the features you do complete are done to a standard that you're happy with. Please complete the features in order.

On the name: Our old tech test was the [game of life](http://en.wikipedia.org/wiki/Conway's_Game_of_Life) - but the Game of Shutl is much more interesting :)

## Completed Features

### Basic Service

Build a basic service that responds to a POST to /quotes, with the following request structure:

    {
      "pickup_postcode":   "SW1A1AA",
      "delivery_postcode": "EC2A3LT"
    }

And responds with the following price:

    {
      "pickup_postcode":   "SW1A1AA",
      "delivery_postcode": "EC2A3LT",
      "price":             316
    }

### Variable Prices By Distance

The price we charge depends on the distance between two postcodes. We won't implement postcode geocoding here, so instead let's use a basic formula for working out the price for a quote between two postcodes. The process is to take the base-36 integer of each postcode, substract the delivery postcode from the pickup postcode and then divide by some large number. If the result is negative, turn it into a positive.

Hint: in java, this would be:

    Long.valueOf("SW1A1AA", 36) - Long.valueOf("EC2A3LT", 36)

If you have a better idea for a deterministic way of making a number from two postcodes, please feel free to use that instead.

Update your service to calculate pricing based upon these rules.

## Pending Features

### Simple Variable Prices By Vehicle

Our price changes based upon the vehicle. Implement a "vehicle" attribute on the request, that takes one of the following values, applying the appropriate markup:

* bicycle: 10%
* motorbike: 15%
* parcel_car: 20%
* small_van: 30%
* large_van: 40%

The vehicle should also be returned in the response.

### Vehicle Price Limits

Each vehicle has a limit in the distance it can travel, which is reflected in the price. Update your logic so that each vehicle has an upper price limit, after which the next vehicle is selected:

* bicycle: 500
* motorbike: 750
* parcel_car: 1000
* small_van: 1500
* large_van: no limit

### Simple Volumetrics

Another feature of Shutl is that if the vehicle is not specifed, we calculate what vehicle is required based upon the volumetrics (weights and dimensions) of the product.

Update the service to accept an array of products, each of which is a hash, ie:

    {
      "pickup_postcode":   "SW1A1AA",
      "delivery_postcode": "EC2A3LT",
      "products" : [
        {
          weight: 10,
          width: 50,
          height: 50,
          length: 50
        }
      ]
    }

Weight is specified in kilograms, dimensions in centimetres.

The service should then calculate the smallest possible vehicle which could be used for this job. The vehicle capacities are:

* bicycle: Weight 3kg, Capacity: L30 x W25 x H10 cm
* motorbike: Weight: 6kg Max. Capacity: L35 x W25 x H25 cm
* parcel_car: Weight: 50kg Max. Capacity: L100 x W100 x H75 cm
* small_van: Weight: 400kg Max. Capacity: L133 x W133 x H133 cm
* large_van: unlimited

Don't worry about working out the vehicle if there are multiple products - assume there will always be only one.

### Complex Volumetrics

... assume no longer. Update your calculation that decides which vehicle should be used to deal with multiple products. You can assume that the volumetrics are simply to be summed together.

## Icebox

### Vehicle Becomes Vehicle_Id

A change in requirements has come up - that vehicle is renamed vehicle_id.

In order to not break existing clients, you and the team have decided to add a header which will allow the client to specify the version of the API they want to use. Update your service to allow a header to be passed in, and to accept/show different the correct attribute depending on the version.


# Dependencies

- `gradle`: make sure is correctly installed on your machine. `brew` can help you with the installation.

## Useful commands

Run tests from command line:
```
gradle test
```

Run server locally:
```
gradle bootRun
```

Make quote request:
```
echo '{"pickupPostcode": "SW1A1AA", "deliveryPostcode": "EC2A3LT" }' | \
curl -d @- http://localhost:8080/quote --header "Content-Type:application/json"
```
