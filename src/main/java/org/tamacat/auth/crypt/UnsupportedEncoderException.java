package org.tamacat.auth.crypt;

public class UnsupportedEncoderException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public UnsupportedEncoderException(String arg0) {
		super(arg0);
	}

	public UnsupportedEncoderException(Throwable arg0) {
		super(arg0);
	}

	public UnsupportedEncoderException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
