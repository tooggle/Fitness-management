package org.fm.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultMessage {
    public String message;

    @Override
    public String toString() {
        return "ResultMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
