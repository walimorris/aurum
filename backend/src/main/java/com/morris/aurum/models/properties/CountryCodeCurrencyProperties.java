package com.morris.aurum.models.properties;

import com.morris.aurum.models.types.CountryCode;
import com.morris.aurum.models.types.CurrencyType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("currency")
public record CountryCodeCurrencyProperties(Map<CountryCode, CurrencyType> codes) {
}
