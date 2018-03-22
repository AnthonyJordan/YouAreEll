
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

    public String get_messages() {
        return MakeURLCall("/messages", "GET", "");
    }

    public String MakeURLCall(String mainurl, String method, String jpayload) {

        try {
            if (method.equals("GET")) {
                return Unirest.get("http://zipcode.rocks:8085" + mainurl).asString().getBody();
            }
        }catch (UnirestException e){
            e.printStackTrace();
        }
            return null;
    }


}
