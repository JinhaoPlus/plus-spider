package top.jinhaoplus.parser;

import top.jinhaoplus.config.Config;

public class DefaultParserFactory implements ParserFactory {
    @Override
    public Parser newInstance(Config config) {
        return new DefaultParser(config);
    }
}
