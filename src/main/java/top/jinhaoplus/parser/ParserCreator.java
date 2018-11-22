package top.jinhaoplus.parser;

import top.jinhaoplus.core.Config;

public class ParserCreator {
    public static Parser create(Config config) throws ParserException {
        try {
            ParserFactory parserFactory = (ParserFactory) Class.forName(config.parserFactoryClass()).newInstance();
            return parserFactory.newInstance(config);
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
