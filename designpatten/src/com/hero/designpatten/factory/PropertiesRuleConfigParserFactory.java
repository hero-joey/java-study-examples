package com.hero.designpatten.factory;

/**
 * @description: PropertiesRuleConfigParserFactory
 * @date: 2021/3/3 10:49
 * @author: maccura
 * @version: 1.0
 */
public class PropertiesRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new PropertiesRuleConfigParser();
    }
}
