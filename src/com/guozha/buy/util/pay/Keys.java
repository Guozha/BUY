package com.guozha.buy.util.pay;

public class Keys {

	//支付宝支付相关
	
	//商户PID
	public static final String PARTNER = "2088611837939890";
	//商户收款账号
	public static final String SELLER = "1377178480@qq.com";
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ69DLNYN/akMaC4GNMhe2cuBx4sT5d8JKDqzW6uuCej8XQn3WvwWW3Iht4DjdYH6T/m+I3QWL10GLZaSS/0cXyf+XinMYs//TPBzqCehOnFlBX3jBm8pjqOxgUnbSOaqhoCLhLRJjPKlx1iD2fqa1/XUBN8sdDkdd+aEm/BQywPAgMBAAECgYEAkNj9b+G+H2ewiXGz5WSWEI7lm9jfP+HeIqBfmPIBxNpKh62g1M/bKtxihNBFatqJMvB2OiG1+BqqOiukwQeTTa82fAFoOIW8jJ/leoU/UEEx1o3MiMxcaYRj1QQnPiVrBF7F0VwKfJ0k2Ma1d/3qNKvxeZFGh52JZzZr/3R04SECQQDPkynNEBNLHA6ZLzzo5VPcv/Qe5Lg8DrlZQBc0z2AGN/kKyjiIbp1MVwoeOEGU9Ij0jUFJkgr8UE5/racCdT15AkEAw8VEllJuvpX7P3BVTkb1Sr0RgPhPnQLXg9a6S7T/tH7TlnAWLEa1h86taOKnTLajSNK0dkxDigzwwzBRvMG7xwJBAMPSL0MXATyAUX1NxQZpIf5E0AY8+1kdAtohzkKxyALV1wnI0CBVSet0yUTNCYF+V51tjMPdFKQHFqb4qW92LvECQBFMeh4KVKzUlenqJC+Rh5U+FbDewDIwkRiePcH40WuClXHCn8Lz0JCai6or9PqLXOxPahDV9lXZhODKgT57o/sCQGDHk7YRcso6kbfyduitpvug42bmVbmIO1tA9nmrYVXdnEGOusbsloJWPZX25zhpVLjkWrzPr3YCib/vZrWiKEM=";
	//支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//微信支付相关
	//APP_ID
	public static final String APP_ID = "wxc17fccd72a2dbea7";
	//商户号
	public static final String MCH_ID = "1231381501";
	//API密钥
	public static final String API_KEY = "hKpHiW4gGgNN7jZF7BxMiGIZvOrRrier";
	
	//支付宝回调地址
	public static final String REAL_ALI_PAY_URL = "http://120.24.211.45:9999/PAY_ALI/notify_url.jsp";
	public static final String TEST_ALI_PAY_URL = "http://120.24.220.86:9999/PAY_ALI/notify_url.jsp";
	
	//微信回调地址
	public static final String REAL_WX_PAY_URL = "http://120.24.211.45:9999/PAY_WECHAT/notify_url.jsp";
	public static final String TEST_WX_PAY_URL = "http://120.24.220.86:9999/PAY_WECHAT/notify_url.jsp";
}
