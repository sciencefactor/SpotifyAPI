package advisor;

public class ExceptionStrategy extends ReactionStrategies {
    @Override
    UserReply produce(UserRequest userRequest) {
        System.out.println("---WRONG COMMAND---");
        return new UserReply();
    }
}