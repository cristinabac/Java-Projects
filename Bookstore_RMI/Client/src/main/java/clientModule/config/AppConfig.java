package clientModule.config;

import commonModule.service.BookServiceInterface;
import commonModule.service.ClientServiceInterface;
import commonModule.service.SaleServiceInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class AppConfig {
    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanClient() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(ClientServiceInterface.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/ClientService");
        return rmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanBook() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(BookServiceInterface.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/BookService");
        return rmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanSale() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(SaleServiceInterface.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/SaleService");
        return rmiProxyFactoryBean;
    }

}
