package com.h2s.kafkaclient.model;

public class RecordModel {
    private String partition;

    private String offset;

    private String timestamp;

    private String value;

    public RecordModel(String partition, String offset, String timestamp, String value) {
        this.partition = partition;
        this.offset = offset;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
