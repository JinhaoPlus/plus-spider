package top.jinhaoplus.parser;

import top.jinhaoplus.config.Config;

public interface ParserFactory {
    Parser newInstance(Config config);
}
