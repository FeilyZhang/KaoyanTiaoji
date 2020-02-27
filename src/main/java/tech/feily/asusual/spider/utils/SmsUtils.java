package tech.feily.asusual.spider.utils;

import org.apache.log4j.Logger;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

import com.tencentcloudapi.sms.v20190711.SmsClient;

import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;


/*
 * This class sends notification SMS to users through Tencent's SMS API。
 * @author Feily Zhang
 * @version v0.1
 * @email fei@feily.tech
 */
public class SmsUtils {


    static String secretId = "";
    static String secretKey = "";

    public static int[] send(String[] phones, String[] params, String templateID) {
        int length = 0, success = 0;
        Logger log = Logger.getLogger(SmsUtils.class);
        try {
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);

            SendSmsRequest req = new SendSmsRequest();
            String appid = "";
            req.setSmsSdkAppid(appid);
            String sign = "";
            req.setSign(sign);
            req.setTemplateID(templateID);
            req.setPhoneNumberSet(phones);
            req.setTemplateParamSet(params);

            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            SendSmsResponse res = client.SendSms(req);

            log.info(SendSmsResponse.toJsonString(res));
            log.info(res.getRequestId());

            SendStatus[] sendStatus = res.getSendStatusSet();
            length  = sendStatus.length;
            for (SendStatus status : sendStatus) {
                if (status.getCode().equalsIgnoreCase("OK")) {
                    success += 1;
                }
            }
        } catch (TencentCloudSDKException e) {
            log.info("短信发送异常，详情请查看\n" + e.getMessage());
        }
        return new int[] {length, success};
    }
    
}