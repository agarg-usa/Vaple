package databaseobject;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.DBCursor;
import com.sun.xml.internal.bind.v2.model.core.ID;
import main.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import util.Json;
import util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE> The activity class describes an activity object, which stores data about the activity's name, location, time, description and
 * a list of attendees. The activity class also constructs a corresponding DBObject to store in the database.</PRE>
 */
public class Activity implements DatabaseStructureObject, Comparable<Activity>
{

	public static final String NAME = "name", DESCRIPTION = "description", LATITUDE = "latitude", LONGITUDE = "longitude",
			ATTENDING = "attending", TIME_START = "time_start", TIME_END = "time_end", FORM = "form", OBJECTID = "objectID",
			ASSOCIATED_CLUB = "associated_club", CREATOR = "creator", TYPE = "type";

	DBObject object = new BasicDBObject();
	List<String> tags;
	public Activity(String name, String description, String type, ObjectId creator, ArrayList<ObjectId> attending, long time_start, long time_end, double latitude, double longitude, Club club)
	{
		tags = new ArrayList<String>();
		object.put("name",name);
		object.put("type",type);
		object.put("description",description);
		object.put("attending",attending);
		object.put("creator",creator);
		object.put("time_start",time_start);
		object.put("time_end",time_end);
		object.put("latitude",latitude);
		object.put("longitude",longitude);
		object.put("tags",tags);
		if (club != null) {
			object.put("club", club);
		}
	}

	@Override
	public int compareTo(Activity o) {
		if ((long)get(TIME_START) - (long)o.get(TIME_START) > 0)
		{
			return -1;
		}
		else if ((long)get(TIME_START) == (long)o.get(TIME_START))
		{
			return 0;
		}
		return 1;
	}

	private static Activity databaseConnectivityObject = new Activity();
	public static Activity databaseConnectivity()
	{
		return databaseConnectivityObject;
	}

	public Activity() {}



	@Override
	public Object get(String string) {
		return object.get(string);
	}

	public void set(String identifier, Object value)
	{
		object.put(identifier,value);
	}


	public ObjectNode toFeedNode(){

		ObjectNode node = JsonNodeFactory.instance.objectNode();
		node.put("name", (String)get(NAME));
		node.put("id", getObjectID().toHexString());
		node.put("description", (String)get(DESCRIPTION));
		node.put("type", (String)get(TYPE));
		node.put("attending", Json.objectIdListToJsonNode((ArrayList<ObjectId>)get(ATTENDING)));
		node.put("creator", ((ObjectId)(get(CREATOR))) .toHexString());
		node.put("time_start", (long)get(TIME_START));
		node.put("time_end", (long)get(TIME_END));
		node.put(LATITUDE, (double)get(LATITUDE));
		node.put(LONGITUDE, (double)get(LONGITUDE));
		ObjectNode creator = Util.createObjectNode();
		creator.put("id", ((ObjectId)get(CREATOR)).toHexString());
		creator.put("name", (String)((User)User.databaseConnectivity().getFromInfoInDataBase(ID, get(CREATOR))).get(NAME));
		node.put("creator", creator);


		return node;
	}

	@Override
	public ObjectId getObjectID() {
		return (ObjectId)object.get("_id");
	}

	public Activity(DBObject object)
	{
		this.object = object;
	}

	/**
	 * Finds an activity in the database
	 * @param obj The activity to find
	 * @return Returns the activity object
	 */
	@Override
	public DatabaseStructureObject findInDatabase(DBObject obj)
	{
		DBObject object = DatabaseConnectivity.findOneObject(obj, DatabaseConnectivity.ACTIVITYCOLLECTION);
		if(object != null)
			return new Activity(object);
		return null;
	}

	/**
	 * Finds an activity via info
	 * @param varName The property type
	 * @param data The value of the property
	 * @return The activity object
	 */
	@Override
	public DatabaseStructureObject getFromInfoInDataBase(String varName, Object data)
	{
		return findInDatabase(new BasicDBObject(varName,data));

	}

	/**
	 * Finds if an activity exists in the database through provided info
	 * @param varName The property type
	 * @param data The value of the property
	 * @return True if it exists in the database, false if it doesn't
	 */
	@Override
	public boolean infoExistsInDatabase(String varName, Object data)
	{
		return getFromInfoInDataBase(varName, data) != null;
	}

	/**
	 * Updates a specified object in the database
	 * @param object The updated object
	 */
	@Override
	public void updateInDatabase(DatabaseStructureObject object)
	{
		DatabaseConnectivity.updateObject(object.getDBForm(), DatabaseConnectivity.ACTIVITYCOLLECTION);
	}

	/**
	 * Adds an activity into the database
	 * @param object The object to add
	 */
	@Override
	public void addInDatabase(DatabaseStructureObject object)
	{
		DatabaseConnectivity.addObject(object.getDBForm(), DatabaseConnectivity.ACTIVITYCOLLECTION);
	}

	public DBObject getDBForm()
	{
		return object;
	}

	public void addTag(String tag)
	{
		tags = (ArrayList<String>)object.get("tags");
		tags.add(tag);
		object.put("tags",tags);
	}

	public void removeTag(String tag)
	{
		tags = (ArrayList<String>)object.get("tags");
		tags.remove(tag);
		object.put("tags",tags);
	}

	public ArrayList<String> getTags()
	{
		return (ArrayList<String>)object.get("tags");
	}

	public static ArrayList<Activity> getActivitiesByTag(String tag)
	{
		ArrayList<DBObject> objects = (ArrayList<DBObject>)DatabaseConnectivity.findObjectsByCommonProperty("tags",tag, DatabaseConnectivity.ACTIVITYCOLLECTION);
		ArrayList<Activity> activities = new ArrayList<Activity>();
		for (int i = 0; i < objects.size(); i++)
			activities.add(new Activity(objects.get(i)));

		return activities;
	}
}
