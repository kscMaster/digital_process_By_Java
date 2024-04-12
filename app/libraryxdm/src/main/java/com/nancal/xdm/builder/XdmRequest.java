package com.nancal.xdm.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.innovation.rdm.coresdk.basic.dto.AbstractViewDTO;
import com.huawei.innovation.rdm.coresdk.basic.util.XDMDelegatorJsonUtil;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMErrorVO;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMParamVO;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMResultVO;
import com.huawei.innovation.rdm.dto.entity.TenantViewDTO;
import com.nancal.common.base.TableRequest;
import com.nancal.common.constants.CacheConstant;
import com.nancal.common.constants.Constant;
import com.nancal.common.enums.ActionEnum;
import com.nancal.config.XdmConfig;
import com.nancal.redis.service.RedisService;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/***
 * 获取XDM的请求
 *
 * @author 徐鹏军
 * @date 2022/9/30 10:43
 */
public class XdmRequest {


    /***
     * 获取获取token的请求参数
     *
     * @author 徐鹏军
     * @date 2022/9/30 10:33
     * @return {@link String}
     */
    private static String getTokenParam() {
        XdmConfig config = SpringUtil.getBean(XdmConfig.class);
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> authMap = new HashMap<>();
        Map<String, Object> identityMap = new HashMap<>();
        identityMap.put("methods", Collections.singletonList("password"));
        Map<String, Object> passwordMap = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        Map<String, Object> domainMap = new HashMap<>();
        domainMap.put("name", config.getDomainName());
        userMap.put("name", config.getUserName());
        userMap.put("password", config.getPassword());
        userMap.put("domain", domainMap);
        passwordMap.put("user", userMap);
        identityMap.put("password", passwordMap);
        authMap.put("identity", identityMap);
        Map<String, Object> scopeMap = new HashMap<>();
        scopeMap.put("domain", domainMap);
        authMap.put("scope", scopeMap);
        paramMap.put("auth", authMap);
        return JSON.toJSONString(paramMap);
    }

