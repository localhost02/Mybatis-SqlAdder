# 插件介绍
Mybatis条件动态添加插件，用于书写少量代码（一句话代码，类似pagehelper的PageHelper.startPage(currentPage, branchSize)），以完成一些查询条件的添加。

# 使用场景
你有一个查询所有学生的接口。这时有个需求是查询“有效”的学生，或是查询年龄为18岁以上的学生，又或是查询所有男生等等，这时，你要么将接口重构或重载，动态传入sql，或者分别为不同需求，在dao中书写不同接口。前者每次需要写不同sql，后者就不知道要写多少个接口。因此你可以使用Mybatis-SqlAdder，为查询语句动态添加条件，如下：

SqlAdder.addSqlCond("s_enable=0");   studentMapper.selectAllList();

SqlAdder.addSqlCond("s_age>18");   studentMapper.selectAllList();

SqlAdder.addSqlCond("s_gender=1");   studentMapper.selectAllList();

# 使用说明
1. 在mybatis配置文件中，添加类似如下：
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="mapperLocations" value="classpath*:com/**/mapper/**/*.xml" />
		<property name="plugins">
			<array>
				<bean id="sqlAdder" class="cn.localhost01.SqlAdder">
					<!-- [正则]拦截的sql语句，下列是拦截“select……” -->
					<property name="sql_Intercept" value="^\s*select[\s\S]*$" />
					<!-- [正则]不拦截的sql语句，下列是不拦截“select count……” -->
					<property name="sql_Not_Intercept" value="^\s*select\s+count\s*\(\s*(?:\*|\w+)\s*\)\s+[\s\S]+$" />
				    <!-- 是否打印sql -->
				    <property name="is_print_sql" value="false" />
				</bean>
			</array>
		</property>
	</bean>


2. 代码使用示例：
    SqlAdder.add("where car_enable=1");
    studentMapper.selectAllList();

# 依赖
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.2.6</version>
            <scope>compile</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
