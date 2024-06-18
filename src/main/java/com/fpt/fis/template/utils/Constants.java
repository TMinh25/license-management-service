package com.fpt.fis.template.utils;

public class Constants {

    public enum ErrorType {
        DELETE_USED("DELETE_USED", "That print template has been used. Cannot be deleted."),
        EDIT_USED("EDIT_USED", "That print template has been used. Cannot be edited.");

        private final String code;
        private final String message;

        ErrorType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
