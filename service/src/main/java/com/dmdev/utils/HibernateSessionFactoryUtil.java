package com.dmdev.utils;

import com.dmdev.entity.Car;
import com.dmdev.entity.CarRate;
import com.dmdev.entity.Model;
import com.dmdev.entity.OrderDetails;
import com.dmdev.entity.DriverLicense;
import com.dmdev.entity.Order;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateSessionFactoryUtil {

    @SneakyThrows
    public static SessionFactory buildSessionAnnotationFactory() {
        SessionFactory sessionFactory;
        Configuration configuration = new Configuration();

        configuration
                .addProperties(PropertiesUtil.getProperties());

        configuration.addAnnotatedClass(Model.class);
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(OrderDetails.class);
        configuration.addAnnotatedClass(DriverLicense.class);
        configuration.addAnnotatedClass(CarRate.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(UserDetails.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }
}