package vn.edu.fpt.prm.features.user.dto.response;

import vn.edu.fpt.prm.features.user.User;

public class ChangePasswordResponse {
    private String status;
    private String token;

    private Data data;

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private User user;

        public User getUser() {
            return user;
        }
    }
}
