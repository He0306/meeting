package com.rookie.rookiemeeting.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.aip.face.AipFace;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.dto.LoginDto;
import com.rookie.rookiemeeting.entity.Employee;
import com.rookie.rookiemeeting.service.IEmployeeService;
import com.rookie.rookiemeeting.service.IMeetingParticipantsService;
import com.rookie.rookiemeeting.service.IMenuService;
import com.rookie.rookiemeeting.util.BASE64;
import com.rookie.rookiemeeting.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "人脸识别控制类")
public class PhotoController {

    @Autowired
    IEmployeeService employeeService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IMenuService menuService;
    @Autowired
    IMeetingParticipantsService meetingParticipantsService;
    //人脸信息储存位置
    @Value("${files.upload.path}")
    private String fileUploadPath;
    //临时照片存储位置
    @Value("${photo.tmp}")
    private String tmpPhoto;
    //百度人脸识别接口
    @Autowired
    private AipFace aipFace;

    /**
     * 人脸登录
     *
     * @param file
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ApiOperation("人脸登录接口")
    @PostMapping("/faceLogin")
    public Result faceLogin(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<>();
        options.put("max_face_num", "3");
        options.put("match_threshold", "80");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "NONE");
        options.put("max_user_num", "3");

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile headerImage = multipartRequest.getFile("file");
        String fileName = headerImage.getOriginalFilename();

        //根据当前时间生成文件名
        fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "." + fileName;
        // 确定文件存放的路径
        File dest = new File(tmpPhoto + fileName);
        //判断当前目录是否存在
        if (!dest.exists()) {
            //目录不存在，需要创建
            dest.mkdir();
        }
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        String imgage = BASE64.encodeImgageToBase64(dest);
        String imageType = "BASE64";
        String groupIdList = "employee";

        // 人脸搜索 M:N 识别
        JSONObject res = aipFace.multiSearch(imgage, imageType, groupIdList, options);
        System.out.println(res);
        int id;
        LoginDto loginDto = new LoginDto();
        try {
            id = res.getJSONObject("result").getJSONArray("face_list")
                    .getJSONObject(0).getJSONArray("user_list")
                    .getJSONObject(0).getInt("user_id");
            Employee employee = employeeService.getById(id);
            BeanUtil.copyProperties(employee, loginDto, true);
            String jwt = TokenUtils.genToken(employee.getEmployeeid().toString(), employee.getPassword());
            redisTemplate.opsForValue().set("JWT_" + jwt, JSON.toJSON(employee), 2, TimeUnit.HOURS);
            loginDto.setToken(jwt);
            loginDto.setMenus(menuService.getRoleMenu(employee.getRole()));

        } catch (Exception e) {
            return Result.fail("登录失败，请刷脸登录");
        }

        return Result.succ(loginDto);
    }

    /**
     * 人脸照片录入
     *
     * @param file
     * @param request
     * @param employeeid
     * @return
     * @throws IOException
     */
    @ApiOperation("人脸照片录入接口")
    @PostMapping("/registered")
    public Result registered(@RequestParam("file") MultipartFile file,
                             HttpServletRequest request,
                             @RequestParam("employeeid") String employeeid) throws IOException {

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user_info", "user's info");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "NONE");
        options.put("action_type", "REPLACE");

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile headerImage = multipartRequest.getFile("file");
        String fileName = headerImage.getOriginalFilename();

        //根据当前时间生成文件名
        fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "." + fileName;
        // 确定文件存放的路径
        File dest = new File(fileUploadPath + fileName);
        //判断当前目录是否存在
        if (!dest.exists()) {
            //目录不存在，需要创建
            dest.mkdir();
        }
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        //转换成base64
        String imgage = BASE64.encodeImgageToBase64(dest);
        String imageType = "BASE64";
        String groupId = "employee";
        String userId = employeeid;

        // 人脸注册
        JSONObject res = aipFace.addUser(imgage, imageType, groupId, userId, options);

        return Result.succ(fileName);
    }

    /**
     * 签到
     *
     * @param file
     * @param request
     * @param employeeid
     * @param meetingid
     * @param location
     * @return
     */
    @ApiOperation("签到接口")
    @PostMapping("/SingIn")
    public Result SingIn(@RequestParam("file") MultipartFile file,
                         HttpServletRequest request,
                         @RequestParam Long employeeid,
                         @RequestParam Integer meetingid,
                         @RequestParam String location) {

        //传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<>();
        options.put("max_face_num", "3");
        options.put("match_threshold", "80");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "NONE");
        options.put("max_user_num", "3");

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile headerImage = multipartRequest.getFile("file");
        String fileName = headerImage.getOriginalFilename();

        //根据当前时间生成文件名
        fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "." + fileName;
        // 确定文件存放的路径
        File dest = new File(tmpPhoto + fileName);
        //判断当前目录是否存在
        if (!dest.exists()) {
            //目录不存在，需要创建
            dest.mkdir();
        }
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        String imgage = BASE64.encodeImgageToBase64(dest);
        String imageType = "BASE64";
        String groupIdList = "employee";

        // 人脸搜索 M:N 识别
        JSONObject res = aipFace.multiSearch(imgage, imageType, groupIdList, options);

        int id;
        Employee employee = null;
        try {
            id = res.getJSONObject("result").getJSONArray("face_list")
                    .getJSONObject(0).getJSONArray("user_list")
                    .getJSONObject(0).getInt("user_id");
            employee = employeeService.getById(id);
            if (employee == null) {
                return Result.fail(400, "签到失败，请重新上传图片或者刷脸", null);
            }
            if (!employee.getEmployeeid().equals(employeeid)) {
                return Result.fail(400, "签到失败，请重新上传图片或者刷脸", null);
            } else {
                meetingParticipantsService.singIn(employeeid, meetingid, location);
                return Result.succ("恭喜您签到成功");
            }
        } catch (Exception e) {
            return Result.fail("签到失败，请重新刷脸");
        }
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @ApiOperation("文件下载接口")
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(fileUploadPath + name);
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
