package advisor;

public class ExitStrategy extends ReactionStrategies {
    @Override
    UserReply produce(UserRequest userRequest) {
        System.out.println("---GOODBYE!---");
        return new UserReply("exit");
    }
}