package com.xmcy.controller.mapper;

import com.xmcy.model.ApiCrypto;
import com.xmcy.service.model.CryptoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Mapper
public interface CryptoModelMapper {

    /**
     * @param cryptoModel to be converted
     * @return {@link ApiCrypto} api model
     */
    @Mapping(target = "date", source = "localDateTime", qualifiedByName = "convertDateTime")
    ApiCrypto convert(CryptoModel cryptoModel);

    @Named("convertDateTime")
    default LocalDate convertDateTime(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime)
            .map(LocalDateTime::toLocalDate)
            .orElse(null);
    }
}
