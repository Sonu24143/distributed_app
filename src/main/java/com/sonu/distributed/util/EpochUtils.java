package com.sonu.distributed.util;

public class EpochUtils {
    public static Long roundOffToDuration(long time, long duration) {
        long delta = (time % duration);
        return time - delta;
    }
}
