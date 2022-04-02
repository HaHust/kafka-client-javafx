package com.h2s.kafkaclient.utils;

import com.h2s.kafkaclient.controller.HelloController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class Common {
    private final static String THREAD = "Thread";
    private final static String HIFEN = "-";

    public static String getNameThreadTopic(String topic) {
        return THREAD.concat(HIFEN).concat(topic);
    }

    public static Object getController(String view, Class<?> c) throws IOException {
        FXMLLoader loader = new FXMLLoader(c.getResource(view));
        Parent root = loader.load();
        return loader.getController();
    }
}
