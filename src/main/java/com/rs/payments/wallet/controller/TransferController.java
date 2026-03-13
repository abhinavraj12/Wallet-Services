package com.rs.payments.wallet.controller;

import com.rs.payments.wallet.dto.TransferRequest;
import com.rs.payments.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    private final WalletService walletService;

    public TransferController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    @Operation(
            summary = "Transfer funds between wallets",
            description = "Transfers funds from one wallet to another atomically. Creates TRANSFER_OUT and TRANSFER_IN transactions."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Insufficient funds or invalid request"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")

    })
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest request) {

        walletService.transfer(
                request.getFromWalletId(),
                request.getToWalletId(),
                request.getAmount());

        return ResponseEntity.ok("Transfer successful");
    }
}
