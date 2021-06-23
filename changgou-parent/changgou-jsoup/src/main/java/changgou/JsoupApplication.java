package changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author mike ling
 * @description
 * @date 2021/6/18 11:30
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableEurekaClient
public class JsoupApplication {
    public static void main(String[] args) {
        SpringApplication.run(JsoupApplication.class);
    }
}
