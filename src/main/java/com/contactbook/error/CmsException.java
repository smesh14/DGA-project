package com.contactbook.error;

public class CmsException extends RuntimeException {

	private final CmsExceptionCode exceptionCode;

	public CmsException(CmsExceptionCode exceptionCode) {
		this(exceptionCode, exceptionCode.name());
	}

	public CmsException(String message) {
		this(CmsExceptionCode.INTERNAL_SEVER_ERROR, message);
	}

	public static CmsException badRequest() {
		return new CmsException(CmsExceptionCode.BAD_REQUEST);
	}

	public CmsException(CmsExceptionCode exceptionCode, String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	public static CmsException badRequest(String errorMessage) {
		return new CmsException(CmsExceptionCode.BAD_REQUEST, errorMessage);
	}

	public static CmsException notFound() {
		return new CmsException(CmsExceptionCode.NOT_FOUND);
	}

	public CmsExceptionCode getExceptionCode() {
		return exceptionCode;
	}
}
