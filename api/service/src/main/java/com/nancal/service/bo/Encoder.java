package com.nancal.service.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.EncoderReq;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.constants.Constant;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.EncoderEntity;
import com.nancal.model.entity.QEncoderEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "DatasetFileRL 数据集文件关系 的BO")
public class Encoder extends WorkspaceObject {

    @ApiModelProperty("前缀，为空时为不用前缀，只有流水码,例如：{{yyyyMMHH}}")
    private String prefix;

    @ApiModelProperty("流水码长度，为空时不在前补位0")
    private Integer length;

    @ApiModelProperty("编码")
    private Long maxSerialNumber;

    @ApiModelProperty("流水开始码,通常首次申请该类型该前缀时有效，之后当该值大于最大编码时有效")
    private Long startSerial;

    @ApiModelProperty("流水结束码, 仅在首次申请该类型该前缀时有效,默认为所传长度个9组成，如所传长度为2时，该值默认为99")
    private Long endSerial;

    /***
     * 申请编码
     *
     * @param req 请求参数
     * @author 徐鹏军
     * @date 2022/4/25 17:18
     * @return {@link List< String>}
     */
    public String createOneCode(EncoderReq req) {
        List<String> codes = this.createCode(req);
        return CollUtil.getFirst(codes);
    }

    /***
     * 申请编码
     *
     * @param req 请求参数
     * @author 徐鹏军
     * @date 2022/4/25 17:18
     * @return {@link List< String>}
     */
    public List<String> createCode(EncoderReq req) {
        // 解析前缀模板
        String prefix = StrUtil.blankToDefault(req.getPrefix(), StrUtil.EMPTY);
        Matcher matcher = Constant.PREFIX_PATTERN.matcher(prefix);
        if (matcher.find()) {
            String formatString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(StringUtils.substringBetween(matcher.group(), "{{", "}}")));
            prefix = matcher.replaceFirst(formatString);
        }
        // 获取最大编码
        Long endSerial = ObjectUtil.defaultIfNull(req.getEndSerial(), Long.valueOf(String.join(StrUtil.EMPTY, Collections.nCopies(req.getLength(), "9"))));
        // 构建编码对象
        EncoderEntity encoder = new EncoderEntity();
        encoder.setPrefix(prefix);
        encoder.setLength(req.getLength());
        encoder.setMaxSerialNumber(req.getStartSerial());
        encoder.setStartSerial(req.getStartSerial());
        encoder.setEndSerial(endSerial);
        encoder.setObjectType(CharSequenceUtil.blankToDefault(req.getObjectType(), EntityUtil.getObjectType()));
        encoder.setUid(encoder.getObjectType() + encoder.getPrefix());
        EncoderEntity encoderEntity = SpringUtil.getBean(JPAQueryFactory.class)
                .selectFrom(QEncoderEntity.encoderEntity)
                .where(QEncoderEntity.encoderEntity.uid.eq(encoder.getUid()))
                .fetchFirst();
        if (Objects.nonNull(encoderEntity)) {
            encoder = encoderEntity;
        }
        long maxSerialNumber = encoder.getMaxSerialNumber() > req.getStartSerial() ? encoder.getMaxSerialNumber() : req.getStartSerial();
        if (maxSerialNumber + req.getSize() > encoder.getEndSerial()) {
            throw new ServiceException(ErrorCode.ERROR, "类型" + encoder.getObjectType() + " 前缀" + prefix + "的码已用完");
        }
        List<String> codes = new ArrayList<>();
        for (int i = 1; i < req.getSize() + 1; i++) {
            String formatCode = String.format("%0" + req.getLength() + "d", maxSerialNumber + ((long) i * req.getStep()));
            codes.add(prefix + formatCode);
        }
        encoder.setMaxSerialNumber(maxSerialNumber + ((long) req.getSize() * req.getStep()));
        SpringUtil.getBean(EntityManager.class).persist(encoder);
        return codes;
    }

}
