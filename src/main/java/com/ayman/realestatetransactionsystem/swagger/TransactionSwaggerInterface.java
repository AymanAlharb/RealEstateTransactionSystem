package com.ayman.realestatetransactionsystem.swagger;

import com.ayman.realestatetransactionsystem.model.dto.request.CreateApprovalRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.PaymentRequest;
import com.ayman.realestatetransactionsystem.model.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

public interface TransactionSwaggerInterface {

    @PostMapping("/{propertyId}")
    @Operation(
            summary = "Request a property purchase",
            description = "Only buyers can request properties. " +
                    "This is the first endpoint of four to complete the transaction. " +
                    "Property gets locked during the request."
    )
    ResponseEntity<ApiResponse> requestProperty(
            @Parameter(description = "Property ID required for property request", required = true)
            @PathVariable Long propertyId
    );

    @PatchMapping("/seller-process")
    @Operation(
            summary = "Seller approve or disapprove a property transaction request",
            description = "Only sellers who own the property can approve the transaction.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Property id and decision required"
            )
    )
    ResponseEntity<ApiResponse> sellerApproveOrDissApprove(
            @Valid @RequestBody CreateApprovalRequest approvalRequest
    );

    @PatchMapping("/broker-process")
    @Operation(
            summary = "Broker approve or disapprove a property transaction request",
            description = "Only brokers who have authority on the property can approve the transaction.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Property id and decision required"
            )
    )
    ResponseEntity<ApiResponse> brokerApproveOrDissApprove(
            @Valid @RequestBody CreateApprovalRequest approvalRequest
    );

    @PostMapping("/payment")
    @Operation(
            summary = "Buyer paying the property price (last step)",
            description = "Only buyers who requested the transaction can pay for the property.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Transaction id and CVV required"
            )
    )
    ResponseEntity<ApiResponse> payment(
            @Valid @RequestBody PaymentRequest paymentRequest
    );
}
