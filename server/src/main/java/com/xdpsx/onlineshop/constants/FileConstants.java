package com.xdpsx.onlineshop.constants;

import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileConstants {
    public final static Set<String> IMG_TYPES = new HashSet<>(Arrays.asList(
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE
    ));

    public final static int BRAND_IMG_WIDTH = 320;
    public final static String BRAND_IMG_FOLDER = "brands";
}
