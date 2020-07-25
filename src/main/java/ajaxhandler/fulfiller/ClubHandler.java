package ajaxhandler.fulfiller;

import ajaxhandler.AjaxHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import databaseobject.Club;
import databaseobject.User;
import org.bson.types.ObjectId;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ClubHandler implements AjaxHandler
{
    @Override
    public int service(HttpServletRequest req, HttpServletResponse resp, JsonNode request, ObjectNode response, String[] uriSplit, User user) throws ServletException, IOException {

        if(uriSplit.length != 3) // 0: / 1: club 2: id
            return 404;
        String id = uriSplit[2];
        Club c = (Club)Club.databaseConnectivity().getFromInfoInDataBase(ID, new ObjectId(id));
        if (c == null)
        {
            return 404;
        }

        ObjectNode node = Util.createObjectNode();
        response.put("data", node);
        response.put("type", "club");
        response.put("data", c.getConciseDataNode());
        return 200;

    }

    @Override
    public boolean isPage() {
        return true;
    }
}