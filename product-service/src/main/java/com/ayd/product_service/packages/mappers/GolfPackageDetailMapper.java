package com.ayd.product_service.packages.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ayd.product_service.packages.dtos.GolfPackageDetailRequestDTO;

import com.ayd.product_service.packages.models.GolfPackageDetail;
import com.ayd.product_service.product.mappers.ProductMapper;
import com.ayd.sharedProductService.packages.dtos.GolfPackageDetailResponseDTO;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface GolfPackageDetailMapper {

    @Mapping(target = "product.id", source = "product")
    public GolfPackageDetail fromGolfPackageDetailRequestDToToGolfPackageDetail(GolfPackageDetailRequestDTO dto);

    public List<GolfPackageDetail> fromList_GolfPackageDetailToList_GolfPackageDetailRequestDTO(
            List<GolfPackageDetailRequestDTO> dtos);

    public GolfPackageDetailResponseDTO fromGolfPackageDetailToGolfPackageDetailResponseDTO(
            GolfPackageDetail golfPackageDetail);
}
