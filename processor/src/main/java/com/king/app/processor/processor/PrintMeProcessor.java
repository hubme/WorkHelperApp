package com.king.app.processor.processor;

import com.google.auto.service.AutoService;
import com.king.app.processor.annotation.PrintMe;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author VanceKing
 * @since 2019/1/14.
 */
@AutoService(PrintMeProcessor.class)
public class PrintMeProcessor extends AbstractProcessor {
    private final List<String> elementsValue = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        Elements elementUtils = env.getElementUtils();
        Types typeUtils = env.getTypeUtils();
        Filer filer = env.getFiler();
        Messager messager = env.getMessager();

    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedTypes = new HashSet<>(1);
        supportedTypes.add(PrintMe.class.getCanonicalName());
        return supportedTypes;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(PrintMe.class);
        System.out.println("elements size: " + elements.size());
        //由于编译器的输出无法打印到控制台，因此这里借助 javapoet 库把需要输出的信息写入到一个新的类中。
        for (Element element : elements) {
            PrintMe annotation = element.getAnnotation(PrintMe.class);
            if (element instanceof TypeElement) {
                elementsValue.add("TypeElement: " + annotation.value());

                /*===============打印包信息=================*/
                elementsValue.add("=============================打印包信息================================");
                PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(element);
                elementsValue.add("packageElement:  " + packageElement.getSimpleName().toString());
                elementsValue.add("packageElement:  " + packageElement.getQualifiedName());

                elementsValue.add("=============================打印泛型信息================================");
                List<? extends TypeParameterElement> typeParameters = ((TypeElement) element).getTypeParameters();
                for (TypeParameterElement typeParameter : typeParameters) {
                    elementsValue.add(typeParameter.getSimpleName().toString());
                }
                elementsValue.add("=============================================================");
            } else if (element instanceof ExecutableElement) {
                elementsValue.add("ExecutableElement: " + annotation.value());
            } else if (element instanceof VariableElement) {
                elementsValue.add("VariableElement: " + annotation.value());
            }

        }
        printLog();
        return true;
    }

    private void printLog() {
        TypeSpec.Builder builder = TypeSpec.classBuilder("GeneratedClass");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("testMethod");

        int len = elementsValue.size();
        for (int i = 0; i < len; i++) {
            String arg = elementsValue.get(i);
            methodBuilder.addStatement("$T arg" + i + "=$S", String.class, arg);
        }

        builder.addMethod(methodBuilder.build());

        JavaFile javaFile = JavaFile.builder("test", builder.build()).build();
        /*try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
