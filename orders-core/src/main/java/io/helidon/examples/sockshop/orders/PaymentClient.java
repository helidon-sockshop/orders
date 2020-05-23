package io.helidon.examples.sockshop.orders;

import io.helidon.microprofile.grpc.client.GrpcChannel;
import io.helidon.microprofile.grpc.core.GrpcMarshaller;
import io.helidon.microprofile.grpc.core.RpcService;
import io.helidon.microprofile.grpc.core.Unary;

@RpcService(name = "PaymentGrpc")
@GrpcChannel(name = "payment")
@GrpcMarshaller("jsonb")
public interface PaymentClient {
   @Unary
   Payment authorize(PaymentRequest request);
}
