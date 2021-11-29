package volley;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface ActivityCallbackInterfaceGet {
    void getResultBack(JSONObject jsonObject, int resultCode) throws JSONException;
    void volleyErrorMessage(VolleyError error, int resultCode);
}
