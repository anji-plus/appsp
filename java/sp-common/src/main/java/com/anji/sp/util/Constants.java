package com.anji.sp.util;

/**
 * 通用常量信息
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 版本用户缓存列表 redis key
     */
    public static final String APP_VERSION_KEYS = "version_keys:";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 保存token原始值的key前缀
     */
    public static final String LOGIN_USER_TOKEN_UUID_KEY = "LOGIN_USER_TOKEN_UUID_KEY";

    /**
     * app  public_key
     */
    public static final String APP_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDf2XdSpvtuC1xeRzP8VOhoF4CRrfOk0ZbxlYniauPWmQqNeDP1IMqoXyJ72FEmmFh6crNSKdGwqyy9ifRuiCPbQ0UhKMsOe7F/06FECoUH3NYipVIHgY4KYpHrIYo9uw8xTCWjUh9iz3Kv5yhulzHR+ORpNQ460xTki57yd6LA/wIDAQAB";

    /**
     * app  private_key
     */
    public static final String APP_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAN/Zd1Km+24LXF5HM/xU6GgXgJGt86TRlvGVieJq49aZCo14M/UgyqhfInvYUSaYWHpys1Ip0bCrLL2J9G6II9tDRSEoyw57sX/ToUQKhQfc1iKlUgeBjgpikeshij27DzFMJaNSH2LPcq/nKG6XMdH45Gk1DjrTFOSLnvJ3osD/AgMBAAECgYA9vMGPFyTDLfj0u5iX4GcUxl6cTyiibXe++2pC+w4Jkr35VIyn77pVA6AEHh4LkfrdEqNoZsZBfoRhZhn3Hi8c9sh9WEuPx46/L+geNFkuMBn1UefaaWrMtZbI39JBR/OTnqFYjEfu44RmSslCdFoHSBlqkWRyAJfA/+2q8xj+KQJBAP02jRu7icxc67+6WDy3QR/vCzxN9oTYf/5diYDfbcUj0jfVp7BDYYxn5gr7eAJCB2abeh5c0pY0VaHxBv0L0ZsCQQDiUC402K1jZNsGspYjYvpmu06XjcGu+nw8o8hYQ1lBfw6oYAb/FsejuvQFdB2ioCvB7yh3bXJOq2x1ThRCEKZtAkAG4ZWiHE2ZXSkW88R0GfLOIFvozqGVaKDJ09bpxqigA+IxnD/LXXloLGanA8B2Jz82PaFq89DRPijZlsg+1jRjAkADA8Bp0c0Vet10DGL1m7bsMWiNmkTPOOT4xdHAYz1IIxIl6gmN0lbcdnwTqoVOikWq8q2eUaXpKF1sfprEeoZNAkEAilN5XEZyTliKm0tmWgXq/OYJcwVXYUd8dcMT8t3692MlgTafbUSFVjDjSBe+k5t3cESNdbui38a/ngliNHskJA==";

}
