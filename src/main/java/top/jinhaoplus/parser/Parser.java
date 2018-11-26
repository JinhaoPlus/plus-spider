package top.jinhaoplus.parser;

import top.jinhaoplus.core.Result;
import top.jinhaoplus.http.Response;

public interface Parser {
    Result parse(Response response);
}
