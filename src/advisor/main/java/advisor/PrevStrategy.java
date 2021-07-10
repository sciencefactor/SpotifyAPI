package advisor;

public class PrevStrategy extends ReactionStrategies{
    @Override
    UserReply produce(UserRequest userRequest) {
        Main.currentPages.printPrev();
        return new UserReply();
    }
}