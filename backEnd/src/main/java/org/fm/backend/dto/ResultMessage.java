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
    private int code;
    private String message;
    private Object data;

    public static class ResultMessageBuilder {
        private int code;
        private String message;
        private Object data;

        public ResultMessageBuilder code(int code) {
            this.code = code;
            return this;
        }

        public ResultMessage build() {
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.code = this.code;
            resultMessage.message = this.message;
            resultMessage.data = this.data;
            return resultMessage;
        }
    }
}
