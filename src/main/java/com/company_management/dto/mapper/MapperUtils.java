package com.company_management.dto.mapper;

import lombok.experimental.UtilityClass;
import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class MapperUtils {
    public static <S,D> D map(S source, Class<D> destionation) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(source, destionation);
    }

    public static <S,D> D map(S source, Class<D> destionation, PropertyMap<S, D> propertyMap) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(propertyMap);
        return modelMapper.map(source, destionation);
    }

    public static <S,D> void map(S source, D destionation) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(source, destionation);
    }

    public static <S,D> void map(S source, D destionation, PropertyMap<S, D> propertyMap) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(propertyMap);
        modelMapper.map(source, destionation);
    }

    public static <S,D> void mapOnlyNotNullProperty(S source, D destionation) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(source, destionation);
    }

    public static <S,D> void mapOnlyNotNullProperty(S source, D destionation, PropertyMap<S, D> propertyMap) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setPropertyCondition(Conditions.isNotNull());
        modelMapper.addMappings(propertyMap);
        modelMapper.map(source, destionation);
    }

    public static <S, D> void mapOnlyNotNullPropertyIgnoringId(S source, D destination) {
        Field[] fields = source.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(source);
                if (value != null && !"id".equals(field.getName())) {
                    Field targetField = destination.getClass().getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    targetField.set(destination, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public static <S, D> void mapIfNotNull(S source, D destination) {
        if (source != null && destination != null) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT)
                    .setPropertyCondition(new Condition<Object, Object>() {
                        @Override
                        public boolean applies(MappingContext<Object, Object> context) {
                            return context.getSource() != null;
                        }
                    });

            modelMapper.map(source, destination);
        }
    }

    public static <T, K> Map<K,T> buildMap(List<T> list, Function<T,K> keyMapper) {
        return list.stream()
                .filter(Objects::nonNull)
                .filter(item -> keyMapper.apply(item) != null)
                .collect(Collectors.toMap(keyMapper,Function.identity(),(existing, replacement) -> existing));
    }

    public static <T,K,V> Map<K,V> buildMap(List<T> list, Function<T,K> keyMapper, Function<T, V> valueMapper) {
        return list.stream()
                .filter(Objects::nonNull)
                .filter(item -> keyMapper.apply(item) != null)
                .collect(Collectors.toMap(keyMapper,valueMapper,(existing, replacement) -> existing));
    }
}
