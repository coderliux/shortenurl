package com.aws.liujie.controllers;

import com.aws.liujie.service.UrlStorageService;
import com.aws.liujie.util.ResourceUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@Api(value = "UrlShortUrlController", tags = "Short URL API")
public class UrlShortenController {
    private static final Logger log = LoggerFactory.getLogger(UrlShortenController.class);

    @Autowired
    UrlStorageService urlStorageService;

    @GetMapping("/")
    @ApiIgnore
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/s")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 406, message = "Url not available")})
    public Response<String> generateShortUrl(@RequestParam(required = false) String originalUrl,
                                             HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (isEmpty(originalUrl)) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Your URL is not available");
            return null;
        }

        String resourceId = ResourceUtils.resourceId(originalUrl);
        String shortUrl = getRedirectUrl(request, resourceId);
        log.debug("{} short link is {} ", originalUrl, shortUrl);
        urlStorageService.storeUrl(resourceId, originalUrl);
        return new Response<String>("Success", "200", shortUrl);
    }

    private String getRedirectUrl(HttpServletRequest request, String resourceId) {
        return request.getRequestURL() + "/" + resourceId;
    }

    @GetMapping("/s/{code}")
    @ApiResponses(value = {
            @ApiResponse(code = 301, message = "Success"),
            @ApiResponse(code = 406, message = "Url not available"),
            @ApiResponse(code = 404, message = "Url not existed")})
    public void redirectToOriginalLink(@PathVariable String code, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (isEmpty(code)) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "URL is not available");
            return;
        }

        String url = urlStorageService.getUrl(code);
        if (isEmpty(url)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL is not found");
            return;
        }
        //support Chinese
        String encoding = request.getCharacterEncoding();
        url = new String(url.getBytes(encoding), StandardCharsets.ISO_8859_1);

        log.debug("Redirect to {} ", url);
        response.setHeader("Location", url);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    }

    private boolean isEmpty(@PathVariable String code) {
        return code == null || code.trim().length() == 0;
    }

}
