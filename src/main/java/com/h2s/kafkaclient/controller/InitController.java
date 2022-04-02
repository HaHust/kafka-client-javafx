package com.h2s.kafkaclient.controller;

import com.h2s.kafkaclient.BrokerSettingModel;
import com.h2s.kafkaclient.HelloApplication;
import com.h2s.kafkaclient.model.ConsumerModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InitController implements Initializable {

    @FXML
    public Button connectBtn;

    @FXML
    private ListView<BrokerSettingModel> settingListView;

    private BrokerSettingModel brokerSettingModel;

    private static ConsumerModel consumerModel;

    private static Stage s;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<BrokerSettingModel> settingModels = BrokerSettingModel.readSetting();
            settingListView.setItems(FXCollections.observableArrayList(settingModels));
            settingListView.setCellFactory(param -> new ListCell<BrokerSettingModel>() {
                @Override
                protected void updateItem(BrokerSettingModel item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null || item.getBroker() == null) {
                        setText(null);
                    } else {
                        setText(item.getBroker());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        settingListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue != null) {
                        brokerSettingModel = newValue;
                    }
                }
        );
    }

    public void onConnection(ActionEvent actionEvent) throws IOException {
        consumerModel = new ConsumerModel(brokerSettingModel.getTopic(), brokerSettingModel.getGroupId(), brokerSettingModel.getBroker());
        s.close();
    }

    public static ConsumerModel display() throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(HelloApplication.class.getResource("init-view.fxml"));
        Parent parent = (Parent) fxmlLoader1.load();
        s = new Stage();
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle("Choose a connection");
        s.setScene(new Scene(parent));
        s.showAndWait();
        return consumerModel;
    }

    public void onCancel(ActionEvent actionEvent) {
        consumerModel = new ConsumerModel();
        s.close();
    }
}
