package cn.com.servyou.hrbase.dao.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * AccountIdUtils
 *
 * @author zhucj
 * @since 20230824
 */
@UtilityClass
public class AccountIdUtils {

    public static String getAccountId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}