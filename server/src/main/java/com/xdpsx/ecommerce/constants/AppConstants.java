package com.xdpsx.ecommerce.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AppConstants {

    public final static Set<String> IMG_CONTENT_TYPES = new HashSet<>(Arrays.asList("image/png", "image/jpeg"));

    public final static int NUMBER_PRODUCT_IMAGES = 5;
    public final static int PRODUCT_IMG_WIDTH = 300;
    public final static String PRODUCT_IMG_FOLDER = "products";

    public final static int VENDOR_IMG_WIDTH = 320;
    public final static String VENDOR_IMG_FOLDER = "vendors";

    public final static int MIN_ITEMS_PER_PAGE = 5;
    public final static int MAX_ITEMS_PER_PAGE = 20;
    public final static String DEFAULT_SORT_FIELD = "-date";

}
