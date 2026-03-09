package org.loyaltyengine.couponservice.modules.coupons.grpc;

import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CreateCouponDto;
import org.loyaltyengine.couponservice.modules.coupons.mappers.CouponGrpcMapper;
import org.loyaltyengine.couponservice.modules.coupons.services.CouponService;
import org.springframework.grpc.server.service.GrpcService;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openapitools.CouponsModels.CouponResponse;
import openapitools.services.couponsservice.CouponsServiceGrpc.CouponsServiceImplBase;
import openapitools.services.couponsservice.CouponsServiceOuterClass.CreateCouponRequest;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class CouponGrpcService extends CouponsServiceImplBase {

    private final CouponService couponService;
    private final CouponGrpcMapper mapper;

    @Override
    public void createCoupon(CreateCouponRequest request, StreamObserver<CouponResponse> responseObserver) {
        log.info("gRPC: Creating a new coupon {}", request.toString());
        // Request
        CreateCouponDto dto = mapper.grpcToDto(request.getBaseCreateCouponRequest());
        dto.setPropertyId(request.getPropertyId());
        dto.setCustomerId(request.getCustomerId());

        // Create a new coupon
        CouponDto couponDto = couponService.createCoupon(dto);

        // Response (ignore status)
        CouponResponse response = CouponResponse.newBuilder()
                .setCoupon(mapper.toGrpcCoupon(couponDto)).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