    /***
     * 获取XDM的token
     *
     * @author 徐鹏军
     * @date 2022/9/30 10:44
     * @return {@link String}
     */
    private static String getToken() {
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        XdmConfig config = SpringUtil.getBean(XdmConfig.class);
        String redisKey = CacheConstant.XDM_TOKEN + config.getUserName();
        Object redisToken = redisService.getCacheObject(redisKey);
        if (Objects.nonNull(redisToken)) {
            return redisToken.toString();
        }
        RestTemplate template = SpringUtil.getBean(RestTemplate.class);
        String param = getTokenParam();
        String url = "https://iam.myhuaweicloud.com/v3/auth/tokens";
        ResponseEntity<String> response = template.postForEntity(url, param, String.class);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("获取XDM的token异常：" + JSON.toJSONString(response));
        }
        String token = response.getHeaders().getFirst("X-Subject-Token");
        if (StrUtil.isBlank(token)) {
            throw new RuntimeException("获取XDM的token为空：" + JSON.toJSONString(response.getHeaders()));
        }
        JSONObject root = JSON.parseObject(response.getBody());
        JSONObject parent = root.getJSONObject("token");
        String expiresAt = parent.getString("expires_at");
        DateTime dateTime = DateUtil.parse(expiresAt, "yyyy-MM-dd'T'HH:mm:ss");
        Date date = dateTime.toJdkDate();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTimeUtil.of(date);
        long second = LocalDateTimeUtil.between(now, end, ChronoUnit.SECONDS);
        redisService.setCacheObject(CacheConstant.XDM_TOKEN + config.getUserName(), token, second, TimeUnit.SECONDS);
        return token;
    }

    /***
     * 获取请求url
     *
     * @author 徐鹏军
     * @date 2022/9/30 11:02
     * @return {@link String}
     */
    private static String getUrl(String objectType, ActionEnum action) {
        XdmConfig config = SpringUtil.getBean(XdmConfig.class);
        if (StrUtil.endWith(objectType,Constant.ENTITY)){
            objectType = StrUtil.removeSuffix(objectType,Constant.ENTITY);
        } else {
            objectType = StrUtil.addSuffixIfNot(objectType,Constant.ENTITY);
        }
        String urlPrefix = StrUtil.join(StrUtil.SLASH, config.getDomain(), config.getAppId(), "publicservices", "api");
        // 如果是查询类的接口请求，url后面需要拼接一些分页的属性
        boolean anyMatch = Stream.of(ActionEnum.find, ActionEnum.query, ActionEnum.list, ActionEnum.select, ActionEnum.getAllVersions)
                .anyMatch(data -> data == action);
        if (anyMatch) {
            return StrUtil.join(StrUtil.SLASH, urlPrefix, objectType, action.name(), "{pageSize}", "{curPage}");
        }
        return StrUtil.join(StrUtil.SLASH, urlPrefix, objectType, action.name());
    }

    /***
     * post请求
     *
     * @param objectType 针对哪个模型来请求
     * @param action 请求的操作，增删改查...
     * @param param  请求参数
     * @param respClass 响应对象
     * @author 徐鹏军
     * @date 2022/9/30 14:07
     * @return {@link List<T>}
     */
    public static <T extends AbstractViewDTO> List<T> postForObject(String objectType, ActionEnum action, Object param, Class<T> respClass) {
        return execute(HttpMethod.POST, objectType, action, param, null,respClass);
    }

    /***
     * 带分页的post请求，分页的参数是拼接在URL后面的
     *
     * @param objectType 针对哪个模型来请求
     * @param action 请求的操作，增删改查...
     * @param param  请求参数
     * @param tableRequest 分页数据
     * @param respClass 响应对象
     * @author 徐鹏军
     * @date 2022/9/30 14:07
     * @return {@link List<T>}
     */
    public static <T extends AbstractViewDTO> List<T> postForObject(String objectType, ActionEnum action, Object param, TableRequest<?> tableRequest, Class<T> respClass) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("pageSize", tableRequest.getTake());
        uriVariables.put("curPage", tableRequest.getSkip());
        return execute(HttpMethod.POST, objectType, action, param, uriVariables, respClass);
    }
    /***
     * 获取当前登录用户的基本信息
     * <p>
     *     {
     *     "result": "SUCCESS",
     *     "data": [
     *         {
     *             "id": "-1",
     *             "creator": "xdmAdmin",
     *             "modifier": "xdmAdmin",
     *             "createTime": "2022-09-14T03:31:09.000+0000",
     *             "lastUpdateTime": "2022-09-14T03:31:09.000+0000",
     *             "rdmVersion": 1,
     *             "rdmDeleteFlag": 0,
     *             "rdmExtensionType": "Tenant",
     *             "tenant": null,
     *             "className": "Tenant",
     *             "name": "basicTenant",
     *             "description": "默认租户",
     *             "kiaguid": null,
     *             "securityLevel": "internal",
     *             "code": "basicTenant",
     *             "disableFlag": false,
     *             "dataSource": null
     *         }
     *     ],
     *     "errors": []
     * }
     * </p>
     * @author 徐鹏军
     * @date 2022/10/8 15:26
     * @return {@link TenantViewDTO}
     */
    public static TenantViewDTO getCurrentUser() {
        TableRequest<Object> request = new TableRequest<>();
        request.setSkip(1);
        request.setTake(1);
        List<TenantViewDTO> post = postForObject("TenantEntity", ActionEnum.find, null, request, TenantViewDTO.class);
        return CollUtil.getFirst(post);
    }

    /***
     * 远程调用方法
     *
     * @param method 请求方式
     * @param objectType 针对哪个模型来请求
     * @param action 请求的操作，增删改查...
     * @param param 请求参数
     * @param uriVariables 跟在请求url后面的变量值
     * @param respClass 响应对象
     * @author 徐鹏军
     * @date 2022/9/30 13:54
     * @return {@link List<T>}
     */
    public static <T extends AbstractViewDTO> List<T> execute(HttpMethod method, String objectType, ActionEnum action, Object param,
                                                              Map<String, Object> uriVariables, Class<T> respClass) {
        RestTemplate template = SpringUtil.getBean(RestTemplate.class);
        XdmConfig config = SpringUtil.getBean(XdmConfig.class);
        String token = getToken();
        String url = getUrl(objectType, action);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.REFERER, config.getDomain());
        headers.add(HttpHeaders.ORIGIN, config.getDomain());
        headers.add("X-Auth-Token", token);
        RDMParamVO paramVO = new RDMParamVO();
        paramVO.setParams(param);
        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(paramVO), headers);
        ResponseEntity<RDMResultVO> response = template.exchange(url, method, requestEntity, RDMResultVO.class, uriVariables);
        RDMResultVO responseBody = response.getBody();
        if (!CollectionUtils.isEmpty(responseBody.getErrors())) {
            List<RDMErrorVO> errorVOList = responseBody.getErrors();
            StringBuffer messages = new StringBuffer();
            errorVOList.forEach(errorVO -> messages.append(errorVO.getMessage()).append(";"));
            throw new RuntimeException((errorVOList.get(0)).getCode() + StrUtil.LF + messages);
        }
        if (CollUtil.isEmpty(responseBody.getData())) {
            return Collections.emptyList();
        }
        return XDMDelegatorJsonUtil.jsonToDTOList(responseBody.getData(), respClass, Constant.PATTERN);
    }
}
