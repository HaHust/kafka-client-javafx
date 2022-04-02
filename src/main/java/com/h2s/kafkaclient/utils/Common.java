package com.h2s.kafkaclient.utils;

public class Common {
    private final static String THREAD = "Thread";
    private final static String HIFEN = "-";

    public static String getNameThreadTopic(String topic) {
        return THREAD.concat(HIFEN).concat(topic);
    }
}
