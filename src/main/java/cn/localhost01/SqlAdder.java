package cn.localhost01;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 描述：Mybatis SQL条件添加器<br>
 * 一般适用于sql中只有一个where语句的SQL，如果SQL较复杂，如有多个where以及嵌套子查询等，请避免使用本工具类。另外会和pagehelper启用总数查询时冲突！
 *
 * @author 冉椿林
 * @date 2017年7月22日 下午3:37:55
 */
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {
        Connection.class })) public class SqlAdder implements Interceptor {

    private String sql_Intercept;
    private String sql_Not_Intercept;
    private boolean is_print_sql = false;

    private static final ThreadLocal<String> LOCAL_CONDITION = new ThreadLocal<String>();
    private static final Logger logger = LoggerFactory.getLogger(SqlAdder.class);

    /**
     * Description:开始进行SQL拦截 <BR>
     *
     * @param sqlCondition 要添加的条件语句
     *
     * @author ran.chunlin
     * @date 2017年7月24日 下午5:38:19
     * @version 1.0
     */
    public static void addSqlCond(String sqlCondition) {
        LOCAL_CONDITION.set(sqlCondition);
    }

    public Object intercept(Invocation inv) throws Throwable {
        String condition = LOCAL_CONDITION.get();

        LOCAL_CONDITION.remove();

        StatementHandler target = (StatementHandler) inv.getTarget();
        BoundSql boundSql = target.getBoundSql();
        String sql = boundSql.getSql().toLowerCase();

        if (condition == null)
            return inv.proceed();

        // 只有select语句才进行下一步
        if (Pattern.matches(sql_Intercept, sql) && ("".equals(sql_Not_Intercept) ?
                true :
                !Pattern.matches(sql_Not_Intercept, sql))) {
            String newSql = null;
            String lowerSql = sql.toLowerCase();

            if (lowerSql.matches("[\\s\\S]+\\s+where\\s+[\\s\\S]+$"))
                newSql = lowerSql.replaceAll("\\swhere\\s", " " + condition + " and ");
            else if (lowerSql.matches("[\\s\\S]+\\s+gourp\\s+by\\s+[\\s\\S]+$"))
                newSql = lowerSql.replaceAll("\\sgourp\\s+by\\s", " " + condition + " gourp by ");
            else if (lowerSql.matches("[\\s\\S]+\\s+order\\s+by\\s+[\\s\\S]+$"))
                newSql = lowerSql.replaceAll("\\sorder\\s+by\\s", " " + condition + " order by ");
            else if (lowerSql.matches("[\\s\\S]+\\s+limit\\s+[\\s\\S]+$"))
                newSql = lowerSql.replaceAll("\\slimit\\s", " " + condition + " limit ");
            else
                newSql = lowerSql + " " + condition;

            if (newSql != null) {
                ReflectUtil.setFieldValue(boundSql, "sql", newSql);

                if (is_print_sql)
                    logger.info("......\n====================================print sql====================================\nraw sql:" + boundSql.getSql() + "\nparams:"
                            + boundSql.getParameterObject() + "\nnew sql:" + newSql
                            + "\n====================================print sql====================================");
            }
        }
        return inv.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties arg0) {
    }

    public void setSql_Intercept(String sql_Intercept) {
        this.sql_Intercept = sql_Intercept;
    }

    public void setSql_Not_Intercept(String sql_Not_Intercept) {
        this.sql_Not_Intercept = sql_Not_Intercept;
    }

    public void setIs_print_sql(boolean is_print_sql) {
        this.is_print_sql = is_print_sql;
    }
}
