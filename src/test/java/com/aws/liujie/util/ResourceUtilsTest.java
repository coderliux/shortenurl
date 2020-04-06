package com.aws.liujie.util;

import com.google.common.hash.Hashing;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResourceUtilsTest {

    @Test
    public void shouldGenerateTheSameCode() {
        String input = "hello, world";
        String encodeSting = Hashing.murmur3_32().hashBytes(input.getBytes()).toString();
        assertEquals(encodeSting, ResourceUtils.resourceId(input));
    }


}