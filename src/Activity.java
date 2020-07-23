import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Activity
{
    String name, location_country, location_state, location_city, description;
    ArrayList<ObjectId> attending;
    long time;
    BasicDBObject form;
    ObjectId objectID;

    public Activity(String name, String location_country, String location_state, String location_city, String description, ArrayList<ObjectId> attending, long time)
    {
        this.name = name;
        this.location_country = location_country;
        this.location_state = location_state;
        this.location_city = location_city;
        this.description = description;
        this.attending = attending;
        this.time = time;
    }

    public Activity(DBObject object)
    {
        name = (String)object.get("name");
        location_country = (String)object.get("location_country");
        location_city = (String)object.get("location_city");
        location_state = (String)object.get("location_state");
        description = (String)object.get("description");
        objectID = (ObjectId)object.get("_id");
        attending = (ArrayList<ObjectId>)object.get("attending");
        time = (long)object.get("time");
    }

    public Activity(BasicDBObject object)
    {
        name = (String)object.get("name");
        location_country = (String)object.get("location_country");
        location_city = (String)object.get("location_city");
        location_state = (String)object.get("location_state");
        description = (String)object.get("description");
        objectID = (ObjectId)object.get("_id");
        attending = (ArrayList<ObjectId>)object.get("attending");
        time = (long)object.get("time");
    }

    public BasicDBObject getDBform()
    {
        form = new BasicDBObject();
        form.append("name",getName());
        form.append("location_county",getLocation_country());
        form.append("location_state",getLocation_state());
        form.append("location_city",getLocation_city());
        form.append("description",getDescription());
        form.append("attending",getAttending());
        form.append("time",getTime());

        return form;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLocation_country()
    {
        return location_country;
    }

    public void setLocation_country(String location_country)
    {
        this.location_country = location_country;
    }

    public String getLocation_state()
    {
        return location_state;
    }

    public ObjectId getObjectID()
    {
        return objectID;
    }

    public void setObjectID(ObjectId objectID)
    {
        this.objectID = objectID;
    }


    public void setLocation_state(String location_state)
    {
        this.location_state = location_state;
    }

    public String getLocation_city()
    {
        return location_city;
    }

    public void setLocation_city(String location_city)
    {
        this.location_city = location_city;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ArrayList<ObjectId> getAttending()
    {
        return attending;
    }

    public void setAttending(ArrayList<ObjectId> attending)
    {
        this.attending = attending;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }
}