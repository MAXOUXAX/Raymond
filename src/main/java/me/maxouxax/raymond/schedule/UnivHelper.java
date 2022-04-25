package me.maxouxax.raymond.schedule;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.Supervisor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class UnivHelper {

    private static UnivConnector univConnector = Raymond.getInstance().getUnivConnector();

    public static ArrayList<UnivClass> getUnivSchedule(ZonedDateTime from, ZonedDateTime to) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operationName", "timetable");
        jsonObject.put("variables", new JSONObject()
                .put("uid", univConnector.getUnivConfig().getUsername())
                .put("from", from.toInstant().toEpochMilli())
                .put("to", to.toInstant().toEpochMilli()));
        jsonObject.put("query", "query timetable($uid: String!, $from: Float, $to: Float) {\n  timetable(uid: $uid, from: $from, to: $to) {\n    id\n    messages {\n      text\n      level\n      __typename\n    }\n    plannings {\n      id\n      type\n      label\n      default\n      messages {\n        text\n        level\n        __typename\n      }\n      events {\n        id\n        startDateTime\n        endDateTime\n        day\n        duration\n        urls\n        course {\n          id\n          label\n          color\n          url\n          type\n          __typename\n        }\n        teachers {\n          name\n          email\n          __typename\n        }\n        rooms {\n          label\n          __typename\n        }\n        groups {\n          label\n          __typename\n        }\n        __typename\n      }\n      __typename\n    }\n    __typename\n  }\n}\n");

        ArrayList<UnivClass> univClasses = new ArrayList<>();

        try {
            JSONObject response = univConnector.makeGQLRequest(jsonObject);
            JSONObject userPlanning = response.getJSONObject("data").getJSONObject("timetable").getJSONArray("plannings").getJSONObject(0);
            userPlanning.getJSONArray("events").forEach(event -> {
                UnivClass univClass = new UnivClass((JSONObject) event);
                univClasses.add(univClass);
            });
        } catch (IOException | URISyntaxException | InterruptedException | JSONException e) {
            Supervisor.getInstance().getErrorHandler().handleException(e);
        }

        return univClasses;
    }

}
