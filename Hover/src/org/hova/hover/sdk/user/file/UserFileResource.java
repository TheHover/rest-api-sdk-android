package org.hova.hover.sdk.user.file;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import org.hova.hover.sdk.http.ClientGETFileAsync;
import org.hova.hover.sdk.http.ClientGETFileAsync.getRequestFileExecute;
import org.hova.hover.sdk.http.ResponseFile;
import org.hova.hover.sdk.pojo.UserFile;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class UserFileResource implements getRequestFileExecute {

	private String URI = "/user/file";
	FileResource file_resource;
	private ClientGETFileAsync cGETFileAsync;

	public interface FileResource {
		void onGetFile(ResponseFile responseFile);
	}

	public UserFileResource(FileResource fr) {
		file_resource = fr;
	}

	public void getFile(UserFile usr_file) throws UnsupportedEncodingException {
		cGETFileAsync = new ClientGETFileAsync(this);
		cGETFileAsync.execute(URI, checkForNewsAttributes(usr_file));

	}

	protected String checkForNewsAttributes(UserFile uf)
			throws UnsupportedEncodingException {

		String queryString = "";
		try {
			Gson gson = new Gson();
			JSONObject jsonObj = new JSONObject(gson.toJson(uf));
			Iterator<?> keysItr = jsonObj.keys();
			while (keysItr.hasNext()) {
				String key = (String) keysItr.next();
				Object value = jsonObj.get(key);

				if (key.equalsIgnoreCase("page") || key.equalsIgnoreCase("pagination")) {
					int i = (Integer) value;
					if (i != 0)
						if (queryString.equalsIgnoreCase(""))
							queryString = "?" + key + "=" + value;
						else
							queryString = queryString + "&" + key + "=" + value;
				} else {
					if (queryString.equalsIgnoreCase(""))
						queryString = "?" + key + "=" + URLEncoder.encode(value.toString(), "UTF-8");
					else
						queryString = queryString + "&" + key + "=" + URLEncoder.encode(value.toString(), "UTF-8");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return queryString;
	}

	@Override
	public void doGetFileExecute(ResponseFile responseFile) {
		file_resource.onGetFile(responseFile);

	}

	public void cancelRequest() {
		if (cGETFileAsync != null) {
			cGETFileAsync.cancelRequest();
			cGETFileAsync.cancel(true);
		} else {
		}
	}

}
