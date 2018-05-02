package Http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 成刚 on 2016/4/5.
 */
public class WebService {
    private static String IP = "10.0.3.2:8080";
   /* private static String IP = "192.168.254.1:8080";*/
    /*private static String IP = "192.168.43.251:8080";*/

    private static HttpURLConnection conn = null;
    private static InputStream is = null;

    //手机号+密码 登录功能
    public static String executeHttpGet(String username, String password) {

        String path = "http://" + IP + "/MyProject/LogLet";
        path = path + "?username=" + username + "&password=" + password;

        return getConnection(path);
    }

    //根据手机号查找用户
    public static String executeHttpFindUserByPhone(String username) {

        String path = "http://" + IP + "/MyProject/FindUserLet";
        path = path + "?username=" + username;

        return getConnection(path);
    }

    // 传入课程难度直接下载数据库课程信息通过Get方式获取HTTP服务器数据，再后来修改传参，可以控制要下载的课程
    public static String executeHttpGetCourse(String hard) {

        String path = "http://" + IP + "/MyProject/GetCourseLet";
        path = path + "?hard=" + hard;

        return getConnection(path);
    }

    //密码修改功能，传入手机号+新密码
    public static String executeHttpNewPwd(String username, String password) {

        String path = "http://" + IP + "/MyProject/PwdLet";
        path = path + "?username=" + username + "&password=" + password;

        return getConnection(path);
    }

    // 注册功能，传入手机号+密码
    public static String executeHttpRegister(String username, String password) {

        String path = "http://" + IP + "/MyProject/RegLet";
        path = path + "?username=" + username + "&password=" + password;

        return getConnection(path);
    }

    // 注册功能，传入手机号+意见
    public static String executeHttpFeedBack(String username, String suggestion) {

        String path = "http://" + IP + "/MyProject/FeedBackLet";
        path = path + "?username=" + username + "&suggestion=" + suggestion;

        return getConnection(path);
    }

    //传入手机号获取用户关注的课程
    public static String executeHttpGetUserCourseInfo(String username) {

        String path = "http://" + IP + "/MyProject/GetUserCourseInfoLet";
        path = path + "?username=" + username;

        return getConnection(path);
    }

    // 传入手机号+课程，修改用户关注课程的信息
    public static String executeHttpSendUserCourseInfo(String username,String jsonstr) {
        String path = "http://" + IP + "/MyProject/SendUserCourseInfoLet";
        path = path + "?jsonstr=" + jsonstr+ "&username=" + username;

        return getConnection(path);
    }

    //传入手机号获取用户测试记录
    public static String executeHttpGetUserTestInfo(String username) {

        String path = "http://" + IP + "/MyProject/GetUserTestInfoLet";
        path = path + "?username=" + username;

        return getConnection(path);
    }

    // 传入手机号+课程+时间+难度+得分
    public static String executeHttpSendUserTestInfo(String username,String course,String time,int hard,String scores) {
        String path = "http://" + IP + "/MyProject/SendUserTestInfoLet";
        path = path + "?username=" + username + "&course=" + course+ "&time=" + time+ "&hard=" + hard+ "&scores=" + scores;

        return getConnection(path);
    }

    // 在线获取最新app的版本号
    public static String executeHttpGetVersion() {

        String path = "http://" + IP + "/MyProject/VersionLet";

        return getConnection(path);
    }

    //传入用户的详细资料，添加到数据库
    public static String executeHttpUserInfo(String username, String nickname, String introduce, String major, String email) {
        String path = "http://" + IP + "/MyProject/UserInfoLet";
        path = path + "?username=" + username + "&nickname=" + nickname + "&introduce=" + introduce + "&major=" + major + "&email=" + email;
        return getConnection(path);
    }

    public static String getConnection(String path) {
        try {
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parseInfo(is);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    // 将输入流转化为 String 型
    private static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

    // 将输入流转化为byte型
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}
