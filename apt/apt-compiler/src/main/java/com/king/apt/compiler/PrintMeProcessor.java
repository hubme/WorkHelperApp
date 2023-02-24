package com.king.apt.compiler;

import com.king.apt.annotations.PrintMe;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * @author VanceKing
 * @since 2019/1/14.
 */
@SupportedOptions(value = {"verbose"})
//@AutoService(Processor.class)// FIXME: 2021/11/18 使用 AutoService 方式不执行处理器
public class PrintMeProcessor extends AbstractProcessor {
    private final List<String> elementsValue = new ArrayList<>();
    private final HashMap<String, BinderClassCreator> mCreatorMap = new HashMap<>();

    private Messager mMessenger;
    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        //ProcessingEnvironment.getElementUtils(); 处理Element的工具类，用于获取程序的元素，例如包、类、方法。
        //ProcessingEnvironment.getTypeUtils(); 处理TypeMirror的工具类，用于取类信息
        //ProcessingEnvironment.getFiler(); 文件工具
        //ProcessingEnvironment.getMessager(); 错误处理工具

        mMessenger = env.getMessager();
        mElementUtils = env.getElementUtils();
    }

    /**
     * 指定此处理器支持的注解类型
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedTypes = new LinkedHashSet<>(1);
        supportedTypes.add(PrintMe.class.getCanonicalName());
        return supportedTypes;
    }

    /**
     * 如果处理器类使用{@link SupportedSourceVersion SupportedSourceVersion}注解，则返回注释中设置的版本值。
     * <p>
     * 指定此处理器支持的最新Java版本，通常返回 SourceVersion.latestSupported()
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 注解处理方法，可以在这里写扫描、评估和处理注解的代码，生成Java文件
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
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

        for (int i = 0, len = elementsValue.size(); i < len; i++) {
            String arg = elementsValue.get(i);
            methodBuilder.addStatement("$T arg" + i + "=$S", String.class, arg);
        }

        builder.addMethod(methodBuilder.build());

        JavaFile javaFile = JavaFile.builder("test", builder.build()).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
