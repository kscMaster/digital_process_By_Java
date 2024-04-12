package com.nancal.esop.entity;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.esop.consts.EsopConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "SOPB_LINE")
public class LineEntity {

    @EmbeddedId
    private LineEntity.LinePk id;

    @Column(name = "LINEID")
    private String lineId;

    @Column(name = "LINENAME")
    private String lineName;

    @Column(name = "LINEUID")
    private String lineUid;

    @Column(name = "STATIONNUM")
    private Integer stationNum;

    @Column(name = "WORKING")
    private Integer working;

    @Column(name = "REMOVED")
    private Integer removed;

    @Column(name = "PARENT_LINECODE")
    private String parentLinecode;

    @Column(name = "THEMEID")
    private Long themeId;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class LinePk implements Serializable {

        @Column(name = "SITENAME")
        private String siteName;

        @Column(name = "LINECODE")
        private String lineCode;
    }

    /***
     * 构建SOPB_LINE对象
     *
     * @param lineCode 线code
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link LineEntity}
     */
    public static LineEntity create(String lineCode) {
        LineEntity lineEntity = new LineEntity();
        LineEntity.LinePk linePk = new LineEntity.LinePk();
        linePk.setSiteName(EsopConst.sopb_bop_mgmt$sit_name);
        linePk.setLineCode(lineCode);
        lineEntity.setId(linePk);
        lineEntity.setLineId("00" + RandomUtil.randomInt(1000,9999));
        lineEntity.setLineName(StrUtil.addSuffixIfNot(lineCode,"-Line"));
        lineEntity.setLineUid(IdGeneratorUtil.generate());
        lineEntity.setStationNum(RandomUtil.randomInt(0,99));
        lineEntity.setWorking(0);
        lineEntity.setRemoved(0);
        lineEntity.setParentLinecode(null);
        lineEntity.setThemeId(0L);
        return lineEntity;
    }
    /***
     * 构建SOPB_LINE对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <LineEntity>}
     */
    public static List<LineEntity> create(List<String> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(LineEntity::create).collect(Collectors.toList());
    }
}
