package com.boot.demo.auto;

import cn.hutool.core.map.MapUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * HutoolTest
 *
 * @author zhucj
 * @since 20220825
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HutoolTest {

    @Test
    public void testMapProxy1() {
        Map<String, Object> userMap = MapUtil.newHashMap(16);
        userMap.put("username", "alvin");
        userMap.put("age", 20);

        // 使用map的时候, 需要进行强转，一旦类型错误，会报错
        String age = (String) userMap.get("age");
    }
}