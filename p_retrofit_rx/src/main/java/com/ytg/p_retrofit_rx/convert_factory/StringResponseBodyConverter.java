package com.ytg.p_retrofit_rx.convert_factory;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
/**
 *
 * @author 于堂刚
 */
public class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            return value.string();
        } finally {
            value.close();
        }
    }
}
