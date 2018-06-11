package kr.ac.dongseo.foreignstudnet_helper;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayInputStream;

public class HttpConnectionManager
{
    public static final String SERVER_URL = "http://104.198.117.85:3333";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private Context context;
    private SessionManager smgr;

    public HttpConnectionManager(Context context) {
        this.context = context;
        smgr = new SessionManager(context);
    }

    //POST 로그인
    public void login(String mail, String pw, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("mail", mail);
        params.put("password", pw);
        String loginURL = SERVER_URL + "/login";
        client.setCookieStore(smgr.myCookies);
        client.post(loginURL, params, handler);
    }

    //POST 회원가입
    public void register(String mail, String pw, String name, String phone, String country,
                       String language, int helper, // byte[] byteImage,
                       JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("mail", mail);
        params.put("password", pw);
        params.put("name", name);
        params.put("phone", phone);
        params.put("country", country);
        params.put("language", language);
        params.put("helper", helper);

        /*if (byteImage != null) {
            params.put("profPic", new ByteArrayInputStream(byteImage), "profPic.PNG");
        }*/
        /*
        try {
            params.put("profPic", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */

        String registerURL = SERVER_URL + "/register";
        client.post(registerURL, params, handler);
    }

    // GET 유저 정보 가져오기, using only cookies
    /*public void getUserInfo(JsonHttpResponseHandler handler) {
        String getInfoURL = SERVER_URL + "/getUserInfo";
        client.setCookieStore(smgr.myCookies);
        client.get(getInfoURL, handler);
    }*/

    // POST 유저 검색
    /*public void searchUser(String queryid, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", queryid);
        client.setCookieStore(smgr.myCookies);
        String searchUserURL = SERVER_URL + "//searchUserDetails";
        client.post(searchUserURL, params, handler);
    }*/

    // POST 유저 정보 업데이트 및 프로필 사진
    /*public void updateUserInfo(String mail, String pw, String name, String phone, String country,
                               String language, int helper, // byte[] byteImage,
                               JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("mail", mail);
        params.put("password", pw);
        params.put("name", name);
        params.put("phone", phone);
        params.put("country", country);
        params.put("language", language);
        params.put("helper", helper);

        //if (byteImage != null) {
        //    params.put("profPic", new ByteArrayInputStream(byteImage), "profPic.PNG");
        //}
        client.setCookieStore(smgr.myCookies);
        String updateUserInfoURL = SERVER_URL + "/updateUserInfo";
        client.post(updateUserInfoURL, params, handler);
    }*/

    /*
     * Match related http functions
     */

    // POST 매칭 신청
    public void requestMatch(String mail, String title, String content,
                             JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("mail", mail);
        params.put("title", title);
        params.put("content", content);
        client.setCookieStore(smgr.myCookies);
        String requestMatchURL = SERVER_URL + "/reqmatch";
        client.post(requestMatchURL, params, handler);
    }

    // GET 현재 유저의 매치 정보 수신 (using mail)
    public void getUserMatch(String mail, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("mail", mail);
        String getUserMatchURL = SERVER_URL + "/mylist";
        client.setCookieStore(smgr.myCookies);
        client.get(getUserMatchURL, handler);
    }

    // GET 모든 대기중인 매칭 리스트 수신
    public void getAllMatch(JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        String getAllMatchURL = SERVER_URL + "/list";
        client.setCookieStore(smgr.myCookies);
        client.get(getAllMatchURL, handler);
    }

    // POST 매칭 종료 (헬퍼가 매칭 종료)
    public void deleteMatch(String helperMail, String helperMatchId,
                            JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("mail", helperMail);
        params.put("id", helperMatchId);
        String quitMatchURL = SERVER_URL + "/endmatch";
        client.setCookieStore(smgr.myCookies);
        client.post(quitMatchURL, params, handler);
    }

    // POST 매칭 결정 (수락 / 거절)
    public void decideMatch(String mail, int eventIdx, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("mail", mail);
        params.put("num", eventIdx);
        String decideMatchURL = SERVER_URL + "/resmatch";
        client.setCookieStore(smgr.myCookies);
        client.post(decideMatchURL, params, handler);
    }

    /*
     * Profile picture related functions
     */

    //GET
    //request parameter: id (String)
    //response: file
    /*public void getProfPic(String mail, FileAsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("mail", mail);
        String getProfPicURL = SERVER_URL + "/getProfileImage";
        client.setCookieStore(smgr.myCookies);
        client.get(getProfPicURL, params, handler);
    }*/
}
