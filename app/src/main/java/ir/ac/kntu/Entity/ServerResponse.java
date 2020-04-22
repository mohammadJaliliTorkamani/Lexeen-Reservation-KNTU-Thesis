package ir.ac.kntu.Entity;

public class ServerResponse {
    private int code;
    private String message;

    public ServerResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static enum ServerResponseCodes {
        DONE(101), FAILED(102), UNKNOWN(103), NULL(-1);
        private int code;

        ServerResponseCodes(int code) {
            this.code = code;
        }

        /***
         * @param code value
         * @return corresponding ServerResponseCode, default : NULL
         */
        public static ServerResponseCodes getMeaningOf(int code) {
            for (ServerResponseCodes responseCodes : ServerResponseCodes.values())
                if (code == responseCodes.code)
                    return responseCodes;
            return NULL;
        }
    }
}
