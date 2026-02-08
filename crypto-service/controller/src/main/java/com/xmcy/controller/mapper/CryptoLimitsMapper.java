package com.xmcy.controller.mapper;

import com.xmcy.model.ApiCryptoLimits;
import com.xmcy.service.model.CryptoLimits;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CryptoModelMapper.class)
public interface CryptoLimitsMapper {
    CryptoLimitsMapper INSTANCE = Mappers.getMapper(CryptoLimitsMapper.class);

    /**
     * @param cryptoLimits model to be converted
     * @return {@link ApiCryptoLimits} api model
     */
    ApiCryptoLimits convert(CryptoLimits cryptoLimits);
}
