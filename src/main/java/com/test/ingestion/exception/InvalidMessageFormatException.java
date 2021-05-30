package com.test.ingestion.exception;

public class InvalidMessageFormatException extends Exception{

    private static final long serialVersionUID = 1L;

    private String messageId;

    private final String message;

    private String code;

    private Exception cause;


    public InvalidMessageFormatException(String code, String message) {
        super(code + " : " + message);
        this.code = code;
        this.message = message;
    }

    public InvalidMessageFormatException(String code, String message,String messageId) {
        super(code + " : " + message);
        this.code = code;
        this.message = message;
        this.messageId=messageId;
    }

    public InvalidMessageFormatException(String code, String message, Exception cause) {
        super(code + " : " + message);
        this.code = code;
        this.message = message;
        this.cause = cause;
    }

    /**
     * @param message the message
     */
    public InvalidMessageFormatException(final String message) {
        super(message);
        this.message = message;
    }


    public InvalidMessageFormatException(final String message, final Throwable cause, String message1) {
        super(message, cause);
        this.message = message1;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public Exception getCause() {
        return cause;
    }

    public void setCause(Exception cause) {
        this.cause = cause;
    }

    public String getMessageId() {
        return messageId;
    }
}
