package advisor;

public class NextStrategy extends ReactionStrategies {

    @Override
    UserReply produce(UserRequest userRequest) {
        Main.currentPages.printNext();
        return new UserReply();
    }
}