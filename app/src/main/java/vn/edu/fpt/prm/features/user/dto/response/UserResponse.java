package vn.edu.fpt.prm.features.user.dto.response;

import vn.edu.fpt.prm.features.auth.dto.response.AuthResponse;
import vn.edu.fpt.prm.features.user.User;

public class UserResponse {
    private String status;
    private Data data;

    public User toUser() {
        return data.user.toUser();
    }

    public static class Data {
        private AuthResponse.InnerUser user;
    }

    public static class InnerUser {
        private String _id;
        private String name;
        private String email;
        private String photo;
        private String role;

        public User toUser() {
            return new User(_id, name, email, photo, role);
        }
    }
}
