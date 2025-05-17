package com.internship.financeservice.dto.mapper

import com.internship.financeservice.dto.request.RequestWalletTransferDto
import com.internship.financeservice.dto.response.ResponseTransferDto
import com.internship.financeservice.entity.WalletTransfer
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.Mappings

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface WalletTransferMapper {
    @Mappings(
        Mapping(target = "date", source = "financialOperation.createdAt"),
        Mapping(target = "amount", source = "financialOperation.amount"),
        Mapping(target = "driverId", source = "wallet.driverId")
    )
    fun toDto(entity: WalletTransfer): ResponseTransferDto

    @Mapping(target = WalletTransfer.ID, ignore = true)
    @Mapping(target = WalletTransfer.WALLET, ignore = true)
    @Mapping(target = WalletTransfer.FINANCIAL_OPERATION, ignore = true)
    @Mapping(target = "remainingAmount", ignore = true)
    fun toEntity(dto: RequestWalletTransferDto): WalletTransfer
}