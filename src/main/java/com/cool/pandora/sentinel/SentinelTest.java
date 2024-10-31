package com.cool.pandora.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.google.common.collect.Lists;

import java.util.List;

public class SentinelTest {
    public static void main(String[] args) {
        //配置规则
        initFlowRule();

        while (true) {
            //获取资源
            Entry entry = null;
            try {
                entry = SphU.entry("HelloWorld");
                //执行业务逻辑
                System.out.println("HelloWorld");
            } catch (BlockException e1) {
                //处理被流控的逻辑
                System.out.println("block!");
            }
        }
    }

    private static void initFlowRule() {
        List<FlowRule> rules = Lists.newArrayList();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(20);
        rules.add(rule);
        //加载配置好的规则
        FlowRuleManager.loadRules(rules);
    }
}