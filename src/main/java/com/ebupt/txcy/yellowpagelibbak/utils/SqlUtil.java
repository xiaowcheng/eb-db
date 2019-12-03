package com.ebupt.txcy.yellowpagelibbak.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    /**
     * 根据实体生成更新的sql 语句以及对应的参数
     * @param o
     * @return
     */
    public static SqlAndParam getUpdateSqlString(Object o) {
        List<String> params = new ArrayList<>();
        List<String> selectParam = new ArrayList<>();
        List<String> conditionParam = new ArrayList<>();
        Class<?> aClass = o.getClass();
        Table table = aClass.getAnnotation(Table.class);
        String tableNmae = "";
        StringBuilder sqlset = new StringBuilder(" update ");
        StringBuilder sqlSelect = new StringBuilder(" select ");
        StringBuilder sqlcondition = new StringBuilder(" where 1=1 ");

        if(table!=null){
            tableNmae = table.name();
            sqlset.append(" "+tableNmae+" ");
            sqlset.append(" set (");
        }
        Field[] fields = aClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            //此处要注意实体的注解写法，对于复合主键
            Id id = field.getAnnotation(Id.class);
            try {

                if(column !=null&&id==null){
                    String columnName = column.name();
                    sqlset.append(" "+columnName+",");

                    sqlSelect.append(" COALESCE(?,"+columnName+") as "+columnName+",");
                    selectParam.add(field.getName());
                }

                if(id !=null && field.get(o)!=null){
                    String columnName = column.name();
                    sqlcondition.append(" and ");
                    sqlcondition.append(" "+columnName+"= ?");
                    conditionParam.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String sql1 = sqlset.substring(0,sqlset.lastIndexOf(","))+")";
        String sql2 =sqlSelect.substring(0,sqlSelect.lastIndexOf(","))+" from "+tableNmae +sqlcondition;
        String finalSql = sql1+"=("+sql2+")"+ sqlcondition;
        params.addAll(selectParam);
        params.addAll(conditionParam);
        params.addAll(conditionParam);
       return new SqlAndParam(finalSql,params);

    }
    public static String getInsertSql(Object obj){
        return  getInsertSqlString(obj).getSql();
    }
    public static String getUpdateSql(Object obj){
        return  getUpdateSqlString(obj).getSql();
    }
    public static SqlAndParam getInsertSqlString(Object obj){
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
                e.printStackTrace();
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


    /**
     *
     * @param jdbcTemplate
     * @param list
     * @param sqlAndParam
     * @param <T>
     */
    private static<T> void executeBatch(JdbcTemplate jdbcTemplate,List<T> list,SqlAndParam sqlAndParam){
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
                        //当为null时，时间不能直接setObject
                        if(o==null){
                            Class<?> type = declaredField.getType();
                            if(type == Date.class||type==java.sql.Date.class){
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
        log.info("{}条，batchUpdate耗时："+(System.currentTimeMillis()-start),list.size());
    }
    public static <T> void  executeInsertBatch(JdbcTemplate jdbcTemplate,List<T> list){
        if(list.isEmpty()){
            return;
        }
        SqlAndParam sqlAndParam = null;
        for (T t : list) {
            sqlAndParam = getInsertSqlString(t);
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
            sqlAndParam = getUpdateSqlString(t);
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

}
