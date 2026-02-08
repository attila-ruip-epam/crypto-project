package com.xmcy.controller.mapper;

import com.xmcy.model.ApiCryptoRange;
import com.xmcy.service.model.CryptoAndNormalizedRange;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CryptoAndNormalizedRangeMapper {
    CryptoAndNormalizedRangeMapper INSTANCE = Mappers.getMapper(CryptoAndNormalizedRangeMapper.class);

    /**
     * @param model to be converted
     * @return {@link ApiCryptoRange} api model
     */
    ApiCryptoRange convert(CryptoAndNormalizedRange model);
}
