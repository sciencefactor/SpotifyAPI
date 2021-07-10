package advisor;

public abstract class ReactionStrategies {
    abstract UserReply produce(UserRequest userRequest);
}

class UserReply {
    String reply;

    public UserReply() {
        this.reply = "";
    }

    public UserReply(String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }
}
