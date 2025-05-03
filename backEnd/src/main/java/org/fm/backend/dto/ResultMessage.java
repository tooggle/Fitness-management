package org.fm.backend.dto;

public class ResultMessage {
    public String message;

    public ResultMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResultMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
