/**
 * 版权声明：中图一购网络科技有限公司 版权所有 违者必究 2012 
 * 日    期：12-2-29
 */
package com.rop.sample;

import com.rop.utils.RopUtils;
import com.rop.validation.MainErrorType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertTrue;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @author 陈雄华
 * @version 1.0
 */
public class UserRestServiceClient {

    private String sessionId;
    /**
     * 创建一个服务端的会话
     */
    @BeforeMethod
//    @Test
    public void createSession(){
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.getSession");
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("locale", "en");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("<logonResponse sessionId=\"mockSessionId1\"/>") > -1);
    }
    
    /**
     * 在一切正确的情况下，返回正确的服务报文 (user.add + 1.0）
     */
    @Test
    public void testAddUserByVersion1() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("locale", "en");
        form.add("userName", "tomson");
        form.add("salary", "2,500.00");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("<createUserResponse createTime=\"20120101010101\" userId=\"1\">") > -1);
    }
    
    
    /**
     * 显式指定返回的报文类型，在配置文件中已经显式指定了 报文格式参数的名称为messageFormat
     */
    @Test
    public void testAddUserWithJsonFormat() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("locale", "en");
        form.add("messageFormat", "json");
        form.add("userName", "tomson");
        form.add("salary", "2,500.00");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);
        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("{\"createUserResponse\":{\"userId\":\"1\",") > -1);
    }

    /**
     * 在一切正确的情况下，返回正确的服务报文 (user.add + 1.0）
     */
    @Test
    public void testAddUserByVersion2() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "2.0");
        form.add("sessionId", "mockSessionId1");
        form.add("locale", "en");
        form.add("userName", "tomson");
        form.add("salary", "2,500.00");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("<createUserResponse createTime=\"20120101010102\"") > -1);
    }

    /**
     * 在一切正确的情况下，返回正确的服务报文 (user.add + 1.0）
     */
    @Test
    public void testQueryUserByUserId() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> form = new HashMap<String, String>();

        form.put("method", "user.query");//<--指定方法名称
        form.put("appKey", "00001");
        form.put("v", "1.0");
        form.put("sessionId", "mockSessionId1");
        form.put("locale", "en");
        form.put("userId", "userId1");


        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form, "abcdeabcdeabcdeabcdeabcde");
        form.put("sign", sign);

        //使用GET获取：正确返回
        String response = restTemplate.getForObject(
                "http://localhost:8088/router" +
                        "?method={method}&appKey={appKey}&v={v}&sessionId={sessionId}&locale={locale}" +
                        "&userId={userId}&sign={sign}",
                String.class, form);
        System.out.println("response:" + response);
        assertTrue(response.indexOf("user.query") > -1 && response.indexOf("userId1") > -1);
    }

    /**
     * 测试自定义的类型转换器{@link com.rop.sample.request.TelephoneConverter}
     */
    @Test
    public void testCustomConverter() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.customConverter");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("locale", "en");
        form.add("userName", "tomson");
        form.add("salary", "2,500.00");
        form.add("telephone", "0592-12345678");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("0592#12345678") > -1);
    }

    /**
     * 验证内部格式为XML的请求参数会正确绑定到RopRequest的内部属性对象中，参见{@link com.rop.sample.request.CreateUserRequest#address}
     *
     * @see com.rop.sample.request.Address
     */
    @Test
    public void testServiceXmlRequestAttr() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("userName", "tomson");
        form.add("salary", "2,500.00");

        //address会正确绑定
        form.add("address",
                "<address zoneCode=\"0001\" doorCode=\"002\">\n" +
                        "  <streets>\n" +
                        "    <street no=\"001\" name=\"street1\"/>\n" +
                        "    <street no=\"002\" name=\"street2\"/>\n" +
                        "  </streets>\n" +
                        "</address>");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("<createUserResponse createTime=\"20120101010101\" userId=\"1\">") > -1);
    }

    /**
     * 由于{@link com.rop.sample.request.CreateUserRequest#password}标注了{@link com.rop.annotation.IgnoreSign},所以Rop
     * 会忽略对password请求参数进行签名验证。
     */
    @Test
    public void testIngoreSignParamOfPassword() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("userName", "tomsony");
        form.add("salary", "2,500.00");
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");

        form.add("sign", sign);
        form.add("password", "123456"); //password无需签名，所以放在最后

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("<createUserResponse createTime=\"20120101010101\" userId=\"1\">") > -1);
    }

    /**
     * 由于{@link com.rop.sample.request.CreateUserRequest#password}标注了{@link com.rop.annotation.IgnoreSign},所以Rop
     * 会忽略对password请求参数进行签名验证。
     */
    @Test
    public void testIngoreSession() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("appKey", "00001");
        form.add("method", "user.add");//<--指定方法名称
        form.add("v", "4.0");
        form.add("userName", "tomsony");
        form.add("salary", "2,500.00");
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");

        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("userId=\"4\"") > -1);
    }

    /**
     * 请求参数违反合法性校验限制时，返回相应的错误报文
     * 服务端限制salary的格式必须是“#,###.##”，且必须在1000.00和100000.00之间，
     * 参见{@link com.rop.sample.request.CreateUserRequest#salary}
     */
    @Test
    public void testParamConstrainViolation() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("userName", "tomsony");
        form.add("salary", "100");
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("isv.invalid-paramete:salary") > -1);
    }

    /**
     * 在类路径的rop.appSecret.properties中我们定义了两个合法的appKey,分别是：
     * 00001=abcdeabcdeabcdeabcdeabcde
     * 00002=abcdeabcdeabcdeabcdeaaaaa
     * 我们使用一个错误的appKey以验证是否会返回正确的错误报文。
     */
    @Test
    public void testInvalidSysParams() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> form = new HashMap<String, String>();
        form.put("method", "user.get");//<--指定方法名称
        form.put("appKey", "00001");
        form.put("v", "1.0");
        form.put("sessionId", "mockSessionId1");
        form.put("locale", "en");
        form.put("userName", "tomson");
        form.put("salary", "2,500.00");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form, "abcdeabcdeabcdeabcdeabcde");
        form.put("sign", sign);


        //使用GET获取：正确返回
        String response = restTemplate.getForObject(
                "http://localhost:8088/router" +
                        "?appKey={appKey}&locale={locale}&method={method}&salary={salary}&sessionId={sessionId}" +
                        "&userName={userName}&v={v}&sign={sign}",
                String.class, form);
        System.out.println("response:" + response);
    }

    /**
     * 请求参数违反合法性校验限制时，返回相应的错误报文
     * 服务端限制salary的格式必须是“#,###.##”，且必须在1000.00和100000.00之间，
     * 参见{@link com.rop.sample.request.CreateUserRequest#salary}
     */
    @Test
    public void testInvalidSign() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("userName", "tomson");
        form.add("salary", "2,500.00");

        //设置一个错误的签名
        String sign = "xxx";
        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);

        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("code=\"" + MainErrorType.INVALID_SIGNATURE.value() + "\"") > -1);
    }

    /**
     * 在{@link SampleAppSecretManager}中，我们模拟让sessionId为mockSessionId1拥有访问
     * {@link UserRestService#addUser(com.rop.sample.request.CreateUserRequest)}的权限，而sessionId为mockSessionId2时则没有这个权限。
     * 将返回会话错误的响应报文。
     */
    @Test
    public void testInvalidSessionId() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");
        form.add("appKey", "00001");
        form.add("v", "1.0");

        form.add("sessionId", "mockSessionId2"); //<--模拟一个错误的sessionId

        form.add("userName", "tomson");
        form.add("salary", "2,500.00");

        //设置一个错误的签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);
        ;

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);

        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("code=\"" + MainErrorType.INSUFFICIENT_USER_PERMISSIONS.value() + "\"") > -1);
    }

    /**
     * 在{@link SampleSessionChecker}中，我们模拟了一个合法的sessionId,即 mockSessionId1，使用其它的sessionId，
     * 将返回会话错误的响应报文。
     */
    @Test
    public void testViolationSecurityManager() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");
        form.add("appKey", "00001");
        form.add("v", "1.0");

        form.add("sessionId", "xxxx"); //<--模拟一个错误的sessionId

        form.add("userName", "tomsony");
        form.add("salary", "2,500.00");

        //设置一个错误的签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);
        ;

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);

        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("code=\"" + MainErrorType.INVALID_SESSION.value() + "\"") > -1);
    }

    /**
     * “jhon”是{@link UserRestService#addUser(com.rop.sample.request.CreateUserRequest)} 服务方法预留的用户名，
     * 验证其会返回正确的业务错误码。
     */
    @Test
    public void testServiceError() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("userName", "jhon");
        form.add("salary", "2,500.00");

        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");

        form.add("sign", sign);
        form.add("password", "123456"); //password无需签名，所以放在最后

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);

        //返回业务的错误报文
        assertTrue(response.indexOf("isv.user-add-service-error:USER_NAME_RESERVED") > 0);
    }

    /**
     * 测试被拦截器阻截服务的情况 (user.add + 1.0）
     */
    @Test
    public void testAddUserWithInterceptorViolidateReservedUserName() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "2.0");
        form.add("sessionId", "mockSessionId1");
        form.add("locale", "en");
        form.add("userName", "jhonson");//这个用户名会被ReservedUserNameInterceptor拦截器驳回
        form.add("salary", "2,500.00");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("the userName can't be jhonson!") > -1);
    }

    /**
     * user.timeout 服务方法必须在1秒内执行完成，我们在服务端故意让user.timeout过期，看是否会返回正确的错误报文
     */
    @Test
    public void testServiceWhenExceedTimeout() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.timeout");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("locale", "en");
        form.add("userName", "tomson");
        form.add("salary", "2,500.00");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);


        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:" + response);
        assertTrue(response.indexOf("isp.remote-service-timeout") > -1);
    }

    /**
     * user.get 获取用户的信息
     */
    @Test
    public void testGetUserWithGET() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> form = new HashMap<String, String>();

        form.put("method", "user.get");//<--指定方法名称
        form.put("appKey", "00001");
        form.put("v", "1.0");
        form.put("sessionId", "mockSessionId1");
        form.put("locale", "en");
        form.put("userName", "tomson");
        form.put("salary", "2,500.00");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form, "abcdeabcdeabcdeabcdeabcde");
        form.put("sign", sign);

        //使用GET获取：正确返回
        String response = restTemplate.getForObject(
                "http://localhost:8088/router" +
                        "?method={method}&appKey={appKey}&v={v}&sessionId={sessionId}&locale={locale}" +
                        "&userName={userName}&salary={salary}&sign={sign}",
                String.class, form);
        System.out.println("response:" + response);
        assertTrue(response.indexOf("user.get") > -1);
    }

    /**
     * user.get 获取用户的信息
     */
    @Test
    public void testGetUserWithPOST() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.get");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("locale", "en");
        form.add("userName", "tomson");
        form.add("salary", "2,500.00");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);

        String response = restTemplate.postForObject(
                "http://localhost:8088/router", form, String.class);
        System.out.println("response:\n" + response);
        assertTrue(response.indexOf("code=\"5\"") > -1);
    }


    /**
     * 测试多次执行的服务性能: 100次调用，在1秒内完成！
     */
    @Test(timeOut = 2000)
    public void testMultiTimeRun() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("method", "user.add");//<--指定方法名称
        form.add("appKey", "00001");
        form.add("v", "1.0");
        form.add("sessionId", "mockSessionId1");
        form.add("locale", "en");
        form.add("userName", "tomson");
        form.add("salary", "2,500.00");

        //对请求参数列表进行签名
        String sign = RopUtils.sign(new ArrayList<String>(
                form.keySet()), form.toSingleValueMap(), "abcdeabcdeabcdeabcdeabcde");
        form.add("sign", sign);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String response = restTemplate.postForObject(
                    "http://localhost:8088/router", form, String.class);
            assertTrue(response.indexOf("<createUserResponse createTime=\"20120101010101\" userId=\"1\">") > -1);
        }
        System.out.println("time elapsed:" + (System.currentTimeMillis() - begin));
    }


}

