package com.changgou;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.changgou.controller.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mike ling
 * @description
 * @date 2021/6/10 14:21
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class SentinelApplication {
    public static void main(String[] args) {
        SpringApplication.run(SentinelApplication.class);
        initFlowQpsRule();
    }

    // 定义限流规则

    private static void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule(UserController.RESOURCE_NAME);
        // set limit qps to 2
        rule.setCount(2);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);


        FlowRule rule2 = new FlowRule(UserController.RESOURCE_NAME_QUERY_USER_BY_ID);
        rule2.setCount(2);
        rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule2.setLimitApp("default");
        rules.add(rule2);

        FlowRuleManager.loadRules(rules);
    }

}
