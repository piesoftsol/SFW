package com.piesoftsol.oneservice.common.integration.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.piesoftsol.oneservice.common.integration.annotations.SetJsonMapping;

public class ScanClassForAnnotation {

	private static final ClassLoader classLoader = ScanClassForAnnotation.class.getClassLoader();
	
	public static String getMainClass() {
		ClassPathScanningCandidateComponentProvider scanner =
				new ClassPathScanningCandidateComponentProvider(true);

				scanner.addIncludeFilter(new AnnotationTypeFilter(SetJsonMapping.class));
				scanner.setResourceLoader(new PathMatchingResourcePatternResolver(classLoader));
				for (BeanDefinition bd : scanner.findCandidateComponents("com.piesoftsol.oneservice"))
				    System.out.println("KKKKKKKKKKKKK" + bd.getBeanClassName());
				
				System.out.println("OOOOOOOOOOOO");
				return null;
	}
}
