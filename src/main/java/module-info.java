module com.h2s.kafkaclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires kafka.clients;

    opens com.h2s.kafkaclient to javafx.fxml;
    exports com.h2s.kafkaclient;

    opens com.h2s.kafkaclient.model to javafx.base;
    exports com.h2s.kafkaclient.model;
    exports com.h2s.kafkaclient.utils;
    opens com.h2s.kafkaclient.utils to javafx.fxml;
    exports com.h2s.kafkaclient.controller;
    opens com.h2s.kafkaclient.controller to javafx.fxml;

}
