package com.nancal.msgbom;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class MainTest {


    @Test
    public void baseTest(){

        Set<String> sequenceSet = new HashSet<>();
        sequenceSet.add("sdf");
        sequenceSet.add("wer");
        sequenceSet.add("fgh");

        Set<String> cloneSet = ObjectUtil.cloneByStream(sequenceSet);
        cloneSet.remove("fgh");

        System.out.println(JSONUtil.toJsonStr(cloneSet));
        System.out.println(JSONUtil.toJsonStr(sequenceSet));

    }

}
