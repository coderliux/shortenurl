package com.aws.liujie.service;


public interface UrlStorageService {
    boolean storeUrl(String resourceId, String url);

    String getUrl(String resourceId);
}
