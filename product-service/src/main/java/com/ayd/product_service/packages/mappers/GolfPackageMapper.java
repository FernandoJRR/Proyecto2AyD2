package com.ayd.product_service.packages.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.product_service.packages.dtos.GolfPackageResponseDTO;
import com.ayd.product_service.packages.dtos.SaveGolfPackageRequestDTO;
import com.ayd.product_service.packages.models.GolfPackage;

@Mapper(componentModel = "spring", uses = GolfPackageDetailMapper.class)
public interface GolfPackageMapper {

    public GolfPackage fromCreateDtoToPackage(SaveGolfPackageRequestDTO dto);

    public GolfPackageResponseDTO fromGolfPackageToGolfPackageResponseDTO(GolfPackage dto);

    public List<GolfPackageResponseDTO> fromList_GolfPackageToList_GolfPackageResponseDTO(List<GolfPackage> dto);
}
