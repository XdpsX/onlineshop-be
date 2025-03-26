package com.xdpsx.onlineshop.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlugConverterTest {

    @Test
    public void testToSlug() {
        String[][] testCases = {
                {"Chào mừng bạn đến với lập trình Java!", "chao-mung-ban-den-voi-lap-trinh-java"},
                {"Đẹp trai không bằng chai mặt!", "dep-trai-khong-bang-chai-mat"},
                {"Hà Nội - Thủ đô ngàn năm văn hiến", "ha-noi-thu-do-ngan-nam-van-hien"},
                {"Café và bánh mì", "cafe-va-banh-mi"},
                {"Lập trình viên yêu thích công nghệ!", "lap-trinh-vien-yeu-thich-cong-nghe"},
                {"Mùa hè xanh", "mua-he-xanh"},
                {"Tôi yêu Việt Nam!", "toi-yeu-viet-nam"},
                {"Đường phố Sài Gòn", "duong-pho-sai-gon"},
                {"Chương trình học tiếng Việt", "chuong-trinh-hoc-tieng-viet"},
                {"Sống chậm lại, yêu thương nhiều hơn", "song-cham-lai-yeu-thuong-nhieu-hon"}
        };

        for (String[] testCase : testCases) {
            String input = testCase[0];
            String expectedSlug = testCase[1];
            assertEquals(expectedSlug, SlugConverter.toSlug(input));
        }
    }
}
