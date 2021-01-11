package com.shine.app002;

import com.shine.entity.Column;
import com.shine.entity.UserEntity;

import java.lang.reflect.Field;
import java.sql.ResultSet;

/**
 * 用户实体助手类
 */
public class UserEntity_Helper {
    /**
     * 将数据集装换为实体对象
     *
     * @param rs 数据集
     * @return
     * @throws Exception
     */
    public UserEntity create(ResultSet rs) throws Exception {
        if (null == rs) {
            return null;
        }

        // 创建新的实体对象
        UserEntity ue = new UserEntity();

        Field[] fields = ue.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Column annotation = field.getAnnotation(Column.class);
            if(null==annotation){
                continue;
            }
            String name = annotation.name();
            if(null==name||"".equals(name)){
                continue;
            }
            Object object = rs.getObject(name);
            if(null==object){
                continue;
            }

            field.set(ue, object);
        }
        
        //
        // 都是硬编码会不会很累?
        // 而且要是 UserEntity 和 t_user 加字段,
        // 还得改代码...
        // 为何不尝试一下反射?
        // 跳到 step020 看看吧!
        //
        return ue;
    }
}
