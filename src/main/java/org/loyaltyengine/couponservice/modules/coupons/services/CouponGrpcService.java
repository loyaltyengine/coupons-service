package org.loyaltyengine.couponservice.modules.coupons.services;

import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.proto.CouponRequest;
import org.loyaltyengine.couponservice.proto.CouponResponse;
import org.loyaltyengine.couponservice.proto.CouponServiceGrpc.CouponServiceImplBase;
import org.springframework.grpc.server.service.GrpcService;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class CouponGrpcService extends CouponServiceImplBase {

    private final CouponService couponService;

    @Override
    public void getCoupon(CouponRequest req, StreamObserver<CouponResponse> responseObserver) {
        log.info("Hello " + req.getName());
        
        CouponResponse reply = CouponResponse.newBuilder().setMessage("Hello ==> " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
