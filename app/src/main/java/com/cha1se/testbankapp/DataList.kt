package com.cha1se.testbankapp

data class DataList(
    var number: NumberList,
    var scheme: String,
    var type: String,
    var brand: String,
    var prepair: Boolean,
    var country: CountryList,
    var bank: BankList
)

data class NumberList(
    var length: Int,
    var luhn: Boolean
)

data class CountryList(
    var numeric: String,
    var alpha2: String,
    var name: String,
    var emoji: String,
    var currency: String,
    var latitude: Int,
    var longitude: Int
)

data class BankList(
    var name: String,
    var url: String,
    var phone: String,
    var city: String
)
