package com.bigbro.wwooss.v1.utils;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

public class PagingUtil {

    public static  <T> Slice<T> getSlice(JPAQuery<T> query, Pageable pageable) {
        final int pageSize = pageable.getPageSize();
        List<T> content = query
                .offset(pageable.getOffset())
                .limit(pageSize + 1) // 요구 개수의 + 1을 해서 hasNext를 판단
                .fetch();

        boolean hasNext = false;

        // pageSize + 1 결과가 가져온 데이터 보다 크면
        // hasNext 있다고 판단 및 마지막 1개 데이터는 삭제
        if (content.size() > pageSize) {
            content = content.subList(0, pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    public static  <T> Page<T> getPage(JPAQuery<T> query, Pageable pageable) {
        final int pageSize = pageable.getPageSize();
        List<T> content = query
                .offset(pageable. getOffset())
                .limit(pageSize + 1)
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
