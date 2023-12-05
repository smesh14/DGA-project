package com.contactbook.error;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorInfo {

	private String message;

	private String field;

    private Date timeStamp = new Date();

    public ErrorInfo(String message) {
        this.message = message;
    }

    public ErrorInfo(String message, String field) {
        this.message = message;
        this.field = field;
    }
}
