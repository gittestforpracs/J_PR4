import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class ChatWebSocketHandler {

    private String sender, msg;

    int iJoinCounter = 0;
    int iLeftCounter = 0;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Chat.nextUserNumber++;
        Chat.userUsernameMap.put(user, username);
        if (iJoinCounter == 0)
        {
            Chat.broadcastMessage(sender = "Server", msg = (username + " первый в этом чате!!!"));
        }

        if (iJoinCounter == 1)
        {
            Chat.broadcastMessage(sender = "Server", msg = (username + " только что вошёл в этот чат!"));
            iJoinCounter = 0;
        }

        iJoinCounter += 1;
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Chat.userUsernameMap.get(user);
        Chat.userUsernameMap.remove(user);

        if (iLeftCounter == 0)
        {
            Chat.broadcastMessage(sender = "Server", msg = ("Быть не может! " + username + " внезапно покинул чат!"));
        }

        if (iLeftCounter == 1)
        {
            Chat.broadcastMessage(sender = "Server", msg = ( username + " покинул чат!"));
        }

        iLeftCounter = 1;
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        Chat.broadcastMessage(sender = Chat.userUsernameMap.get(user), msg = message);
    }

}
