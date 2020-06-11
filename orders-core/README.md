# orders-core

This module contains the bulk of the Orders service implementation, including the 
[domain model](./src/main/java/io/helidon/examples/sockshop/orders/Order.java), 
[REST API](./src/main/java/io/helidon/examples/sockshop/orders/OrderResource.java), as well as the
[data repository abstraction](./src/main/java/io/helidon/examples/sockshop/orders/OrderRepository.java) 
and its [in-memory implementation](./src/main/java/io/helidon/examples/sockshop/orders/DefaultOrderRepository.java).

## Building the Service

See [main documentation page](../README.md#building-the-service) for instructions.

## Running the Service

Because this implementation uses in-memory data store, it is trivial to run.

Once you've built the Docker image per instructions above, you can simply run it by executing:

```bash
$ docker run -p 7001:7001 helidon/sockshop/orders-core
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
