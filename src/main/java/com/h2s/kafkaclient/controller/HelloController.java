package com.h2s.kafkaclient.controller;

import com.h2s.kafkaclient.BrokerSettingModel;
import com.h2s.kafkaclient.HelloApplication;
import com.h2s.kafkaclient.concurrency.KafkaConsumerRunner;
import com.h2s.kafkaclient.model.ConsumerModel;
import com.h2s.kafkaclient.model.RecordModel;
import com.h2s.kafkaclient.utils.Common;
import com.h2s.kafkaclient.utils.KafkaUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import static com.h2s.kafkaclient.BrokerSettingModel.writeSetting;

public class HelloController implements Initializable {
    @FXML
    BorderPane bp;

    @FXML
    private TextField brokerId = new TextField();
    @FXML
    private TextField groupId = new TextField();

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

    @FXML
    private ListView<String> topicListView;

    @FXML
    private TextField searchTopic;

    private List<Thread> thread = new Vector<>();

    Properties properties = new Properties();

    ObservableList<RecordModel> records = FXCollections.observableArrayList();

    private Set<String> topicList;

    private void init() {
        topicListView.setItems(null);
        groupId.setText("");
        brokerId.setText("");
        processCircle.setVisible(false);
        pingServerStt.setFill(javafx.scene.paint.Color.rgb(255,0,0));

        records.addListener((ListChangeListener<RecordModel>) c -> {
            ObservableList<? extends RecordModel> list = c.getList();
            System.out.println("records size: " + list.size());
        });

        searchTopic.textProperty().addListener((observable, oldValue, newValue) -> {
            Set<String> result = topicList.stream().filter(item -> item.contains(newValue)).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toCollection(LinkedHashSet::new));
            topicListView.setItems(FXCollections.observableArrayList(result));
        });

    }

    public void onClick(ActionEvent actionEvent) {
        processCircle.setVisible(true);
        ConsumerModel consumerModel = new ConsumerModel("", groupId.getText(), brokerId.getText());
        System.out.println("groupId: " + groupId.getText());
        //Creating consumer properties
        properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerModel.getBrokerIp());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,consumerModel.getGroupId());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        //creating consumer
        topicList = KafkaUtils.checkConnection(properties);
        if(!topicList.isEmpty()) {
            pingServerStt.setFill(javafx.scene.paint.Color.rgb(40,127,250));
            topicListView.setItems(FXCollections.observableArrayList(topicList.stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList())));
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
        try {
            ConsumerModel var1 = InitController.display();
            fillSetting(var1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void choiceTopic(MouseEvent arg0) throws IOException {
        if(arg0.getButton().equals(MouseButton.PRIMARY)){
            if(arg0.getClickCount() == 2){
                String topic = topicListView.getSelectionModel().getSelectedItems().get(0);
                writeSetting(new BrokerSettingModel(brokerId.getText(), groupId.getText(), topic));
                String threadName = Common.getNameThreadTopic(topic);
                if(thread.isEmpty()) {
                    KafkaConsumerRunner kafkaConsumerRunner = new KafkaConsumerRunner(topic, properties, records, processCircle);
                    Thread onMessageThread = new Thread(kafkaConsumerRunner);
                    onMessageThread.setName(Common.getNameThreadTopic(topic));
                    onMessageThread.start();
                    thread.add(onMessageThread);
                    System.out.println(onMessageThread.getThreadGroup().toString()+" - " + onMessageThread.getName());
                } else {
                    for (Thread item : thread) {
                        if(!item.getName().equals(threadName)){
                            KafkaConsumerRunner kafkaConsumerRunner = new KafkaConsumerRunner(topic, properties, records, processCircle);
                            Thread onMessageThread = new Thread(kafkaConsumerRunner);
                            onMessageThread.setName(Common.getNameThreadTopic(topic));
                            onMessageThread.start();
                            thread.add(onMessageThread);
                            System.out.println(onMessageThread.getThreadGroup().toString()+" - " + onMessageThread.getName());
                        }
                    }
                }
            }
        }
    }

    public void fillSetting(ConsumerModel p_consumerModel) {
        groupId.setText(p_consumerModel.getGroupId());
        brokerId.setText(p_consumerModel.getBrokerIp());
    }

    public void stopService(Stage stage) {
        stage.setOnHiding(event -> {
            if(thread != null) {
                try {
                    for (Thread item : thread) {
                        item.stop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void show(ActionEvent actionEvent) {
        try {
            ConsumerModel var1 = InitController.display();
            fillSetting(var1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
