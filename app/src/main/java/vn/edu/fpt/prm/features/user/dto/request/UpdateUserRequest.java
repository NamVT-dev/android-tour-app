package vn.edu.fpt.prm.features.user.dto.request;

public class UpdateUserRequest {
    private String name;
    private String email;

    public UpdateUserRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
