package model.remote;

/**
 * Created by Zephyr on 5/30/2018.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://50.23.0.202:31090/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
