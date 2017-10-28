package cn.com.satum.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.wsdl.Message;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;
import javax.xml.soap.Name;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.PrefixedQName;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;



public class URLConnection {
private String url="";
    
    public String geturl() {
        return url;
    }
    public static HttpURLConnection getConnection(String url){
        HttpURLConnection connection = null;
        try {
            // 打开和URL之间的连接
            URL postUrl = new URL(url);
            connection = (HttpURLConnection) postUrl.openConnection();
             // 设置通用的请求属性
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("Accept-Charset", "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return connection;
    }
    public String getHttpRespone(String urls) throws IOException {
        String line = "";
        String httpResults = "";
        
        try {
            HttpURLConnection connection = URLConnection.getConnection(urls);
            
            DataOutputStream out = null;
            // 建立实际的连接
            connection.connect();
            out = new DataOutputStream(connection.getOutputStream());
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                httpResults = httpResults + line.toString();
            }
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResults;
    }
   	 
}
