package com.community.controller;

import com.community.annotation.LoginRequired;
import com.community.entity.User;
import com.community.service.SettingServer;
import com.community.service.UserServer;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * @author aptx
 */
@Controller
public class SettingController implements CommunityConstant {

    @Autowired
    SettingServer settingServer;

    @Autowired
    UserServer userServer;

    @Autowired
    private HostHolder hostHolder;

    @Value("${community.path.upload}")
    String uploadPath;

    @Value("${community.path.domain}")
    String doMain;

    /**
     * 获取设置界面 需要登录
     *
     * @return 页面模板
     * @RequestPath /setting
     * @ResquestMethod Get
     */
    @LoginRequired
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * 修改头像
     *
     * @param image 用户上传到头像
     * @param model 模板数据
     * @return 模板页面
     * @RequestPath /change/header
     * @ResquestMethod Get
     */
    @LoginRequired
    @RequestMapping(path = "/change/header", method = RequestMethod.POST)
    public String upload(MultipartFile image, Model model) {

        //判断是否上传图片
        if (image == null) {
            model.addAttribute("error", "您还未上传图片");
            return "/site/setting";
        }

        //拼接在服务器的访问路径和用户名
        String originalFilename = image.getOriginalFilename();
        if (originalFilename != null) {
            String fileType = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
            if (StringUtils.isBlank(fileType)) {
                model.addAttribute("error", "文件格式错误");
                return "/site/setting";
            }
            String fileName = CommunityUtil.generateUUID().substring(10).toLowerCase(Locale.ROOT) + "." + fileType;
            File file = new File(uploadPath + "/" + fileName);
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("创建文件失败");
                }
                //将图片输出为文件
                //本质为将input输出输入到output
                image.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
                //TODO logger 文件上传失败
                throw new RuntimeException("上传文件失败，服务器内部异常", e);
            }
            //拼接外部访问的访问路径
            //http://localhost:8080/user/header/xxx.png
            String headerUrl = doMain + "/user/header/" + fileName;
            userServer.changeHeaderUrl(hostHolder.getUser().getId(), headerUrl);
            return "redirect:/index";
        }
        model.addAttribute("error", "文件格式错误");
        return "/site/setting";

    }


    /**
     * 获取头像
     *
     * @param response 响应对象用于输出头像
     * @param imageName 头像文件名
     * @RequestPath /user/head/{imageName}
     * @ResquestMethod Get
     */
    @RequestMapping(path = "/user/header/{imageName}", method = RequestMethod.GET)
    public void getHeader(HttpServletResponse response, @PathVariable("imageName") String imageName) {
        File file = new File(uploadPath + "/" + imageName);
        String suffix = imageName.substring(imageName.lastIndexOf(".") + 1);
        //设置响应格式
        response.setContentType("image/" + suffix);
        //获取输入流与输出流
        try (FileInputStream inputStream = new FileInputStream(file)) {
            ServletOutputStream outputStream = response.getOutputStream();
            //进行文件传输
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改密码
     * 需要登录
     * @param newPwd 新的密码明文
     * @param oldPwd 旧的密码明文
     * @param model 数据模型
     * @return 页面路径
     * @RequestPath /change/pwd
     * @ResquestMethod Post
     */
    @LoginRequired
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
