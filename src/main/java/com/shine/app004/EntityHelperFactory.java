package com.shine.app004;

import com.shine.entity.Column;
import javassist.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: ormtest
 * @description: 实体工厂类
 * @author: yczjy
 * @create: 2021-01-08 10:14
 **/
public class EntityHelperFactory {

    private static final Map<Class<?>,AbstractEntityHelper> HELPER_MAP=new HashMap<Class<?>, AbstractEntityHelper>();
    /**
     * 私有化类默认构造器
     */
    private EntityHelperFactory() {
    }

    /**
     * 获取帮助
     *
     * @param entityClazz 实体类
     * @return
     */
    public static AbstractEntityHelper getEntityHelper(Class<?> entityClazz) {
        // 这里需要全新设计,

        if(null==entityClazz){
            return null;
        }

        if(null!=HELPER_MAP.get(entityClazz)){
            return HELPER_MAP.get(entityClazz);
        }

        // 接下来就该请出 javassist 了!

        ClassPool pool = ClassPool.getDefault();//获取类池 用于创建CtClass
        pool.appendSystemPath();
        //导入包
        //        import com.shine.entity.Column;
        //        import java.lang.reflect.Field;
        //        import java.sql.ResultSet;
        pool.importPackage("com.shine.entity.Column");
        pool.importPackage("java.lang.reflect.Field");
        pool.importPackage("java.sql.ResultSet");
        //获取助手抽象类
        try {
            CtClass abstractEntityHelper = pool.getCtClass(AbstractEntityHelper.class.getName());
            //助手实现类名称
            final String extClassName=entityClazz.getName()+"_Helper";
            //创建实现类
            CtClass ctClass = pool.makeClass(extClassName, abstractEntityHelper);

            //创建无参构造器
            CtConstructor ctConstructor = new CtConstructor(new CtClass[0], ctClass);
            ctConstructor.setBody("{}");
            //加入构造器
            ctClass.addConstructor(ctConstructor);

            //创建函数代码
            final StringBuffer sb=new StringBuffer();
            sb.append("public Object  create(java.sql.ResultSet rs) throws Exception {\n");
            sb.append("if (null == rs) {\n" +
                    "            return null;\n" +
                    "        }\n");

            sb.append(entityClazz.getName()+"  ue = new "+entityClazz.getName()+"();\n");
            Field[] fields = entityClazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if(null==column){
                    continue;
                }
                String columnName = column.name();
                if(null==columnName||"".equals(columnName)){
                    continue;
                }
                String fileName=field.getName();
                sb.append("ue.set"+fileName.substring(0, 1).toUpperCase()+fileName.substring(1)+"(");
                if(field.getType().equals(Integer.class)){
                    sb.append("Integer.valueOf(rs.getInt(\""+columnName+"\"))");

                }else if(field.getType().equals(String.class)){
                    sb.append("rs.getString(\""+columnName+"\")");
                }
                sb.append( ");\n");
            }

            sb.append("return ue;\n");
            sb.append("}");
            CtMethod newMethod = CtNewMethod.make(sb.toString(), ctClass);
            ctClass.addMethod(newMethod);
            ctClass.writeFile("G:\\MySource");
            Class aClass = ctClass.toClass();
            AbstractEntityHelper destObj = (AbstractEntityHelper)aClass.newInstance();
            HELPER_MAP.put(entityClazz, destObj);
            return destObj;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (CannotCompileException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
