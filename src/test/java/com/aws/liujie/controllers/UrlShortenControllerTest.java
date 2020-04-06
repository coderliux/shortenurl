package com.aws.liujie.controllers;

import com.aws.liujie.service.UrlStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UrlShortenControllerTest {

    @Mock
    UrlStorageService urlStorageService;

    @InjectMocks
    private UrlShortenController shortenController;

    @Test
    public void shouldReturnIndexViewName() {
        ModelAndView modelAndView = shortenController.index();
        assertThat(modelAndView.getViewName(), is("index"));
    }

    @Test
    public void shouldReturn406ResponseCodeWithEmptyUrl() throws IOException {
        String originalUrl = null;
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Response<String> result = shortenController.generateShortUrl(originalUrl, request, response);
        assertThat(response.getStatus(), is(HttpServletResponse.SC_NOT_ACCEPTABLE));
        assertNull(result);
    }

    @Test
    public void shouldReturnShortenUrl() throws IOException {
        String originalUrl = "http://www.test.com/test/test";
        HttpServletRequest request = new MockHttpServletRequest("GET", "/shorten");

        HttpServletResponse response = new MockHttpServletResponse();
        Response<String> result = shortenController.generateShortUrl(originalUrl, request, response);
        assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
        assertThat(result.getStatus(), is("200"));
        assertThat(result.getMessage(), is("Success"));
        String data = result.getData();
        assertNotNull(data);
        assertTrue(data.matches("http://localhost/shorten/\\w+"));
        String resourceId = data.substring(data.lastIndexOf("/") + 1);
        verify(urlStorageService, times(1)).storeUrl(resourceId, originalUrl);
    }


    @Test
    public void shouldReturnResponse406WithResourceId() throws IOException {
        String resourceId = null;
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        shortenController.redirectToOriginalLink(resourceId, request, response);

        assertThat(response.getStatus(), is(HttpServletResponse.SC_NOT_ACCEPTABLE));
    }

    @Test
    public void shouldReturnResponse404WhenResourceNotExist() throws IOException {
        String resourceId = "resourceId";
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        when(urlStorageService.getUrl(resourceId)).thenReturn(null);
        shortenController.redirectToOriginalLink(resourceId, request, response);
        assertThat(response.getStatus(), is(HttpServletResponse.SC_NOT_FOUND));
    }

    @Test
    public void shouldReturnResponse301AndRedirect() throws IOException {
        String resourceId = "resourceId";
        HttpServletRequest request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        HttpServletResponse response = new MockHttpServletResponse();

        String originUrl = "http://www.test.com";
        when(urlStorageService.getUrl(resourceId)).thenReturn(originUrl);
        shortenController.redirectToOriginalLink(resourceId, request, response);
        assertThat(response.getStatus(), is(HttpServletResponse.SC_MOVED_PERMANENTLY));
        assertThat(response.getHeader("Location"), is(originUrl));
    }

}