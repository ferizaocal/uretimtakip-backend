package com.productiontracking.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ModelMapperServiceImpl implements ModelMapperService {

    private ModelMapper _vModelMapper;

    @Override
    public ModelMapper forResponse() {
        this._vModelMapper
                .getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return this._vModelMapper;

    }

    @Override
    public ModelMapper forRequest() {
        this._vModelMapper
                .getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return this._vModelMapper;
    }

}
