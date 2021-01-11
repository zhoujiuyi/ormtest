package com.shine.app004;

import java.sql.ResultSet;

/**
 * @program: ormtest
 * @description: 抽象助手类
 * @author: yczjy
 * @create: 2021-01-08 10:13
 **/
public abstract class AbstractEntityHelper {

    /**
     * 将数据集转换为实体对象
     *
     * @param rs 数据集
     * @return
     *
     */
    public abstract Object create(ResultSet rs) throws Exception;
}
