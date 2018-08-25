package com.wafer.wafersample.models

data class CountryModel(
        val name: String,
        val alpha3Code: String,
        val languages: ArrayList<LanguageModel>,
        var currencies: ArrayList<CurrencyModel>) {

    override fun equals(other: Any?): Boolean {
        return other is CountryModel && other.alpha3Code == alpha3Code
    }

    override fun hashCode(): Int {
        return alpha3Code.hashCode()
    }

    fun getLanguage(): String {
        return if (languages.isNotEmpty()) {
            languages[0].name
        } else {
            "No Languages"
        }
    }

    fun getCurrency(): String {
        return if (currencies.isNotEmpty()) {
            currencies[0].name
        } else {
            "No Currencies"
        }
    }
}

data class LanguageModel(
        val name: String
)

data class CurrencyModel(
        val name: String
)

