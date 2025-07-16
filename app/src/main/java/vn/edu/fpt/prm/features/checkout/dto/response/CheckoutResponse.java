package vn.edu.fpt.prm.features.checkout.dto.response;
public class CheckoutResponse {
    private String status;
    private Session session;

    public String getStatus() {
        return status;
    }

    public Session getSession() {
        return session;
    }

    public static class Session {
        private String id;
        private String url;
        private long amount_total;
        private String payment_status;
        private String success_url;
        private String cancel_url;

        public String getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public long getAmountTotal() {
            return amount_total;
        }

        public String getPaymentStatus() {
            return payment_status;
        }

        public String getSuccessUrl() {
            return success_url;
        }

        public String getCancelUrl() {
            return cancel_url;
        }
    }
}
