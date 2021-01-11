package com.shine.app003;


import com.shine.entity.UserEntity;

import java.sql.*;

/**
 * @program: ormtest
 * @description: 应用002
 * @author: yczjy
 * @create: 2021-01-08 09:11
 **/
public class App001 {

    /**
     * 应用程序主函数
     *
     * @param argvArray 参数数组
     * @throws Exception
     */
    static public void main(String[] argvArray) throws Exception {
        (new App001()).start();
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


        // 获取开始时间
        final UserEntity_Helper userEntity_helper = new UserEntity_Helper();
        // 创建 SQL 查询
        // ormtest 数据库中有个 t_user 数据表,
        // t_user 数据表包括三个字段: user_id、user_name、password,
        // t_user 数据表有 20 万条数据
        String sql = "select * from t_user limit 200000";
        long t0 = System.currentTimeMillis();
        // 执行查询
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                userEntity_helper.create(UserEntity.class, rs);
                //
                // 关于上面这段代码,
                // 我们是否可以将其封装到一个助手类里??
                // 这样做的好处是:
                // 当实体类发生修改时, 只需要改助手类就可以了...
                //
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
