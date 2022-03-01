package com.sillylab.eureka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EurekaServerApplicationTests.Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, value = {
        "spring.jmx.enabled=true", "management.security.enabled=false"})
public class EurekaServerApplicationTests {

    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    private int port = 0;

    @Test
    public void testEurekaApps() throws Exception {
        getAllApps();
    }

    @Test
    public void testEureka() throws InterruptedException {
        //注册
        String appName = "eureka-client";
        String ip = "192.168.0.117";
        Integer appPort = 20001;
        register(appName, ip, appPort);
        //register(appName+"2", ip, appPort);
        //查下所有应用
        getAllApps();
        //根据appId查下
        getByAppId(appName);
        //根据appId和instanceId查询
        getByAppIdAndInstanceId(appName, ip + ":" + appName + ":" + appPort);
        //根据instanceId查询
        getByInstanceId(ip + ":" + appName + ":" + appPort);
        //暂停/下线应用实例
        pauseByInstanceId(appName, ip + ":" + appName + ":" + appPort);
        getByInstanceId(ip + ":" + appName + ":" + appPort);
        //恢复应用实例
        resumeByInstanceId(appName, ip + ":" + appName + ":" + appPort);
        getByInstanceId(ip + ":" + appName + ":" + appPort);
        //应用实例发送心跳
        sendHeartBeatByInstanceId(appName, ip + ":" + appName + ":" + appPort);
        getByInstanceId(ip + ":" + appName + ":" + appPort);
        //修改应用实例元数据
        modifyMetadataByInstanceId(appName, ip + ":" + appName + ":" + appPort,
                "profile", "prod");
        getByInstanceId(ip + ":" + appName + ":" + appPort);
        //注销应用实例
        unRegister(appName, ip + ":" + appName + ":" + appPort);
        try{
            getByInstanceId(ip + ":" + appName + ":" + appPort);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void unRegister(String appName, String instanceId) {
        String url = "http://localhost:" + port + "/eureka/apps/" + appName + "/" + instanceId;
        System.out.println("unRegister: "+url);
        restTemplate.delete(url);
    }

    private void modifyMetadataByInstanceId(String appName, String instanceId, String key, String value) {
        String url = "http://localhost:" + port + "/eureka/apps/" + appName + "/" + instanceId
                +"/metadata?"+key+"="+value;
        System.out.println("modifyMetadataByInstanceId: "+url);
        restTemplate.put(url, null);
    }

    private void sendHeartBeatByInstanceId(String appName, String instanceId) {
        String url = "http://localhost:" + port + "/eureka/apps/" + appName + "/" + instanceId;
        System.out.println("sendHeartBeatByInstanceId: "+url);
        restTemplate.put(url, null);
    }

    private void resumeByInstanceId(String appName, String instanceId) {
        String url = "http://localhost:" + port + "/eureka/apps/" + appName + "/" + instanceId
                + "/status?value=UP";
        System.out.println("resumeByInstanceId: "+url);
        restTemplate.put(url, null);
    }


    private void getAllApps() {
        String url = "http://localhost:" + port + "/eureka/apps";
        ResponseEntity<String> entity = restTemplate.getForEntity(
                url, String.class);
        printEntity(url, entity);
    }

    private void getByAppId(String appId) {
        String url = "http://localhost:" + port + "/eureka/apps/" + appId;
        ResponseEntity<String> entity = restTemplate.getForEntity(
                url, String.class);
        printEntity(url, entity);
    }

    private void getByAppIdAndInstanceId(String appName, String instanceId) {
        String url = "http://localhost:" + port + "/eureka/apps/"+appName+"/" + instanceId;
        ResponseEntity<String> entity = restTemplate.getForEntity(
                url, String.class);
        printEntity(url, entity);
    }

    private void getByInstanceId(String instanceId) {
        String url = "http://localhost:" + port + "/eureka/instances/" + instanceId;
        ResponseEntity<String> entity = restTemplate.getForEntity(
                url, String.class);
        printEntity(url, entity);
    }


    private void pauseByInstanceId(String app, String instanceId) {
        String url = "http://localhost:" + port + "/eureka/apps/" + app + "/" + instanceId
                + "/status?value=OUT_OF_SERVICE";
        System.out.println("pauseByInstanceId: "+url);
        restTemplate.put(url,null);
    }

    private void register(String appName, String ip, Integer appPort) {
        String registerBody = "<instance>\n" +
                "<instanceId>" + ip + ":" + appName + ":" + appPort + "</instanceId>\n" +
                "<hostName>" + ip + "</hostName>\n" +
                "<app>" + appName.toUpperCase() + "</app>\n" +
                "<ipAddr>" + ip + "</ipAddr>\n" +
                "<status>UP</status>\n" +
                "<overriddenstatus>UNKNOWN</overriddenstatus>\n" +
                "<port enabled=\"true\">" + appPort + "</port>\n" +
                "<securePort enabled=\"false\">443</securePort>\n" +
                "<countryId>1</countryId>\n" +
                "<dataCenterInfo class=\"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\">\n" +
                "<name>MyOwn</name>\n" +
                "</dataCenterInfo>\n" +
                "<metadata>\n" +
                "<management.port>" + appPort + "</management.port>\n" +
                "</metadata>\n" +
                "<vipAddress>eureka-client</vipAddress>\n" +
                "<secureVipAddress>eureka-client</secureVipAddress>\n" +
                "<isCoordinatingDiscoveryServer>false</isCoordinatingDiscoveryServer>\n" +
                "<lastUpdatedTimestamp>" + System.currentTimeMillis() + "</lastUpdatedTimestamp>\n" +
                "<lastDirtyTimestamp>" + System.currentTimeMillis() + "</lastDirtyTimestamp>\n" +
                "</instance>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> req = new HttpEntity(registerBody, headers);
        //http://localhost:10001/eureka/apps/EUREKA-CLIENT
        String url = "http://localhost:" + port + "/eureka/apps/" + appName;
        ResponseEntity<String> entity = restTemplate.postForEntity(
                url, req, String.class);
        printEntity(url, entity);
    }

    private void printEntity(String url, ResponseEntity<String> entity) {
        System.out.println("URL: " + url);
        System.out.println("BODY: " + entity.getBody());
        System.out.println("STATUS: " + entity.getStatusCode());
        System.out.println("HEADERS: " + entity.getHeaders());
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableEurekaServer
    protected static class Application {
        public static void main(String[] args) {
            new SpringApplicationBuilder(Application.class)
                    .properties("spring.application.name=eureka")
                    .run(args);
        }
    }
}
