package com.eliangilsierra.mvcscaffold.mapper;

import com.eliangilsierra.mvcscaffold.dto.request.ProductRequestDto;
import com.eliangilsierra.mvcscaffold.dto.response.ProductResponseDto;
import com.eliangilsierra.mvcscaffold.entity.Product;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The request DTO only carries a categoryId, not a Category — resolving that
 * foreign key is the service's job (it has the repository), not the mapper's.
 * That's why "category" is ignored here and set explicitly in
 * CategoryServiceImpl/ProductServiceImpl after mapping.
 *
 * injectionStrategy = CONSTRUCTOR (rather than MapStruct's default field
 * injection) so the generated IProductMapperImpl takes ICategoryMapper as a
 * constructor argument — testable with `new IProductMapperImpl(new
 * ICategoryMapperImpl())`, no Spring context required.
 */
@Mapper(componentModel = "spring", uses = ICategoryMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDto dto);

    ProductResponseDto toResponseDto(Product entity);
}
