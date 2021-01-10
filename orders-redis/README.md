# orders-redis

This module implements [Redis backend](./src/main/java/io/helidon/examples/sockshop/orders/redis/RedisOrderRepository.java)
for the [Orders Service](../README.md) using Redisson client.

## Building the Service

See [main documentation page](../README.md#building-the-service) for instructions.

## Running the Service

This implementation is slightly more complex to run, because it requires a Redis instance
to use as a data store.

First you will need to create a Docker network that will be used by both Redis and the service 
containers, if you haven't done that already:

```bash
$ docker network create sockshop 
``` 

Then you can run Redis container, but you need to assign it to the `sockshop` network 
created above, and give it a name that the service container expects (`orders-db` in this case):

```bash
$ docker run --rm --name orders-db --network sockshop redis:5.0.7
``` 
> **Note:** The `--rm` flag above ensures that the container is removed automatically after it is 
> stopped. This allows you to re-run the command above without having to remove the `orders-db`
> container manually between runs.

Finally, you can start the service container in the same network:

```bash
$ docker run --network sockshop -p 7001:7001 ghcr.io/helidon-sockshop/orders-redis
``` 

Once the container is up and running, you should be able to access [service API](../README.md#api) 
by navigating to http://localhost:7001/orders/.

As a basic test, you should be able to perform an HTTP GET against `/orders/{orderId}` endpoint:

```bash
$ curl -i http://localhost:7001/orders/123
``` 
which should return 404 response code (as there are no orders in the database yet)
```bash
HTTP/1.1 404 Not Found
``` 

## License

The Universal Permissive License (UPL), Version 1.0
