package com.ebupt.txcy.yellowpagelibbak.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * 该工具类根实体类上注解相结合使用，使用前确保相关参数不为空（如更新时：主键，插入时：非空字段）
 */
@Slf4j
public class SqlUtil {
    


    public static SqlAndParam getOracleInsertSqlString(Object obj){
        List<String> param = new ArrayList<>();
        List<String> masterKeyParam = new ArrayList<>();
        List<String> updateParam = new ArrayList<>();
        List<String> insertPram = new ArrayList<>();
        Class<?> aClass = obj.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        Table table = aClass.getAnnotation(Table.class);
        String tableNmae = "";
        StringBuilder mergeSql = new StringBuilder("merge into ");
        StringBuilder selectSql = new StringBuilder(" select ");
        StringBuilder sqlOn = new StringBuilder(" 1=1 and ");
        StringBuilder updateSql = new StringBuilder(" when matched then \n update set ");
        StringBuilder insertSql = new StringBuilder(" when not matched then \n insert ( ");
        StringBuilder valueSql = new StringBuilder(" values( ");
        if(table !=null){
            tableNmae = table.name();
            mergeSql.append(tableNmae+" t1 using");
        }
        Field[] fields = aClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            //此处要注意实体的注解写法，对于复合主键
            Id id = field.getAnnotation(Id.class);
            try {
                if (column != null && id == null) {
                    String columnName = column.name();
                    updateSql.append("t1."+columnName+"=?,");
                    insertSql.append(columnName+",");
                    valueSql.append("?,");
                    updateParam.add(field.getName());
                    insertPram.add(field.getName());
                }
                if (id != null && field.get(obj) != null&&column != null) {
                    selectSql.append(" ? as " +column.name()+", " );
                    sqlOn.append(" t1."+column.name()+"=tmp."+column.name()+" and ");
                    insertSql.append(column.name()+",");
                    valueSql.append("?,");
                    masterKeyParam.add(field.getName());
                    insertPram.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                log.error("反射操作权限异常",e);
            }
        }
        String sql2 = "("+selectSql.substring(0,selectSql.lastIndexOf(","))+" from dual)tmp";
        String sql3 =" on ("+sqlOn.substring(0,sqlOn.lastIndexOf("and"))+")";
        String sql4 = updateSql.substring(0,updateSql.lastIndexOf(","));
        String sql5=insertSql.substring(0,insertSql.lastIndexOf(","))+")";
        String sql6=valueSql.substring(0,valueSql.lastIndexOf(","))+")";
        mergeSql.append(sql2).append(sql3).append(sql4).append(sql5).append(sql6);
        String sql = mergeSql.toString();
        param.addAll(masterKeyParam);
        param.addAll(updateParam);
        param.addAll(insertPram);
        return new SqlAndParam(sql,param);
    }

