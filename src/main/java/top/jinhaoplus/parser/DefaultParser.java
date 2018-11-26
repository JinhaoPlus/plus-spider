package top.jinhaoplus.parser;

import top.jinhaoplus.config.Config;
import top.jinhaoplus.core.Result;
import top.jinhaoplus.http.Response;

public class DefaultParser implements Parser {

    public DefaultParser(Config config) {
    }

    @Override
    public Result parse(Response response) {
        System.out.println("Parse:" + response.resultText());
        return null;
    }
}
