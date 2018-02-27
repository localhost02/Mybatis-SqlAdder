# Mybatis-SqlAdder
Mybatis条件添加插件

# 使用说明
1.在mybatis配置文件中，添加类似如下：
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


2.代码使用示例：
    SqlAdder.add("where car_enable=1");
    carMapper.selectAllCarList();

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
