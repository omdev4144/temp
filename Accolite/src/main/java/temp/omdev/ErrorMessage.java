package temp.omdev;

public enum ErrorMessage {

		FILE_NOT_FOUND_ERROR("File Not Found!",4700),
		DOCUMENT_NOT_FOUND_ERROR("Document Not Found!",999),
		PARSE_ERROR("Error Occurred When Parsing Json",420),
	    DATABASE_ERROR("Cant Connect To Database",600);
	    


		private String message;

		private int code;

		ErrorMessage(String errorMessage, int errorCode) {
		this.message = errorMessage;
		this.code = errorCode;
		}

		public String getMessage() {
		return message;
		}

		public int getCode() {
		return code;
		}

		}
	
