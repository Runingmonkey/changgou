import com.changgou.util.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/1 20:45
 */
public class HttpClientTest {

    @Test
    public void testDemo() throws IOException {
        HttpClient httpClient = new HttpClient("https://fanyi.baidu.com/");
        httpClient.get();
        String content = httpClient.getContent();
        System.out.println(content);
    }


    /**
     * @description: 微信sdk 测试
     * @author: QIXIANG LING
     * @date: 2020/8/1 20:56
     * @param:
     * @return: void
     */
    @Test
    public void wxSdkTest() throws Exception {
        String s = WXPayUtil.generateNonceStr();
        System.out.println("随机生成的字符串: " + s);
        Map<String, String> map = new HashMap<>();
        map.put("花鸟玲爱","对酒当歌，人生几何！譬如朝露，去日苦多。");
        map.put("夏海里伽子","千古兴亡何限错，百年生死本来齐");
        map.put("望月理奈","东风夜放花千树，更吹落，星如雨。");
        String source = WXPayUtil.generateSignedXml(map, "source");
        System.out.println(source);
    }

}
