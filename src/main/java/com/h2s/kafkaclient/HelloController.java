package com.h2s.kafkaclient;

import com.h2s.kafkaclient.concurrency.KafkaConsumerRunner;
import com.h2s.kafkaclient.model.ConsumerModel;
import com.h2s.kafkaclient.model.RecordModel;
import com.h2s.kafkaclient.utils.KafkaUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class HelloController implements Initializable {
    @FXML
    private TextField brokerId;
    @FXML
    private TextField groupId;

    @FXML
    private ComboBox topicComboBox;

    @FXML
    private ProgressIndicator processCircle;

    @FXML
    public Button connect;

    @FXML
    public TableView<RecordModel> tableView;

    @FXML
    public TableColumn<RecordModel, String> partition;

    @FXML
    public TableColumn<RecordModel, String> offset;

    @FXML
    public TableColumn<RecordModel, String> value;

    @FXML
    public TableColumn<RecordModel, String> timestamp;

    @FXML
    private Circle pingServerStt;

    private Thread thread;

    Properties properties = new Properties();

    ObservableList<RecordModel> records = FXCollections.observableArrayList();

    @FXML
    private Label test;

    private void init() {
        brokerId.setText("localhost:9092");
        groupId.setText("test_consumer_group");
        topicComboBox.setItems(null);
        processCircle.setVisible(false);
        pingServerStt.setFill(javafx.scene.paint.Color.rgb(255,0,0));

        records.addListener((ListChangeListener<RecordModel>) c -> {
            ObservableList<? extends RecordModel> list = c.getList();
            System.out.println("records size: " + list.size());
        });
    }

    public void onClick(ActionEvent actionEvent) {
        processCircle.setVisible(true);
        ConsumerModel consumerModel = new ConsumerModel("", groupId.getText(), brokerId.getText());

        //Creating consumer properties
        properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerModel.getBrokerIp());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,consumerModel.getGroupId());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        //creating consumer
        Set<String> topics = KafkaUtils.checkConnection(properties);
        if(!topics.isEmpty()) {
            pingServerStt.setFill(javafx.scene.paint.Color.rgb(40,127,250));
            topicComboBox.setItems(FXCollections.observableArrayList(topics));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        value.setCellValueFactory(new PropertyValueFactory<RecordModel, String>("value"));
        partition.setCellValueFactory(new PropertyValueFactory<RecordModel, String>("partition"));
        offset.setCellValueFactory(new PropertyValueFactory<RecordModel, String>("offset"));
        timestamp.setCellValueFactory(new PropertyValueFactory<RecordModel, String>("timestamp"));

        tableView.setItems(records);
        init();
    }

    public void choiceTopic(ActionEvent actionEvent) {
        String topic = topicComboBox.getValue().toString();
        KafkaConsumerRunner kafkaConsumerRunner = new KafkaConsumerRunner(topic, properties, records, processCircle);
        thread = new Thread(kafkaConsumerRunner);
        thread.start();
    }

    public void stopService(Stage stage) {
        stage.setOnHiding(event -> {
            if(thread != null) {
                try {
                    thread.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
