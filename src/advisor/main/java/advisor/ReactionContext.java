package advisor;

import java.util.Map;

public class ReactionContext {
    public static final Map<String, ReactionStrategies> strategies = Map.of(
            "featured", new FeaturedStrategy(),
            "new", new NewStrategy(),
            "categories", new CategoriesStrategy(),
            "playlists", new PlaylistsStrategy(),
            "exit", new ExitStrategy(),
            "auth", new AuthorizationStrategy(),
            "error", new ExceptionStrategy(),
            "next", new NextStrategy(),
            "prev", new PrevStrategy());

    private ReactionStrategies method;

    public void setMethod(UserRequest userRequest) {
        String strategy = userRequest.getRequest();
        chooseStrategy(strategy);
    }

    private void chooseStrategy(String strategy) {
        if (strategies.containsKey(strategy)) {
            this.method = strategies.get(strategy);
        } else {
            this.method = new ExceptionStrategy();
        }
    }

    public UserReply produce(UserRequest userRequest) {
        if (userRequest.isAuthorised() || userRequest.getRequest().equals("auth") || userRequest.getRequest().equals("exit")) {
            return this.method.produce(userRequest);
        }
        System.out.println("Please, provide access for application.");
        return new UserReply();
    }
}
