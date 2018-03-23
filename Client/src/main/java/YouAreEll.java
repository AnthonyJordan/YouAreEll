
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class YouAreEll {

    YouAreEll() {
    }

    public static void main(String[] args) {
        YouAreEll urlhandler = new YouAreEll();
        System.out.println(urlhandler.MakeURLCall("/ids", "GET", ""));
        System.out.println(urlhandler.MakeURLCall("/messages", "GET", ""));
    }

    public String get_ids() {
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonString = MakeURLCall("/ids", "GET", "");
        try {
            IDs[] iDSList = jsonMapper.readValue(jsonString, IDs[].class);
            return Arrays.toString(iDSList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void postID(String name, String gitHubID){
        String jsonPackage = "{ \"userid\": \"-\", \"name\": \"" + name + "\", \"github\": \"" + gitHubID + "\"}";
        MakeURLCall("/ids", "PUT", jsonPackage);
    }

    public String get_messages() {
        String jsonString = MakeURLCall("/messages", "GET", "");
        String last20 = convertMessagesToString(jsonString);
        if (last20 != null) return last20;
        return null;
    }

    private String convertMessagesToString(String jsonString) {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            Messages[] messagesList = jsonMapper.readValue(jsonString, Messages[].class);
            if (messagesList!=null) {
                Messages[] last20 = Arrays.copyOf(messagesList, 20);
                return getRidOfNulls(last20);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertSingeMessageToString(String jsonString) {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            Messages message = jsonMapper.readValue(jsonString, Messages.class);
            return message.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_messagesForId(String id) {
        String jsonString = MakeURLCall("/ids/"+id+"/messages", "GET", "");
        String last20 = convertMessagesToString(jsonString);
        if (last20 != null) return last20;
        return null;
    }

    public String sendMessage(String id, String message){
        String jsonPackage = "{ \"sequence\": \"-\", \"timestamp\": \"2018-03-21T01:00:00.0Z\", \"fromid\": \"" +id + "\"," +
                "\"toid\": \"\", \"message\": \"" + message + "\"}";
        String returnedMessage = MakeURLCall("/ids/" + id + "/messages", "PUT", jsonPackage);
        return convertSingeMessageToString(returnedMessage);
    }

    public String sendMessageToId(String fromId, String message, String toId){
        String jsonPackage = "{ \"sequence\": \"-\", \"timestamp\": \"2018-03-21T01:00:00.0Z\", \"fromid\": \"" + fromId + "\"," +
                "\"toid\": \""+toId+ "\", \"message\": \"" + message + "\"}";
        String returnedString = MakeURLCall("/ids/" + fromId + "/messages", "PUT", jsonPackage);
        return convertSingeMessageToString(returnedString);
    }

    public String MakeURLCall(String mainurl, String method, String jpayload) {

        try {
            if (method.equals("GET")) {
                return Unirest.get("http://zipcode.rocks:8085" + mainurl).asString().getBody();
            }
            if (method.equals("PUT")){
                return Unirest.post("http://zipcode.rocks:8085" + mainurl).body(jpayload).asString().getBody();
            }
        }catch (UnirestException e){
            e.printStackTrace();
        }
        return null;
    }

    private String getRidOfNulls(Messages[] messages){
        ArrayList<Messages> returnArrayList = new ArrayList<>();
        for (Messages message: messages) {
            if (message!=null){
                returnArrayList.add(message);
            }
        }
        return returnArrayList.toString();
    }
}
