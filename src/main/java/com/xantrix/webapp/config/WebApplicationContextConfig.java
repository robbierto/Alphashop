package com.xantrix.webapp.config;

import java.util.ArrayList;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;
import org.springframework.web.util.UrlPathHelper;

import com.xantrix.webapp.domain.Articoli;
import com.xantrix.webapp.views.ArticoliCsvView;
import com.xantrix.webapp.views.ArticoliExcelView;
import com.xantrix.webapp.views.ArticoliPdfView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.xantrix.webapp")
public class WebApplicationContextConfig  implements WebMvcConfigurer
{
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
	{
		configurer.enable();
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry)
	{
		TilesViewResolver viewResolver = new TilesViewResolver();
		registry.viewResolver(viewResolver);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");

		registry.addInterceptor(localeChangeInterceptor);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/img/**").addResourceLocations("/static/images/");
	}
	
	@Override
	public Validator getValidator()
	{
		return validator();
	}
	
	@Bean
	public LocaleResolver localeResolver()
	{
		/*
		 * SessionLocaleResolver resolver = new SessionLocaleResolver();
		 * resolver.setDefaultLocale(new Locale("it")); return resolver;
		 */

		CookieLocaleResolver r = new CookieLocaleResolver();
		r.setCookieName("localeInfo");
		r.setCookieMaxAge(24 * 60 * 60);
		r.setDefaultLocale(new Locale("it"));

		return r;

	}
	
	@Bean(name = "validator")
	public LocalValidatorFactoryBean validator()
	{
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());

		return bean;
	}
	
	@Bean
	public MappingJackson2JsonView jsonView()
	{
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setPrettyPrint(true);

		return jsonView;
	}
	
	@Bean
	public MarshallingView xmlView()
	{
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Articoli.class);
		
		MarshallingView xmlView = new MarshallingView(marshaller);

		return xmlView;
	}
	
	@Bean
	public ArticoliPdfView articoliPdfView()
	{
		return new ArticoliPdfView("Articoli.pdf");
	}
	
	
	@Bean
	public ArticoliExcelView articoliExcelView()
	{
		return new ArticoliExcelView("Articoli.xlsx");
	}
	
	@Bean
	public ArticoliCsvView articoliCsvView()
	{
		return new ArticoliCsvView("Articoli.csv");
	}
	
	
	@Bean
	public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager)
	{
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setContentNegotiationManager(manager);
		
		ArrayList<View> views = new ArrayList<>();
		views.add(jsonView()); // Formato JSON
		views.add(xmlView()); // Formato XML
		views.add(articoliPdfView());
		views.add(articoliExcelView());
		views.add(articoliCsvView());
		 
		resolver.setDefaultViews(views);
		
		return resolver;
	}
	
	/* Alternetiva del MessageSource
	@Bean
    public MessageSource messageSource() 
	{
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    */
	
	
	@Bean
	public MessageSource messageSource()
	{
		ResourceBundleMessageSource resource = new ResourceBundleMessageSource();
		resource.setBasename("messages");
		
		return resource;
	}
	
	
	@Bean
	public InternalResourceViewResolver getInternalResourceViewResolver()
	{
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		
		resolver.setPrefix("/WEB-INF/view/");
		resolver.setSuffix(".jsp");

		return resolver;
	}
	
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer)
	{
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		urlPathHelper.setRemoveSemicolonContent(false);

		configurer.setUrlPathHelper(urlPathHelper);
	}
	
	
}
