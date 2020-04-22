package ir.ac.kntu.Entity;

public class AuthenticationResponse {
    private int resultCode;
    private String message;
    private String token;

    public AuthenticationResponse(int resultCode, String message, String token) {
        this.resultCode = resultCode;
        this.message = message;
        this.token = token;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static enum ResultCode {
        ERROR(100), SUCCESSFUL(200);
        private int code;

        ResultCode(int code) {
            this.code = code;
        }

        /***
         * @param code value
         * @return corresponding ResultCode, default : ERROR
         */
        public static ResultCode getResult(int code) {
            for (ResultCode resultCode : AuthenticationResponse.ResultCode.values())
                if (code == resultCode.code)
                    return resultCode;
            return ERROR;
        }
    }
}
