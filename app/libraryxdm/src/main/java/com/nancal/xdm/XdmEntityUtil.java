package com.nancal.xdm;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.huawei.innovation.rdm.bean.entity.BasicObject;
import com.huawei.innovation.rdm.hwkeymodeltest.bean.entity.BusinessObjectEntity;
import com.nancal.common.constants.Constant;
import com.nancal.common.utils.BeanUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class XdmEntityUtil {

    /***
     * 获取对象类型的全包名
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 12:53
     * @return {@link String}
     */
    public static String getEntityPackage(String objectType) {
        return ClassUtil.getPackage(BusinessObjectEntity.class) + StrUtil.DOT + StrUtil.addSuffixIfNot(objectType, Constant.ENTITY);
    }

    /***
     * 获取对象类型的全包名
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 12:53
     * @return {@link Class<?>}
     */
    public static Class<?> getEntityClass(String objectType) {
        String packagePath = getEntityPackage(objectType);
        return ClassUtil.loadClass(packagePath);
    }

    /***
     * 根据类型创建实体
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 12:53
     * @return {@link Class<?>}
     */
    public static <T extends BasicObject> T getObject(String objectType) {
        String packagePath = getEntityPackage(objectType);
        return ReflectUtil.newInstance(packagePath);
    }

    /**
     * 判断是否是零组件,用来区分workspace，如：工步
     * @param objectType
     * @return
     */
    public static boolean checkItem(String objectType){
        try{
            XdmEntityUtil.getEntityClass(XdmEntityUtil.getRevision(objectType));
            return true;
        }catch (Exception e){
            return false;
        }catch (NoClassDefFoundError e){
            return false;
        }
    }

    /**
     * req转entity
     *
     * @param req
     * @return
     */
    public static BasicObject reqToEnity(BasicObject req) {
        String objectType = StrUtil.isNotBlank(req.getRdmExtensionType())?req.getRdmExtensionType(): XdmEntityUtil.getObjectType();
        // 构建entity全包名
        String classPath = XdmEntityUtil.getEntityPackage(objectType);
        BasicObject entity = ReflectUtil.newInstance(classPath);
        BeanUtil.copyPropertiesIgnoreNull(req, entity);
        return entity;
    }

    /***
     * 拼接service实现bean名称
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 16:57
     * @return {@link String}
     */
    public static String getBeanServiceImpl(String objectType) {
        return CharSequenceUtil.lowerFirst(objectType) + "DomainServiceImpl";
    }

    /***
     * 获取请求对象
     *
     * @author 徐鹏军
     * @date 2022/3/31 13:21
     * @return {@link String}
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /***
     * 获取对象类型
     *
     * @author 徐鹏军
     * @date 2022/3/31 13:21
     * @return {@link String}
     */
    public static String getObjectType() {
        return StrUtil.subBetween(getRequest().getRequestURI(), StrUtil.SLASH);
    }
    /***
     * 获取对象类型
     *
     * @author 徐鹏军
     * @date 2022/3/31 13:21
     * @return {@link String}
     */
    public static <T extends BasicObject> String getObjectType(Class<T> clazz) {
        return StrUtil.removeSuffix(clazz.getSimpleName(),Constant.ENTITY);
    }
    /***
     * 获取对象类型
     *
     * @author 徐鹏军
     * @date 2022/3/31 13:21
     * @return {@link String}
     */
    public static <T extends BasicObject> String getObjectType(T t) {
        return getObjectType(t.getClass());
    }
    /***
     * 拼接零组件版本类型名称
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 16:57
     * @return {@link String}
     */
    public static String getRevision(String objectType) {
        return StrUtil.addSuffixIfNot(objectType, "Revision");
    }
    /***
     * 根据版本类型转成组件类型
     *
     * @param revisionType 版本类型
     * @author 徐鹏军
     * @date 2022/4/12 16:57
     * @return {@link String}
     */
    public static String getObjectTypeByRevisionType(String revisionType) {
        return StrUtil.removeSuffix(getRevision(revisionType), "Revision");
    }


}
