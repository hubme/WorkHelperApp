package com.king.app.processor.processor;

import com.google.auto.service.AutoService;
import com.king.app.processor.annotation.BindView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

@AutoService(MyBindViewProcessor.class)
public class MyBindViewProcessor extends AbstractProcessor {
    @Override public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        final HashSet<String> supportedTypes = new HashSet<>(1);
        supportedTypes.add(BindView.class.getCanonicalName());
        return supportedTypes;
    }
    
    //Element 用于代表程序的一个元素，这个元素可以是包、类、接口、变量、方法等。
    @Override public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //获取所有包含 BindView 注解的元素
        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        //因为 BindView 的作用对象是 FIELD，因此 element 可以直接转化为 VariableElement
        final Map<TypeElement, Map<Integer, VariableElement>> typeElementMapHashMap = new HashMap<>();
        for (Element element : elementSet) {
            VariableElement variableElement = (VariableElement) element;
            //getEnclosingElement 方法返回封装此 Element 的最里层元素
            //此处表示的即 Activity 类对象
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            Map<Integer, VariableElement> variableElementMap = typeElementMapHashMap.get(typeElement);
            variableElementMap = new HashMap<>();
            typeElementMapHashMap.put(typeElement, variableElementMap);
        }


        return false;
    }
}
