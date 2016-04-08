package misszero;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class FileDownLoader {

    /**根据 url 和网页类型生成需要保存的网页的文件名
     *去除掉 url 中非文件名字符
     */
    public  String getFileNameByUrl(String url,String contentType)
    {
        url=url.substring(7);//remove http://
        if(contentType.indexOf("html")!=-1)//text/html
        {
            url= url.replaceAll("[\\?/:*|<>\"]", "_")+".html";
            return url;
        }
        else//如application/pdf
        {
            return url.replaceAll("[\\?/:*|<>\"]", "_")+"."+ contentType.substring(contentType.lastIndexOf("/")+1);
        }
    }

    /**保存网页字节数组到本地文件
     * filePath 为要保存的文件的相对地址
     */
    private void saveToLocal(byte[] data,String filePath)
    {
        try {
            DataOutputStream out=new DataOutputStream(
                    new FileOutputStream(new File(filePath)));
            for(int i=0;i<data.length;i++)
                out.write(data[i]);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*下载 url 指向的网页*/
    public String  downloadFile(String url)
    {
        String filePath=null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;

		  /*3.执行 HTTP GET 请求*/
        try {
            //判断访问的状态码

            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                System.err.println("Method failed: "+ response.getStatusLine());
                filePath = null;
            } else {
                HttpEntity entity = response.getEntity();
                byte[] responseBody = EntityUtils.toByteArray(entity);

                //根据网页 url 生成保存时的文件名
                String type = response.getHeaders("Content-Type")[0].getValue();
                filePath = "E:\\MyProjects\\exports\\" + getFileNameByUrl(url, type);

                saveToLocal(responseBody, filePath);
            }

        } catch (Exception e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();

        } finally {
            // 释放连接
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }
    //测试的 main 方法
    public static void main(String[]args)
    {
        FileDownLoader downLoader = new FileDownLoader();
        downLoader.downloadFile("http://www.twt.edu.cn");
    }
}