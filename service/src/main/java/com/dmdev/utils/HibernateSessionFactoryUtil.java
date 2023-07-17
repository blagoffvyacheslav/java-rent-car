package com.dmdev.utils;

import com.dmdev.entity.*;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateSessionFactoryUtil {

    @SneakyThrows
    public static Configuration buildConfiguration() {
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
        configuration.addAnnotatedClass(Damage.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        return configuration;
    }
}