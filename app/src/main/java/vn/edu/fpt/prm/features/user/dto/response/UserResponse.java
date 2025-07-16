package vn.edu.fpt.prm.features.user.dto.response;

import vn.edu.fpt.prm.features.user.User;

public class UserResponse {
    private String status;
    private Data data;

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
