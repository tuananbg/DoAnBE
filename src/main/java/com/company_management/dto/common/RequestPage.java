package com.company_management.dto.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestPage {
    @QueryParam("page")
    @DefaultValue("0")
    @Min(0)
    private int page = 0;

    @QueryParam("size")
    @DefaultValue("25")
    @Positive
    private int size = 25;

    @QueryParam("sort")
    private List<String> sort;

    public Pageable toPageable() {
        if (CollectionUtils.isEmpty(sort)) {
            return PageRequest.of(page, size);
        }
        return PageRequest.of(page, size,
                Sort.by(sort.stream()
                        .map(el -> el.split("/"))
                        .map(ar -> new Sort.Order(Sort.Direction.fromString(ar[1]), ar[0]))
                        .collect(Collectors.toList())
                )
        );
    }

    public boolean isDefaultSort() {
        return CollectionUtils.isEmpty(sort);
    }


}
