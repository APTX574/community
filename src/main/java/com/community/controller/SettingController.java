package com.community.controller;

import com.community.entity.User;
import com.community.service.SettingServer;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.logging.FileHandler;

/**
 * @author aptx
 */
@Controller
public class SettingController implements CommunityConstant {

    @Autowired
    SettingServer settingServer;

    @Autowired
    private HostHolder hostHolder;

    @Value("${community.path.upload}")
    String uploadPath;

    @Value("${community.path.doMain}")
    String doMain;

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @RequestMapping(path = "/upload" ,method = RequestMethod.POST)
    public String upload(MultipartFile image,Model model){
        if(image==null){
            model.addAttribute("error","您还未上传图片");
            return "/site/setting";
        }
        String originalFilename = image.getOriginalFilename();
        if(originalFilename!=null){
            String fileType=originalFilename.substring(originalFilename.lastIndexOf('.'));
            if(StringUtils.isBlank(fileType)){
                model.addAttribute("error","文件格式错误");
                return "/site/setting";
            }
            String fileName= CommunityUtil.generateUUID().substring(10).toLowerCase(Locale.ROOT)+fileType;
            File file=new File(uploadPath+"/"+fileName);
            try {
                image.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
                //TODO logger 文件上传失败
                throw new RuntimeException("上传文件失败，服务器内部异常",e);
            }

        }
        model.addAttribute("error","文件格式错误");
        return "/site/setting";

    }

    @RequestMapping(path = "/change/pwd", method = RequestMethod.POST)
    public String changePassword(String newPwd, String oldPwd, Model model) {
        User user = hostHolder.getUser();
        Map<String, Object> map = settingServer.changePwd(user, newPwd, oldPwd);
        int result = (int) map.get("msg");
        if (result == UN_LOGIN) {
            model.addAttribute("msg", "尚未登录，请登录");
            model.addAttribute("target", "/login");
            return "/site/operate-result";
        }
        if (result == CHANGE_PWD_SUCCESS) {
            model.addAttribute("msg", "密码修改成功,请重新登录");
            model.addAttribute("target", "/login");
            return "/site/operate-result";
        }
        if (result == CHANGE_PWD_ERROR_OLD_PWD) {
            model.addAttribute("passwordMsg", "密码错误");
            return "/site/setting";
        }
        return "/site/setting";
    }


}
