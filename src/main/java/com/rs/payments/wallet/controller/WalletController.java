package com.rs.payments.wallet.controller;

import com.rs.payments.wallet.dto.CreateWalletRequest;
import com.rs.payments.wallet.dto.DepositRequest;
import com.rs.payments.wallet.dto.TransferRequest;
import com.rs.payments.wallet.dto.WithdrawRequest;
import com.rs.payments.wallet.model.Wallet;
import com.rs.payments.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@Tag(name = "Wallet Management", description = "APIs for managing user wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(
            summary = "Create a new wallet for a user",
            description = "Creates a new wallet for the specified user ID with a zero balance.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Wallet created successfully",
                            content = @Content(schema = @Schema(implementation = Wallet.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User already has a wallet"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.createWalletForUser(request.getUserId());
        return ResponseEntity.status(201).body(wallet);
    }

    @PostMapping("/{walletId}/deposit")
    @Operation(
            summary = "Deposit funds into wallet",
            description = "Adds funds to the wallet and records a DEPOSIT transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful",
                    content = @Content(schema = @Schema(implementation = Wallet.class))),
            @ApiResponse(responseCode = "400", description = "Invalid deposit amount"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<Wallet> deposit(
            @PathVariable UUID walletId,
            @Valid @RequestBody DepositRequest request) {

        Wallet wallet = walletService.deposit(walletId, request.getAmount());

        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/{walletId}/withdraw")
    @Operation(
            summary = "Withdraw funds from wallet",
            description = "Withdraws funds from the wallet if sufficient balance is available and records a WITHDRAWAL transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful",
                    content = @Content(schema = @Schema(implementation = Wallet.class))),
            @ApiResponse(responseCode = "400", description = "Insufficient balance or invalid amount"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<Wallet> withdraw(
            @PathVariable UUID walletId,
            @Valid @RequestBody WithdrawRequest request) {

        Wallet wallet = walletService.withdraw(walletId, request.getAmount());

        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/{walletId}/balance")
    @Operation(
            summary = "Get wallet balance",
            description = "Returns the current balance of the wallet."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID walletId) {

        BigDecimal balance = walletService.getBalance(walletId);

        return ResponseEntity.ok(balance);
    }
}