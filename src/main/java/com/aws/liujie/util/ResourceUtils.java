package com.aws.liujie.util;

import com.google.common.hash.Hashing;

public class ResourceUtils {
    public static String resourceId(String url) {
        return Hashing.murmur3_32().hashBytes(url.getBytes()).toString();
    }

}
