package top.jinhaoplus.parser;

import top.jinhaoplus.core.Config;

public interface ParserFactory {
    Parser newInstance(Config config);
}