    public static SqlAndParam getMysqlInsertSql(Object obj){
        Class<?> aClass = obj.getClass();
        Table table = aClass.getAnnotation(Table.class);
        if(table==null){
            return null;
        }
        String tableName = table.name();
        Field[] fields = aClass.getDeclaredFields();
        List<String> paramNames = new ArrayList<>();
        StringBuilder insert = new StringBuilder("insert into ");
        StringBuilder insert2 = new StringBuilder("(");
        StringBuilder insert3 = new StringBuilder(" values(");
        StringBuilder insert4 = new StringBuilder(" on duplicate key update ");
        insert.append(tableName+" ");
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                //此处要注意实体的注解写法，对于复合主键
                Id id = field.getAnnotation(Id.class);
                if (column != null) {
                    String name = column.name();
                    insert2.append(name);
                    insert2.append(",");
                    insert3.append("?");
                    insert3.append(",");
                    paramNames.add(field.getName());
                    insert4.append(" "+name+"=?");
                    insert4.append(",");
                }

            }
        }catch (Exception e){
                log.error("[svc]sql拼装异常",e);
        }
        String substring = insert2.substring(0, insert2.lastIndexOf(","));
        String substring2 = insert3.substring(0, insert3.lastIndexOf(","));
        String substring1 = insert4.substring(0, insert4.lastIndexOf(","));
        substring = substring+")";
        insert.append(substring);
        insert.append(" ");
        insert.append(substring2);
        insert.append(") ");
        insert.append(substring1);
        //insert into test (id,name,age,grade)  vaules(?,?,?,?)  on duplicate key update  id=?, name=?, age=?, grade=?
        paramNames.addAll(paramNames);
        SqlAndParam sqlAndParam = new SqlAndParam(insert.toString(),paramNames);
        return  sqlAndParam;
    }

    public static SqlAndParam getUpdateSql(Object obj){
        Class<?> aClass = obj.getClass();
        Table table = aClass.getAnnotation(Table.class);
        if(table==null){
            return null;
        }
        String tableName = table.name();
        Field[] fields = aClass.getDeclaredFields();
        List<String> paramNames = new ArrayList<>();
        List<String> conditionNames = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder("update ");
        StringBuilder conditionBuilder = new StringBuilder(" where 1=1 ");
        stringBuilder.append(tableName);
        stringBuilder.append(" set ");
        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            //此处要注意实体的注解写法，对于复合主键
            Id id = field.getAnnotation(Id.class);
            try {

                if(column !=null&&id==null&&field.get(obj)!=null){
                    String name = column.name();
                    stringBuilder.append(name+"=?,");
                    paramNames.add(field.getName());
                }

                if(id !=null && field.get(obj)!=null){
                    String name = column.name();
                    conditionBuilder.append(" and ");
                    conditionBuilder.append( name+"=?");
                    conditionNames.add(field.getName());

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        paramNames.addAll(conditionNames);
        String substring = stringBuilder.substring(0, stringBuilder.lastIndexOf(","));
        substring = substring+conditionBuilder.toString();
        return new SqlAndParam(substring,paramNames);
    }
    /**
     *
     * @param jdbcTemplate
     * @param list
     * @param sqlAndParam
     * @param <T>
     */
    private static<T> void executeBatch(JdbcTemplate jdbcTemplate,List<T> list,SqlAndParam sqlAndParam){
        log.debug("执行sql",sqlAndParam.getSql());
        List<String> paramNames = sqlAndParam.getParamNames();
        long start = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(sqlAndParam.getSql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int j) throws SQLException {
                T t = list.get(j);
                Class aClass = t.getClass();
                for (  int i = 0;i<paramNames.size();i++){
                    try {
                        Field declaredField = aClass.getDeclaredField(paramNames.get(i));
                        declaredField.setAccessible(true);
                        Object o = declaredField.get(t);
                        //对于类型，最好要明确，不然会出现类型异常，特别是不常用的类型。
                        if(o instanceof  Date){
                            ps.setTimestamp(i+1,new Timestamp((((Date) o).getTime())));
                            continue;
                        }
                        //当为null时，最好不要通过Object来处理，类型会出异常
                        if(o==null){
                            Class<?> type = declaredField.getType();
                            if(type == Date.class||type==java.sql.Date.class){
                                ps.setTimestamp(i+1,null);
                                continue;
                            }
                            if(type==Integer.class){
                                ps.setNull(i+1, Types.INTEGER);
                                continue;
                            }
                        }
                        ps.setObject(i+1,o);

                    } catch (NoSuchFieldException e) {
                       ;
                        log.error("[svc]反射获取失败",e);

                    } catch (IllegalAccessException e) {

                       log.error("[svc]反射权限异常",e);
                    }
                }

            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
       /* int ints[][] = jdbcTemplate.batchUpdate(sqlAndParam.getSql(), list, list.size(), (ps, param) -> {
            Class<?> aClass = param.getClass();
            for (  int i = 0;i<paramNames.size();i++){
                try {
                    Field declaredField = aClass.getDeclaredField(paramNames.get(i));
                    declaredField.setAccessible(true);
                    Object o = declaredField.get(param);
                    //对于类型，最好要明确，不然会出现类型异常，特别是不常用的类型。
                    if(o instanceof  java.util.Date){
                        ps.setTimestamp(i+1,new Timestamp((((Date) o).getTime())));
                        continue;

                    }
                    //当为null时，时间不能直接setObject
                    if(o==null){
                        Class<?> type = declaredField.getType();
                        if(type == java.util.Date.class||type==java.sql.Date.class){
                            ps.setTimestamp(i+1,null);
                            continue;
                        }
                    }
                    ps.setObject(i+1,o);

                } catch (NoSuchFieldException e) {
                    throw new RuntimeException("服务器内部错误");

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("服务器内部错误");
                }
            }
        });*/
        log.debug("[svc]操作{}条数据，batchUpdate耗时："+(System.currentTimeMillis()-start),list.size());
    }
    public static <T> void  executeInsertBatch(JdbcTemplate jdbcTemplate,List<T> list){
        String databaseProductName = null;
        Connection connection = null;
        try {
             connection = jdbcTemplate.getDataSource().getConnection();
            databaseProductName = connection.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            log.error("[svc]获取连接异常",e);
        }finally {
            try {
                if(connection!=null&&!connection.isClosed()){
                      connection.close();
                }
            } catch (SQLException e) {
                log.error("[svc]释放连接异常",e);
            }
        }

        if(list.isEmpty()){
            return;
        }
        SqlAndParam sqlAndParam = null;
        log.debug("使用的数据库是：{}",databaseProductName);
        for (T t : list) {
            if("Oracle".equals(databaseProductName)){
                log.debug("拼装oracleSQL");
                sqlAndParam = getOracleInsertSqlString(t);
            }
            else{
                log.debug("拼装MysqlSQL");
                sqlAndParam = getMysqlInsertSql(t);
            }

            break;
        }
        executeBatch(jdbcTemplate,list,sqlAndParam);
    }
    public static <T> void executeUpdateBatch(JdbcTemplate jdbcTemplate,List<T> list){

        if(list.isEmpty()){
            return;
        }
        SqlAndParam sqlAndParam = null;
        for (T t : list) {
            sqlAndParam = getUpdateSql(t);
            break;
        }
        executeBatch(jdbcTemplate,list,sqlAndParam);

    }
    public static String formatDate(Date date) {
        String format = "yyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    static class  SqlAndParam{
        private String sql;
        private List <String> paramNames;

        public SqlAndParam() {
        }

        public SqlAndParam(String sql, List params) {
            this.sql = sql;
            this.paramNames = params;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public List<String> getParamNames() {
            return paramNames;
        }

        public void setParamNames(List<String> paramNames) {
            this.paramNames = paramNames;
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.setId(1);
        test.setName("wftest");
        test.setAge(20);
        test.setGrade(1);
        SqlAndParam mysqlInsertSql = getMysqlInsertSql(test);
        System.out.println(mysqlInsertSql.getSql());
    }
    @Table(name = "test")
    @Entity
    @Data
    static  class Test{
        @Id
        @Column(name = "id")
        private Integer id;
        @Column(name = "name")
        private String name;
        @Column(name = "age")
        private Integer age;
        @Column(name = "grade")
        private Integer grade;
    }
}
