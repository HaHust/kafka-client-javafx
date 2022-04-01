package com.h2s.kafkaclient.model;

public class ConsumerModel {
    private String topic;
    private String groupId;
    private String brokerIp;

    public ConsumerModel() {
    }

    public ConsumerModel(String topic, String groupId, String brokerIp) {
        this.topic = topic;
        this.groupId = groupId;
        this.brokerIp = brokerIp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    @Override
    public String toString() {
        return "ConsumerModel{" +
                "topic='" + topic + '\'' +
                ", groupId='" + groupId + '\'' +
                ", brokerIp='" + brokerIp + '\'' +
                '}';
    }
}
