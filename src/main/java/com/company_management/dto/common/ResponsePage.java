package com.company_management.dto.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class ResponsePage<T> extends PageImpl<T> {
    private static final long serialVersionUID = 1L;

    @JsonCreator
    public ResponsePage(@JsonProperty("content") final List<T> content,
                        @JsonProperty("totalElements") final long totalElements,
                        @JsonProperty("totalPages") final int totalPages,
                        @JsonProperty("page") final int page,
                        @JsonProperty("size") final int size,
                        @JsonProperty("sort") final List<String> sort) {
        this(content,
                RequestPage.builder()
                        .page(page)
                        .size(size)
                        .sort(sort)
                        .build(),
                totalElements);
    }

    public ResponsePage(final List<T> content, final RequestPage requestPage, final long totalElements) {
        super(content, requestPage.toPageable(), totalElements);
    }

    public int getPage() {
        return getNumber();
    }

    @JsonProperty("sort")
    public List<String> getSortList() {
        return getSort().stream().map(order -> order.getProperty() + "," + order.getDirection().name())
                .collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public Pageable getPageable() {
        return super.getPageable();
    }

    @Override
    @JsonIgnore
    public int getNumberOfElements() {
        return super.getNumberOfElements();
    }

    @Override
    @JsonIgnore
    public int getNumber() {
        return super.getNumber();
    }
}
