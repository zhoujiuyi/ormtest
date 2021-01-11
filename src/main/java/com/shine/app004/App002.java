package com.shine.app004;


import com.shine.entity.UserEntity;

import java.sql.*;

/**
 * @program: ormtest
 * @description: 应用002
 * @author: yczjy
 * @create: 2021-01-08 09:11
 **/
public class App002 {

    /**
     * 应用程序主函数
     *
     * @param argvArray 参数数组
     * @throws Exception
     */
    static public void main(String[] argvArray) throws Exception {
        (new App002()).start();
    }

    /**
     * 测试开始
     */
    private void start() throws Exception {
        // 加载 Mysql 驱动
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        // 数据库连接地址
        String dbConnStr = "jdbc:mysql://192.168.81.129:3306/temp20210108?user=root&password=shineCoding&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
        // 创建数据库连接
        Connection conn = DriverManager.getConnection(dbConnStr);
        // 简历陈述对象
        final Statement stmt = conn.createStatement();
        // 创建 SQL 查询
        // ormtest 数据库中有个 t_user 数据表,
        // t_user 数据表包括三个字段: user_id、user_name、password,
        // t_user 数据表有 20 万条数据
        String sql = "select * from t_user limit 200000";

        // 执行查询
        ResultSet rs = null;
        AbstractEntityHelper abstractEntityHelper = EntityHelperFactory.getEntityHelper(UserEntity.class);
        // 获取开始时间
        long t0 = System.currentTimeMillis();
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                abstractEntityHelper.create(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取结束时间
        long t1 = System.currentTimeMillis();

        // 关闭数据库连接
        stmt.close();
        conn.close();

        // 打印实例化花费时间
        System.out.println("实例化花费时间 = " + (t1 - t0) + "ms");
    }
}
