package io.helidon.examples.sockshop.orders;

import io.helidon.microprofile.grpc.core.GrpcMarshaller;
import io.helidon.microprofile.grpc.core.RpcService;
import io.helidon.microprofile.grpc.core.Unary;

@RpcService(name = "PaymentGrpc")
@GrpcMarshaller("jsonb")
public interface ShippingClient {
    @Unary
    Shipment ship(ShippingRequest request);
}
