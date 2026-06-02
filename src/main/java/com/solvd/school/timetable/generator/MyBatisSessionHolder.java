package com.solvd.school.timetable.generator;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public final class MyBatisSessionHolder {

    private static final Logger LOGGER =
            LogManager.getLogger(MyBatisSessionHolder.class);

    private static final String CONFIG_FILE_NAME =
            "mybatis-config.xml";

    private static final SqlSessionFactory SQL_SESSION_FACTORY;

    static {
        SQL_SESSION_FACTORY = buildSessionFactory();
    }

    private MyBatisSessionHolder() {
    }

    private static SqlSessionFactory buildSessionFactory() {

        InputStream inputStream;

        try {
            inputStream = Resources.getResourceAsStream(CONFIG_FILE_NAME);

            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

            SqlSessionFactory factory = builder.build(inputStream);

            LOGGER.info("MyBatis SqlSessionFactory initialised");

            return factory;

        } catch (IOException e) {

            throw new RuntimeException("Unable to prepare mybatis config xml.", e);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return SQL_SESSION_FACTORY;
    }

    public static SqlSession openSession() {
        return SQL_SESSION_FACTORY.openSession(true);
    }

    public static SqlSession openManagedSession() {
        return SQL_SESSION_FACTORY.openSession(false);
    }

}