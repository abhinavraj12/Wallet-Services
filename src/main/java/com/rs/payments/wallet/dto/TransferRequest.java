package com.rs.payments.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "Request to transfer funds between wallets")
public class TransferRequest {

    @NotNull
    @Schema(description = "Source wallet ID")
    private UUID fromWalletId;

    @NotNull
    @Schema(description = "Destination wallet ID")
    private UUID toWalletId;

    @NotNull
    @DecimalMin(value = "0.01")
    @Schema(description = "Transfer amount", example = "20.00")
    private BigDecimal amount;
}

