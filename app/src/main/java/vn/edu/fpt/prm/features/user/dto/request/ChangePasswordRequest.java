package vn.edu.fpt.prm.features.user.dto.request;

public class ChangePasswordRequest {
    private String passwordCurrent;
    private String password;
    private String passwordConfirm;

    public ChangePasswordRequest(String passwordCurrent, String password, String passwordConfirm) {
        this.passwordCurrent = passwordCurrent;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
}

