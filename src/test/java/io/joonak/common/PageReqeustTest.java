package io.joonak.common;

import io.joonak.common.domain.PageRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class PageReqeustTest {

    @Test
    @DisplayName("setPage() 메서드 테스트")
    public void setPageTest() {
        var pageRequest = new PageRequest();

        pageRequest.setPage(10);
        assertThat(pageRequest.getPage(), is(10));

        pageRequest.setPage(0); // page가 0일경우 1로 설정
        assertThat(pageRequest.getPage(), is(1));
    }

    @Test
    @DisplayName("setSize() 메서드 테스트")
    public void setSizeTest() {
        var pageRequest = new PageRequest();

        pageRequest.setSize(20);
        assertThat(pageRequest.getSize(), is(20));

        pageRequest.setSize(Integer.MAX_VALUE); // 설정된 MAX_SIZE보다 큰 수가 들어오는경우 DEFAULT_SIZE로 설정
        assertThat(pageRequest.getSize(), is(10));
    }

    @Test
    @DisplayName("setDirection() 메서드 테스트")
    public void setDirectionTest() {
        var pageRequest = new PageRequest();

        pageRequest.setDirection(Sort.Direction.ASC);
        assertThat(pageRequest.getDirection(), is(Sort.Direction.ASC));
    }

    @Test
    @DisplayName("of() 메서드 정상 테스트")
    public void ofTest() {
        var pageRequest = new PageRequest();
        // 정상적인 요청
        pageRequest.setPage(1);
        pageRequest.setSize(20);
        pageRequest.setDirection(Sort.Direction.ASC);

        org.springframework.data.domain.PageRequest request = pageRequest.of();

        assertThat(request.getPageNumber(), is(0));
        assertThat(request.getPageSize(), is(20));
        assertThat(request.getSort(), is(Sort.by(Sort.Direction.ASC, "createdAt")));
    }

    @Test
    @DisplayName("of() 메서드 비정상 테스트")
    public void IllegalOfTest() {
        var pageRequest = new PageRequest();
        // 비정상적인 요청
        pageRequest.setPage(-10);
        pageRequest.setSize(Integer.MAX_VALUE);

        org.springframework.data.domain.PageRequest request = pageRequest.of();

        assertThat(request.getPageNumber(), is(0));
        assertThat(request.getPageSize(), is(10));
        assertThat(request.getSort(), is(Sort.unsorted()));
    }

}
