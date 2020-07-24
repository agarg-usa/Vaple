import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

public class FollowPersonalHandler implements AjaxHandler
{
    @Override
    public int service(HttpServletRequest req, HttpServletResponse resp, JsonNode request, ObjectNode response, String[] uriSplit, User user) throws ServletException, IOException {
        JsonNode jsonNode = request.get("following");

        if (user == null)
        {
            return 400;
        }

        if (jsonNode != null) {
            for (Iterator<JsonNode> it = jsonNode.elements(); it.hasNext(); ) {
                JsonNode idToFollow = it.next();
				String id = Util.removeNonAlphanumeric(Util.asText(idToFollow));

				if(id != null){
					User other = (User)User.databaseConnectivity().getByInfoInDataBase(ID, new ObjectId(id));

					if(other != null)
						continue;
					user.setFollowing(other.getObjectID(), true);
					other.setFollower(user.getObjectID(), true);
				}
            }
        }

        jsonNode = request.get("unfollowing");
        if (jsonNode != null)
        {
            for (Iterator<JsonNode> it = jsonNode.elements(); it.hasNext(); ) {
                JsonNode idToFollow = it.next();
				String id = Util.removeNonAlphanumeric(Util.asText(idToFollow));

				if(id != null){
					User other = (User)User.databaseConnectivity().getByInfoInDataBase(ID, new ObjectId(id));

					if(other != null)
						continue;
					user.setFollowing(other.getObjectID(), false);
					other.setFollower(user.getObjectID(), false);
				}
            }
        }
        return 200;
    }

    @Override
    public boolean isPage() {
        return false;
    }
}
