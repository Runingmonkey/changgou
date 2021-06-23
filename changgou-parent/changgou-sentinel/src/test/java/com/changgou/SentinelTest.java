package com.changgou;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mike ling
 * @description
 * @date 2021/6/10 14:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SentinelApplication.class)
public class SentinelTest {

    @Test
    public void testSentinelBlock() {
        List<FlowRule> flowRules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();
        flowRule.setCount(2);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setResource("Hello");
        flowRules.add(flowRule);
        FlowRuleManager.loadRules(flowRules);
    }

}
