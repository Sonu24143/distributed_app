package com.sonu.distributed.util;

public interface Converter<S,D> {
    D convert(S data);
}
