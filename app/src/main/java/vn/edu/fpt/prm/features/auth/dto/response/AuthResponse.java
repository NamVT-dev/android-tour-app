package vn.edu.fpt.prm.features.auth.dto.response;

import vn.edu.fpt.prm.features.user.User;

public class AuthResponse {
    private String status;
    private String token;
    private Data data;

    public String getToken() { return token; }

    public User toUser() {
        return data.user.toUser();
    }

    public static class Data {
        private InnerUser user;
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
