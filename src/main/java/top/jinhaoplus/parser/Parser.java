package top.jinhaoplus.parser;

import top.jinhaoplus.core.Result;
import top.jinhaoplus.http.Request;

public interface Parser {
    Result parse(Request request);
}
