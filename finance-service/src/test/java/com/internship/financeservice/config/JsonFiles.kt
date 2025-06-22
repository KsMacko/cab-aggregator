package com.internship.financeservice.config

object JsonFiles {
    const val CARD_BASE_URL = "/api/v1/finance/cards"
    const val FINANCE_BASE_URL = "/api/v1/finance"
    const val WALLET_BASE_URL = "/api/v1/finance/wallet"

    const val CARD_REQUEST_PATH = "/json/card-request.json"
    const val PAYMENT_REQUEST_PATH = "/json/payment-request.json"
    const val WALLET_TRANSFER_REQUEST_PATH = "/json/wallet-transfer-request.json"

    val cardRequest by lazy { JsonFileReader.readJsonFile(CARD_REQUEST_PATH) }
    val paymentRequest by lazy { JsonFileReader.readJsonFile(PAYMENT_REQUEST_PATH) }
    val walletTransferRequest by lazy { JsonFileReader.readJsonFile(WALLET_TRANSFER_REQUEST_PATH) }
}