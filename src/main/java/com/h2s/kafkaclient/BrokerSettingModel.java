package com.h2s.kafkaclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2s.kafkaclient.constant.Settings;
import com.h2s.kafkaclient.model.ConsumerModel;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BrokerSettingModel {
    private String broker;
    private String groupId;
    private String topic;
    private String alias;

    public BrokerSettingModel() {}

    public BrokerSettingModel(String broker, String groupId, String topic, String alias) {
        this.broker = broker;
        this.groupId = groupId;
        this.topic = topic;
        this.alias = alias;
    }

    public BrokerSettingModel(String broker, String groupId, String topic) {
        this.broker = broker;
        this.groupId = groupId;
        this.topic = topic;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "BrokerSettingModel{" +
                "broker='" + broker + '\'' +
                ", groupId='" + groupId + '\'' +
                ", topic='" + topic + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrokerSettingModel that = (BrokerSettingModel) o;
        return Objects.equals(broker, that.broker) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(broker, groupId, topic);
    }

    // read from json file by jackson library
    public static List<BrokerSettingModel> readSetting() throws IOException {
        List<BrokerSettingModel> settingModels = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            settingModels = mapper.readValue(Paths.get(Settings.CONFIG_FILE_PATH).toFile(), new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return settingModels;
    }

    // write to json file by jackson library
    public static void writeSetting(BrokerSettingModel settingModels) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<BrokerSettingModel> var1 = readSetting();
            if(!var1.contains(settingModels)) {
                var1.add(settingModels);
            }
            mapper.writeValue(Paths.get(Settings.CONFIG_FILE_PATH).toFile(), var1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
