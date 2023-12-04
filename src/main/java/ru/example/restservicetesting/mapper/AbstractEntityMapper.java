package ru.example.restservicetesting.mapper;

import org.mapstruct.*;

import java.util.List;
import java.util.Set;

public interface AbstractEntityMapper<D, E> {

    @Named(value = "toDto")
    D toDto(E entity);

    @Named(value = "toEntity")
    E toEntity(D dto);

    @IterableMapping(qualifiedByName = "toDto")
    List<D> toDto(List<E> entityList);

    @IterableMapping(qualifiedByName = "toEntity")
    List<E> toEntity(List<D> dtoList);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget E entity, D dto);

    @Named("toDtoSet")
    @IterableMapping(qualifiedByName = "toDto")
    Set<D> toDtoSet(Set<E> entity);

    @Named("toEntitySet")
    @IterableMapping(qualifiedByName = "toEntity")
    Set<E> toEntitySet(Set<D> entity);
}
