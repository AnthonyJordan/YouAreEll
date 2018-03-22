
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
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

        System.out.println(MakeURLCall("/ids", "PUT", jsonPackage));
    }

    public String get_messages() {
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonString = MakeURLCall("/messages", "GET", "");
        try {
            Messages[] messagesList = jsonMapper.readValue(jsonString, Messages[].class);
            Messages[] last20 = Arrays.copyOf(messagesList, 20);
            return Arrays.toString(last20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String MakeURLCall(String mainurl, String method, String jpayload) {

        try {
            if (method.equals("GET")) {
                return Unirest.get("http://zipcode.rocks:8085" + mainurl).asString().getBody();
            }
            if (method.equals("PUT")){
                return Unirest.post("http://zipcode.rocks:8085" + mainurl).body(jpayload).asString().getStatusText();
            }
        }catch (UnirestException e){
            e.printStackTrace();
        }
        return null;
    }
}
